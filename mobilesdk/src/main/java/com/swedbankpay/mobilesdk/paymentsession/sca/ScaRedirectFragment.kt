package com.swedbankpay.mobilesdk.paymentsession.sca

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.swedbankpay.mobilesdk.R
import com.swedbankpay.mobilesdk.logging.BeaconService
import com.swedbankpay.mobilesdk.logging.model.EventAction
import com.swedbankpay.mobilesdk.logging.model.HttpModel
import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants
import com.swedbankpay.mobilesdk.paymentsession.api.model.SwedbankPayAPIError
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.IntegrationTask
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentSessionProblem
import com.swedbankpay.mobilesdk.paymentsession.sca.extension.toByteArray
import com.swedbankpay.mobilesdk.paymentsession.sca.util.ScaRedirectUtil
import com.swedbankpay.mobilesdk.paymentsession.util.extension.safeLet
import com.swedbankpay.mobilesdk.paymentsession.util.scaRedirectResultExtensionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class ScaRedirectFragment(
    private val task: IntegrationTask,
    private val errorHandler: (PaymentSessionProblem) -> Unit,
    private val completionHandler: (String?) -> Unit
) : Fragment() {

    private var webView: WebView? = null
    private var progressIndicator: CircularProgressIndicator? = null

    private var loadingJob: Job? = null
    private var timeoutHandler: Handler = Handler(Looper.getMainLooper())

    private var hasError = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sca_redirect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view.findViewById(R.id.sca_redirect_web_view)
        progressIndicator = view.findViewById(R.id.sca_redirect_loading_indicator)

        // Remove this scope when testing is done
        lifecycleScope.launch(Dispatchers.Main) {
            val start = System.currentTimeMillis()

            loadingJob = lifecycleScope.launch {
                delay(1000)
                progressIndicator?.visibility = View.VISIBLE
            }

            safeLet(
                task.href,
                task.expects,
            ) { initialUrl, expects ->
                webView?.apply {
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
                    }, 5000)

                    postUrl(initialUrl, expects.toByteArray())
                }

                webView?.webViewClient = object : WebViewClient() {

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        if (!hasError) {
                            loadingJob?.cancel()
                            loadingJob = null
                            progressIndicator?.visibility = View.GONE
                            webView?.visibility = View.VISIBLE
                        }
                        hasError = false
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
                        if (uri.toString()
                                .startsWith(PaymentSessionAPIConstants.NOTIFICATION_URL)
                        ) {
                            val cres = uri?.getQueryParameter(
                                PaymentSessionAPIConstants.CRES
                            )

                            BeaconService.logEvent(
                                EventAction.SCARedirectResult(
                                    http = HttpModel(
                                        requestUrl = initialUrl,
                                        method = "POST",
                                    ),
                                    duration = (System.currentTimeMillis() - start).toInt(),
                                    extensions = scaRedirectResultExtensionModel(cres != null)
                                )
                            )

                            val handler = Handler(Looper.getMainLooper())
                            handler.post {
                                completionHandler.invoke(cres)
                            }
                            webView?.stopLoading()
                            return true
                        }

                        val handled = uri != null && (
                                ScaRedirectUtil.attemptHandleByExternalApp(
                                    uri.toString(),
                                    requireContext()
                                ))

                        return handled || !ScaRedirectUtil.webViewCanOpen(uri)
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
                            EventAction.SCARedirectResult(
                                http = HttpModel(
                                    requestUrl = uri.toString(),
                                    method = "POST",
                                ),
                                duration = (System.currentTimeMillis() - start).toInt(),
                                extensions = scaRedirectResultExtensionModel(
                                    false,
                                    description,
                                    errorCode
                                )
                            )
                        )

                        val error = PaymentSessionProblem.PaymentSession3DSecureFragmentLoadFailed(
                            error = SwedbankPayAPIError.Error(
                                message = description, responseCode = errorCode
                            ),
                            retry = {
                                safeLet(task.href, task.expects) { href, expects ->
                                    webView?.postUrl(href, expects.toByteArray())
                                }
                                    ?: errorHandler.invoke(PaymentSessionProblem.InternalInconsistencyError)
                            }
                        )

                        errorHandler.invoke(error)
                    }
                }
            } ?: errorHandler.invoke(PaymentSessionProblem.InternalInconsistencyError)

        }
    }
}