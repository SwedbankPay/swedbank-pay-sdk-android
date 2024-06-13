package com.swedbankpay.mobilesdk.nativepayments.webviewservice.client

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient


@SuppressLint("SetJavaScriptEnabled")
open class RequestInspectorWebViewClient(
    webView: WebView,
) : WebViewClient() {

    private val interceptionJavascriptInterface = RequestInspectorJavaScriptInterface(webView)

    init {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
    }

    final override fun shouldInterceptRequest(
        view: WebView,
        request: WebResourceRequest
    ): WebResourceResponse? {
        val recordedRequest = interceptionJavascriptInterface.findRecordedRequestForUrl(
            request.url.toString()
        )

        val webViewRequest = WebViewRequest.create(request, recordedRequest)
        return shouldInterceptRequest(view, webViewRequest)
    }

    open fun shouldInterceptRequest(
        view: WebView,
        webViewRequest: WebViewRequest
    ): WebResourceResponse? {
        return null
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        RequestInspectorJavaScriptInterface.enabledRequestInspection(view)
        super.onPageStarted(view, url, favicon)
    }

}