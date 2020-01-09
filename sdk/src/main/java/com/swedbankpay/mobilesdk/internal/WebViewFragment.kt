package com.swedbankpay.mobilesdk.internal

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.swedbankpay.mobilesdk.R

internal class WebViewFragment : Fragment() {
    private companion object {
        private const val STATE_WEBVIEW = "W"
        private const val STATE_BASE_HTML = "B"
    }

    private var webView: WebView? = null
    private var webViewActive = false
    private val activeWebView: WebView? get() = webView.takeIf { webViewActive }

    private var currentBaseUrl: String? = null
    private var lastBaseHtml: String? = null

    private var restored = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // only save webview state if it is not showing the initial page
        if (currentBaseUrl == null) {
            lastBaseHtml?.let { html ->
                activeWebView?.apply {
                    val webViewState = Bundle().takeIf {
                        saveState(it) != null
                    }
                    webViewState?.let {
                        outState.putBundle(STATE_WEBVIEW, it)
                        outState.putString(STATE_BASE_HTML, html)
                    }
                }
            }
        }
    }

    private fun restoreInstanceState(savedInstanceState: Bundle, webView: WebView) {
        val html = savedInstanceState.getString(STATE_BASE_HTML)
        val state = savedInstanceState.getBundle(STATE_WEBVIEW)
        if (html != null && state != null && webView.restoreState(state) != null) {
            currentBaseUrl = null // we never save a "root" state
            lastBaseHtml = html
            restored = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return WebView(inflater.context).apply {
            webView?.destroy()
            webView = this
            webViewActive = true

            webViewClient = MyWebViewClient()
            settings.apply {
                // JavaScript is required for our use case.
                // The SDK will only use remote content through links
                // retrieved from the backend. The backend must be careful
                // not to send compromised links.
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true

                // Redirect pages may require this.
                domStorageEnabled = true
            }
            val vm = ViewModelProviders.of(requireParentFragment())[InternalPaymentViewModel::class.java]
            addJavascriptInterface(vm.javascriptInterface, getString(R.string.swedbankpaysdk_javascript_interface_name))

            savedInstanceState?.let {
                restoreInstanceState(it, this)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        activeWebView?.onPause()
    }

    override fun onResume() {
        activeWebView?.onResume()
        super.onResume()
    }

    override fun onDestroyView() {
        webViewActive = false
        super.onDestroyView()
    }

    override fun onDestroy() {
        webView?.destroy()
        webView = null
        super.onDestroy()
    }

    fun clear() {
        currentBaseUrl = null
        lastBaseHtml = null
        restored = false

        activeWebView?.apply {
            clearHistory()
            loadUrl("about:blank")
        }
    }

    fun load(baseUrl: String, htmlString: String): Boolean {
        if (restored) {
            // we were restored, so do not load the page if it is the root page
            val wouldReloadBase = htmlString == lastBaseHtml
            // in any case we are now done with the restore process
            restored = false
            if (wouldReloadBase) return false
        }

        currentBaseUrl = baseUrl
        lastBaseHtml = htmlString
        activeWebView?.apply {
            clearHistory()
            loadDataWithBaseURL(baseUrl, htmlString, "text/html", "utf-8", "")
        }
        return true
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            if (url != currentBaseUrl) {
                currentBaseUrl = null
            }
        }

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

        private fun shouldOverrideUrlLoading(uri: Uri?): Boolean {
            return uri != null
                    && (attemptHandleByCallback(uri) || attemptHandleByExternalApp(uri))
        }

        private fun attemptHandleByCallback(uri: Uri): Boolean {
            val parentFragment = parentFragment ?: return false
            val context = context ?: return false
            val vm = ViewModelProviders.of(parentFragment)[InternalPaymentViewModel::class.java]
            return vm.overrideNavigation(context, uri)
        }

        private fun attemptHandleByExternalApp(uri: Uri): Boolean {
            getExternalAppIntent(uri)?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                try {
                    startActivity(it)
                    return true
                } catch (_: Exception) {}
            }
            return false
        }

        private fun getExternalAppIntent(uri: Uri): Intent? {
            return Intent(Intent.ACTION_VIEW, uri).apply {
                getPackageNameForScheme(scheme)?.let(::setPackage)
                addCategory(Intent.CATEGORY_BROWSABLE)
            }.takeIf {
                val resolveInfo = requireContext().packageManager.resolveActivity(it, PackageManager.MATCH_DEFAULT_ONLY)
                resolveInfo != null && shouldStartActivity(uri, resolveInfo)
            }
        }

        private fun getPackageNameForScheme(scheme: String?) = when (scheme) {
            "swish" -> "se.bankgirot.swish"
            "vipps" -> "no.dnb.vipps"
            else -> null
        }

        private fun shouldStartActivity(uri: Uri, resolveInfo: ResolveInfo): Boolean {
            return when (uri.scheme) {
                "http", "https" ->
                    // only open http(s) links in external apps if the intent filter is a "good" match
                    resolveInfo.match and IntentFilter.MATCH_CATEGORY_MASK >= IntentFilter.MATCH_CATEGORY_HOST
                else -> true
            }
        }
    }
}
