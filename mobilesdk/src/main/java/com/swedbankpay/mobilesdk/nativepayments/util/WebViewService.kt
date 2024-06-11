package com.swedbankpay.mobilesdk.nativepayments.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.ExpectationModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.IntegrationTask
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.RequestMethod
import com.swedbankpay.mobilesdk.nativepayments.util.extension.safeLet
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

    private val successUrl = "https://webhook.consid.mobi/6886fc30-3ca9-4b20-a07a-3c5ccb40463d"
    private val errorUrl = "https://webhook.consid.mobi/88bf2d0b-0e3e-46ba-92cf-09728828bb47"
    private val timeoutUrl = "https://webhook.consid.mobi/000b4bb5-c189-4abd-ae94-348461745264"
    private val testUrl = successUrl

    //region Load invisible webview
    /**
     * This method will load a sca-method-request in an invisible webview.
     * If the view succeeds to load we will return Y
     * If we get response code 4xx/5xx we will return N
     * If we get a timeout we will return U
     */
    suspend fun load(
        task: IntegrationTask,
        context: Context?
    ): String? = withContext(Dispatchers.Main) {
        suspendCoroutine { continuation ->
            safeLet(
                task.href,
                task.method,
                task.contentType,
                task.expects,
                context
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
                                    request = request,
                                    method = method,
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

    private fun postWebViewRequest(
        request: WebResourceRequest,
        method: RequestMethod,
        contentType: String,
        expects: List<ExpectationModel>,
    ): Pair<WebResourceResponse?, String> {
        try {
            val connection = URL(request.url.toString()).openConnection() as HttpsURLConnection

            connection.readTimeout = 5000
            connection.connectTimeout = 5000

            connection.requestMethod = when (method) {
                RequestMethod.GET -> "GET"
                RequestMethod.POST -> "POST"
            }

            for ((key, value) in request.requestHeaders) {
                connection.setRequestProperty(key, value)
            }
            connection.setRequestProperty("Content-Type", contentType)

            connection.doInput = true
            connection.doOutput = true

            val postData = getDataString(expects).toByteArray(StandardCharsets.UTF_8)

            val outputStreamWriter = DataOutputStream(connection.outputStream)
            outputStreamWriter.write(postData)
            outputStreamWriter.flush()

            return if (200.until(400).contains(connection.responseCode)) {
                Pair(
                    WebResourceResponse(
                        connection.contentType.substringBefore(";"),
                        "utf-8",
                        connection.inputStream
                    ), ""
                )
            } else Pair(null, "N")
        } catch (timeoutException: SocketTimeoutException) {
            return Pair(null, "U")
        } catch (e: Exception) {
            return Pair(null, "N")
        }
    }
    //endregion

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