package com.swedbankpay.mobilesdk.internal

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.swedbankpay.mobilesdk.R
import java.net.URISyntaxException

internal class WebViewFragment : Fragment() {
    private companion object {
        private const val STATE_WEBVIEW = "W"
        private const val STATE_BASE_HTML = "B"

        private val INTENT_URI_FLAGS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Intent.URI_ANDROID_APP_SCHEME or Intent.URI_INTENT_SCHEME
        } else {
            Intent.URI_INTENT_SCHEME
        }
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

    private fun showIntentUriErrorDialog(uri: Uri) {
        ErrorDialogFragment.newInstance(uri).show(childFragmentManager, null)
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
            return uri != null && (
                    attemptHandleByViewModel(uri)
                            || attemptHandleIntentUri(uri)
                            || attemptHandleByExternalApp(uri)
                    )
        }

        private fun attemptHandleByViewModel(uri: Uri): Boolean {
            val parentFragment = parentFragment ?: return false
            val context = context ?: return false
            val vm = ViewModelProviders.of(parentFragment)[InternalPaymentViewModel::class.java]
            return vm.overrideNavigation(context, uri)
        }

        private fun attemptHandleIntentUri(uri: Uri): Boolean {
            val scheme = uri.scheme
            if (scheme != "intent" && scheme != "android-app") return false

            val success = try {
                val intent = getIntentUriIntent(uri)
                intent?.let(::startActivity)
                intent != null
            } catch (_: Exception) {
                false
            }
            if (!success) {
                showIntentUriErrorDialog(uri)
            }

            // Always intercept "intent" or "android-app" uris,
            // as the WebView would choke on them anyway.
            return true
        }

        private fun getIntentUriIntent(uri: Uri): Intent? {
            val intent = try {
                Intent.parseUri(uri.toString(), INTENT_URI_FLAGS)
            } catch (_: URISyntaxException) {
                null
            }
            intent?.addCategory(Intent.CATEGORY_BROWSABLE)
            val resolvedIntent = intent?.takeIf {
                requireContext().packageManager
                    .resolveActivity(it, PackageManager.MATCH_DEFAULT_ONLY) != null
            }
            return resolvedIntent ?: intent?.let(::getFallbackIntent)
        }

        private fun getFallbackIntent(intent: Intent): Intent? {
            return intent.getStringExtra("browser_fallback_url")
                ?.let(Uri::parse)
                ?.let { Intent(Intent.ACTION_VIEW, it) }
                ?.apply { addCategory(Intent.CATEGORY_BROWSABLE) }
                ?.takeIf {
                    requireContext().packageManager
                        .resolveActivity(it, PackageManager.MATCH_DEFAULT_ONLY) != null
                }
        }

        private fun attemptHandleByExternalApp(uri: Uri): Boolean {
            getExternalAppIntent(uri)?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                try {
                    startActivity(it)
                    return true
                } catch (_: Exception) {}
            }
            // Don't intercept the load if an activity was not actually started;
            // this allows the iframe or whatnot to get the error.
            return false
        }

        private fun getExternalAppIntent(uri: Uri): Intent? {
            return Intent(Intent.ACTION_VIEW, uri).apply {
                getPackageNameForScheme(scheme)?.let(::setPackage)
                addCategory(Intent.CATEGORY_BROWSABLE)
            }.takeIf {
                val resolveInfo = requireContext().packageManager
                    .resolveActivity(it, PackageManager.MATCH_DEFAULT_ONLY)
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

    class ErrorDialogFragment : DialogFragment() {
        companion object {
            const val ARG_URI = "U"

            fun newInstance(uri: Uri) = ErrorDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URI, uri.toString())
                }
            }
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val context = requireContext()
            return AlertDialog.Builder(context, theme)
                .setTitle(R.string.swedbankpaysdk_intent_failure_title)
                .setMessage(context.formatMessage())
                .setNeutralButton(R.string.swedbankpaysdk_intent_failure_close) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
        }

        private fun Context.formatMessage(): String {
            val packageName = getFailingPackage() ?: getString(R.string.swedbankpaysdk_intent_failure_unknown)
            return getString(R.string.swedbankpaysdk_intent_failure_body, packageName)
        }


        private fun getFailingPackage(): String? {
            val uri = arguments?.getString(ARG_URI)
            val intent = try {
                uri?.let { Intent.parseUri(it, INTENT_URI_FLAGS) }
            } catch (_: URISyntaxException) {
                null
            }
            return intent?.`package`
        }
    }
}
