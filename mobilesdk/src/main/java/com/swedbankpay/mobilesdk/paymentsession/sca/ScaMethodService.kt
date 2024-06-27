package com.swedbankpay.mobilesdk.paymentsession.sca

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.swedbankpay.mobilesdk.logging.BeaconService
import com.swedbankpay.mobilesdk.logging.model.EventAction
import com.swedbankpay.mobilesdk.logging.model.HttpModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.IntegrationTask
import com.swedbankpay.mobilesdk.paymentsession.sca.extension.toByteArray
import com.swedbankpay.mobilesdk.paymentsession.util.extension.safeLet
import com.swedbankpay.mobilesdk.paymentsession.util.scaMethodRequestExtensionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


internal object ScaMethodService {

    var hasError = false

    var timeoutHandler: Handler = Handler(Looper.getMainLooper())

    /**
     * This method will load a sca-method-request in an "invisible" webview.
     * If the view succeeds to load we will return Y
     * If an error occurs we will return N
     * If we get a timeout we will return U
     */
    suspend fun loadScaMethodRequest(
        task: IntegrationTask,
        localStartContext: Context?
    ): String = withContext(Dispatchers.Main) {
        suspendCoroutine { continuation ->
            val start = System.currentTimeMillis()
            safeLet(
                task.href,
                task.expects,
                localStartContext
            ) { initialUrl, expects, context ->
                WebView(context).apply {
                    settings.apply {
                        // JavaScript is required for our use case.
                        // The SDK will only use remote content through links
                        // retrieved from the backend. The backend must be careful
                        // not to send compromised links.
                        @SuppressLint("SetJavaScriptEnabled")
                        javaScriptEnabled = true
                        setSupportMultipleWindows(true)
                        javaScriptCanOpenWindowsAutomatically = true

                        // Redirect pages may require this.
                        domStorageEnabled = true

                        builtInZoomControls = true
                        displayZoomControls = false
                    }
                    webViewClient = object : WebViewClient() {

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            timeoutHandler.removeCallbacksAndMessages(null)

                            if (hasError) {
                                continuation.resume("N")
                            } else {
                                BeaconService.logEvent(
                                    EventAction.SCAMethodRequest(
                                        http = HttpModel(
                                            requestUrl = initialUrl,
                                            method = "POST",
                                        ),
                                        duration = (System.currentTimeMillis() - start).toInt(),
                                        extensions = scaMethodRequestExtensionModel("Y")
                                    )
                                )

                                continuation.resume("Y")
                            }

                            hasError = false
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onReceivedError(
                            view: WebView?,
                            errorCode: Int,
                            description: String?,
                            failingUrl: String
                        ) {
                            onWebViewError(
                                Uri.parse(failingUrl),
                                errorCode,
                                description
                            )
                        }

                        @RequiresApi(Build.VERSION_CODES.M)
                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest,
                            error: WebResourceError
                        ) {
                            if (request.isForMainFrame) {
                                onWebViewError(
                                    request.url,
                                    error.errorCode,
                                    error.description.toString()
                                )
                            }
                        }

                        override fun onReceivedHttpError(
                            view: WebView?,
                            request: WebResourceRequest,
                            errorResponse: WebResourceResponse
                        ) {
                            if (request.isForMainFrame) {
                                onWebViewError(
                                    request.url,
                                    errorResponse.statusCode,
                                    errorResponse.reasonPhrase
                                )
                            }
                        }

                        private fun onWebViewError(
                            uri: Uri,
                            errorCode: Int,
                            description: String?
                        ) {
                            hasError = true

                            BeaconService.logEvent(
                                EventAction.SCAMethodRequest(
                                    http = HttpModel(
                                        requestUrl = uri.toString(),
                                        method = "POST",
                                        responseStatusCode = errorCode
                                    ),
                                    duration = (System.currentTimeMillis() - start).toInt(),
                                    extensions = scaMethodRequestExtensionModel(
                                        "N",
                                        description,
                                        errorCode
                                    )
                                )
                            )
                        }
                    }

                    timeoutHandler.postDelayed({
                        continuation.resume("U")
                    }, 5000)

                    postUrl(initialUrl, expects.toByteArray())
                }
            } ?: continuation.resume("N")
        }
    }
}