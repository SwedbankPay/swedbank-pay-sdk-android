package com.swedbankpay.mobilesdk.nativepayments.webviewservice

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.util.RequestUtil
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.ExpectationModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.IntegrationTask
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.RequestMethod
import com.swedbankpay.mobilesdk.nativepayments.util.extension.safeLet
import com.swedbankpay.mobilesdk.nativepayments.webviewservice.client.RequestInspectorWebViewClient
import com.swedbankpay.mobilesdk.nativepayments.webviewservice.client.WebViewRequest
import com.swedbankpay.mobilesdk.nativepayments.webviewservice.util.UriUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.net.SocketTimeoutException
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


internal object WebViewService {

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
                task.method,
                task.contentType,
                task.expects,
                localStartContext
            ) { initialUrl, method, contentType, expects, context ->
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
                            continuation.resume("Y")
                        }

                        override fun shouldInterceptRequest(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): WebResourceResponse? {

                            if (request != null && request.url.toString() == initialUrl) {
                                val (webResourceRequest, completionIndicator) = postWebViewRequest(
                                    url = request.url.toString(),
                                    method = when (method) {
                                        RequestMethod.GET -> "GET"
                                        RequestMethod.POST -> "POST"
                                    },
                                    requestHeaders = request.requestHeaders,
                                    contentType = contentType,
                                    expects = expects
                                )

                                if (webResourceRequest == null) {
                                    continuation.resume(completionIndicator)
                                    val handler = Handler(Looper.getMainLooper())
                                    handler.post {
                                        view?.destroy()
                                    }
                                } else {
                                    return webResourceRequest
                                }
                            }

                            return super.shouldInterceptRequest(view, request)
                        }
                    }

                    loadUrl(initialUrl)
                }
            } ?: continuation.resume(null)
        }
    }
    //endregion

    private fun postWebViewRequest(
        url: String,
        method: String,
        requestHeaders: Map<String, String>,
        contentType: String,
        expects: List<ExpectationModel>,
    ): Pair<WebResourceResponse?, String> {
        try {
            val connection = URL(url).openConnection() as HttpsURLConnection

            connection.readTimeout = 5000
            connection.connectTimeout = 5000

            connection.requestMethod = method

            for ((key, value) in requestHeaders) {
                connection.setRequestProperty(key, value)
            }
            connection.setRequestProperty("Content-Type", contentType)

            connection.doInput = true
            connection.doOutput = true

            val postData = getDataString(expects).toByteArray(StandardCharsets.UTF_8)

            val outputStreamWriter = DataOutputStream(connection.outputStream)
            outputStreamWriter.write(postData)
            outputStreamWriter.flush()

            val responseCode = connection.responseCode

            return if (200.until(400).contains(responseCode)) {
                Pair(
                    WebResourceResponse(
                        connection.contentType.substringBefore(";"),
                        "utf-8",
                        connection.inputStream
                    ), ""
                )
            } else {
                Pair(null, "N")
            }
        } catch (timeoutException: SocketTimeoutException) {
            return Pair(null, "U")
        } catch (e: Exception) {
            return Pair(null, "N")
        }
    }

    fun getWebView(
        task: IntegrationTask,
        localStartContext: Context?,
        completionHandler: (String?) -> Unit
    ): WebView? {
        safeLet(
            task.href,
            task.method,
            task.contentType,
            task.expects,
            localStartContext
        ) { initialUrl, method, contentType, expects, context ->
            val webView = WebView(context).apply {
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

                loadUrl("https://firebasestorage.googleapis.com/v0/b/consid-beta.appspot.com/o/fake-3ds.html?alt=media")
            }

            webView.webViewClient = object : RequestInspectorWebViewClient(webView) {
                override fun shouldInterceptRequest(
                    view: WebView,
                    webViewRequest: WebViewRequest
                ): WebResourceResponse? {
                    if (webViewRequest.url == RequestUtil.NOTIFICATION_URL) {
                        val cRes = webViewRequest.formParameters["CRes"]
                        val handler = Handler(Looper.getMainLooper())
                        handler.post {
                            webView.stopLoading()
                            completionHandler.invoke(cRes)
                        }
                    }

                    /*if (webViewRequest.url == "https://firebasestorage.googleapis.com/v0/b/consid-beta.appspot.com/o/fake-3ds.html?alt=media") {
                        val (webResourceRequest, _) = postWebViewRequest(
                            url = webViewRequest.url,
                            requestHeaders = webViewRequest.headers,
                            method = "POST",
                            contentType = contentType,
                            expects = expects
                        )

                        if (webResourceRequest == null) {
                            val handler = Handler(Looper.getMainLooper())
                            handler.post {
                                webView.stopLoading()
                                completionHandler.invoke(null)
                            }
                        } else {
                            return webResourceRequest
                        }
                    }*/

                    return super.shouldInterceptRequest(view, webViewRequest)

                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val handled = request?.url != null && (
                            UriUtil.attemptHandleByExternalApp(
                                request.url.toString(),
                                context
                            ))

                    return handled || !UriUtil.webViewCanOpen(request?.url)
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