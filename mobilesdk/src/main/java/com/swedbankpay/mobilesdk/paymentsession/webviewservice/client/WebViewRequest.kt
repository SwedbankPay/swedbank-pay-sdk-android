package com.swedbankpay.mobilesdk.paymentsession.webviewservice.client

import android.os.Build
import android.webkit.CookieManager
import android.webkit.WebResourceRequest

data class WebViewRequest(
    val type: WebViewRequestType,
    val url: String,
    val method: String,
    val formParameters: Map<String, String>,
    val headers: Map<String, String>,
    val trace: String,
    val enctype: String?,
    val isForMainFrame: Boolean,
    val isRedirect: Boolean,
    val hasGesture: Boolean
) {

    companion object {
        internal fun create(
            webResourceRequest: WebResourceRequest,
            recordedRequest: RequestInspectorJavaScriptInterface.RecordedRequest?
        ): WebViewRequest {
            val type = recordedRequest?.type ?: WebViewRequestType.HTML
            val url = webResourceRequest.url.toString()
            val cookies = CookieManager.getInstance().getCookie(url) ?: ""
            val headers = HashMap<String, String>()
            headers["cookie"] = cookies
            if (recordedRequest != null) {
                val recordedHeadersInLowercase = recordedRequest.headers.mapKeys { (key, _) ->
                    key.lowercase()
                }
                headers.putAll(recordedHeadersInLowercase)
            }
            val requestHeadersInLowercase = webResourceRequest.requestHeaders.mapKeys { (key, _) ->
                key.lowercase()
            }
            headers.putAll(requestHeadersInLowercase)

            val isRedirect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                webResourceRequest.isRedirect
            } else {
                false
            }
            return WebViewRequest(
                type = type,
                url = url,
                method = webResourceRequest.method,
                headers = headers,
                trace = recordedRequest?.trace ?: "",
                enctype = recordedRequest?.enctype,
                isForMainFrame = webResourceRequest.isForMainFrame,
                isRedirect = isRedirect,
                hasGesture = webResourceRequest.hasGesture(),
                formParameters = recordedRequest?.formParameters ?: mapOf()
            )
        }
    }
}