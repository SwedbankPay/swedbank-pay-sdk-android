package com.swedbankpay.mobilesdk.paymentsession.webviewservice

import android.annotation.SuppressLint
import android.annotation.TargetApi
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
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.util.RequestUtil
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ExpectationModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.IntegrationTask
import com.swedbankpay.mobilesdk.paymentsession.util.extension.safeLet
import com.swedbankpay.mobilesdk.paymentsession.webviewservice.util.UriUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


internal object WebViewService {

    data class WebViewError(
        val uri: Uri,
        val statusCode: Int,
        val description: String?
    )

    var webViewError: WebViewError? = null

    var timeoutHandler: Handler = Handler(Looper.getMainLooper())

    //region Load invisible webview
    /**
     * This method will load a sca-method-request in an invisible webview.
     * If the view succeeds to load we will return Y
     * If we get response code 4xx/5xx we will return N
     * If we get a timeout we will return U
     */
    suspend fun load(
        task: IntegrationTask,
        localStartContext: Context?
    ): String? = withContext(Dispatchers.Main) {
        suspendCoroutine { continuation ->
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

                            if (webViewError != null) {
                                continuation.resume("N")
                            } else {
                                continuation.resume("Y")
                            }

                            webViewError = null
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
                            statusCode: Int,
                            description: String?
                        ) {
                            webViewError = WebViewError(
                                uri = uri,
                                statusCode = statusCode,
                                description = description
                            )
                        }
                    }

                    timeoutHandler.postDelayed({
                        continuation.resume("U")
                    }, 5000)

                    postUrl(initialUrl, getDataString(expects).toByteArray())
                }
            } ?: continuation.resume(null)
        }
    }
    //endregion

    fun getWebView(
        task: IntegrationTask,
        localStartContext: Context?,
        completionHandler: (String?) -> Unit
    ): WebView? {
        safeLet(
            task.href,
            task.expects,
            localStartContext
        ) { initialUrl, expects, context ->
            val webView = WebView(context).apply {
                settings.apply {
                    @SuppressLint("SetJavaScriptEnabled")
                    javaScriptEnabled = true
                    setSupportMultipleWindows(true)
                    javaScriptCanOpenWindowsAutomatically = true

                    // Redirect pages may require this.
                    domStorageEnabled = true

                    builtInZoomControls = true
                    displayZoomControls = false
                }

                timeoutHandler.postDelayed({
                    completionHandler.invoke(null)
                }, 5000)

                postUrl(initialUrl, getDataString(expects).toByteArray())
            }

            webView.webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    timeoutHandler.removeCallbacksAndMessages(null)
                }

                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return shouldOverrideUrlLoading(url?.let(Uri::parse))
                }

                @TargetApi(Build.VERSION_CODES.N)
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return shouldOverrideUrlLoading(request?.url)
                }

                private fun shouldOverrideUrlLoading(
                    uri: Uri?
                ): Boolean {
                    if (uri.toString().startsWith(RequestUtil.NOTIFICATION_URL)) {
                        val handler = Handler(Looper.getMainLooper())

                        handler.post {
                            completionHandler.invoke(uri?.getQueryParameter("cres"))
                        }
                        webView.stopLoading()
                        return true
                    }

                    val handled = uri != null && (
                            UriUtil.attemptHandleByExternalApp(
                                uri.toString(),
                                context
                            ))

                    return handled || !UriUtil.webViewCanOpen(uri)
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
                    statusCode: Int,
                    description: String?
                ) {
                    webViewError = WebViewError(
                        uri = uri,
                        statusCode = statusCode,
                        description = description
                    )

                    completionHandler.invoke(null)
                }
            }

            return webView

        } ?: return null

    }

    private fun getDataString(expects: List<ExpectationModel>): String {
        val result = StringBuilder()
        var first = true
        for (e in expects) {
            if (first) first = false else result.append("&")
            if (e.value is String) {
                result.append(URLEncoder.encode(e.name, "UTF-8"))
                result.append("=")
                result.append(URLEncoder.encode(e.value, "UTF-8"))
            }
        }
        return result.toString()
    }
}