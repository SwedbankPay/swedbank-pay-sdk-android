package com.swedbankpay.mobilesdk.internal

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.webkit.*
import androidx.lifecycle.*
import com.swedbankpay.mobilesdk.R
import okhttp3.internal.toHexString
import java.net.URISyntaxException

internal class WebViewModel(application: Application) : AndroidViewModel(application) {
    sealed class JSDialogInfo<T : JsResult>(
        val message: String?,
        val result: T
    ) {
        class Alert(message: String?, result: JsResult) : JSDialogInfo<JsResult>(message, result)
        class Confirm(message: String?, result: JsResult) : JSDialogInfo<JsResult>(message, result)
        class Prompt(message: String?, result: JsPromptResult, val defaultValue: String?) : JSDialogInfo<JsPromptResult>(message, result)
    }

    private companion object {
        private val INTENT_URI_FLAGS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Intent.URI_ANDROID_APP_SCHEME or Intent.URI_INTENT_SCHEME
        } else {
            Intent.URI_INTENT_SCHEME
        }
    }

    // Yes, a View is part of a ViewModel. Blasphemy.
    // Anyways, as ViewModel is the preferred way of retaining
    // objects over Fragment destruction and recreation
    // on configuration changes, this is what we have to do.
    // Anyway, a WebView is a monolith that contains both "view model" state
    // and "view" presentation. As long as it does not offer an API to
    // its internal view model, this remains the only way to persist it
    // (saveState/restoreState would seem to do that, but they are insufficient).
    private var webView: WebView? = null

    private var currentBaseUrl: String? = null
    private var lastRootHtml: String? = null

    val failedIntentUris = MutableLiveData<List<Uri>>()

    private val javascriptDialogs = MutableLiveData<Map<String, JSDialogInfo<*>>>()
    val javascriptDialogTags = Transformations.map(javascriptDialogs) { it?.keys ?: emptySet() }
    private var nextTag = 0L
    private fun addDialog(info: JSDialogInfo<*>) {
        val tag = nextTag.toHexString()
        // Will overflow if a single WebViewFragment attempts to show more than 2^64 dialogs.
        // Oh well.
        nextTag++
        val entry = Pair(tag, info)
        javascriptDialogs.apply {
            value = (value ?: emptyMap()).plus(entry)
        }
    }
    fun getDialogInfo(tag: String) = javascriptDialogs.value?.get(tag)
    fun consumeDialogInfo(tag: String): JSDialogInfo<*>? {
        val dialogs = javascriptDialogs.value
        return dialogs?.get(tag)?.also {
            javascriptDialogs.value = dialogs.minus(tag)
        }
    }

    fun setup(context: Context, internalPaymentViewModelProvider: ViewModelProvider) {
        if (webView == null) {
            webView = WebView(context).apply {
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

                webChromeClient = MyWebChromeClient()
                val parentViewModel = internalPaymentViewModelProvider.get<InternalPaymentViewModel>()
                webViewClient = MyWebViewClient(parentViewModel)

                addJavascriptInterface(
                    parentViewModel.javascriptInterface,
                    getApplication<Application>().getString(R.string.swedbankpaysdk_javascript_interface_name)
                )
            }
        }
    }

    override fun onCleared() {
        javascriptDialogs.value = null
        webView?.apply {
            webChromeClient = null
            webViewClient = null
            removeJavascriptInterface(getApplication<Application>().getString(R.string.swedbankpaysdk_javascript_interface_name))
            destroy()
        }
        webView = null
    }

    fun requireWebView() = requireNotNull(webView) { "setup not called" }

    fun isShowingContentRootedAt(htmlString: String) = htmlString == lastRootHtml

    fun clearContent() {
        currentBaseUrl = null
        lastRootHtml = null
        javascriptDialogs.value = null
        requireWebView().apply {
            clearHistory()
            loadUrl("about:blank")
        }
    }

    fun loadContent(baseUrl: String, htmlString: String) {
        currentBaseUrl = baseUrl
        lastRootHtml = htmlString
        javascriptDialogs.value = null
        requireWebView().apply {
            clearHistory()
            loadDataWithBaseURL(baseUrl, htmlString, "text/html", "utf-8", "")
        }
    }

    private inner class MyWebViewClient(
        private val parentViewModel: InternalPaymentViewModel
    ) : WebViewClient() {
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
            val handled = uri != null && (
                    attemptHandleByViewModel(uri)
                            || attemptHandleIntentUri(uri)
                            || attemptHandleByExternalApp(uri)
                    )
            return handled || !webViewCanOpen(uri)
        }

        private fun webViewCanOpen(uri: Uri?) = when (uri?.scheme) {
            // Allow about:blank. This is mostly useful for testing.
            "about" -> uri.schemeSpecificPart == "blank"
            "http", "https" -> true
            else -> false
        }

        private fun attemptHandleByViewModel(uri: Uri): Boolean {
            return parentViewModel.overrideNavigation(uri)
        }

        private fun attemptHandleIntentUri(uri: Uri): Boolean {
            val scheme = uri.scheme
            if (scheme != "intent" && scheme != "android-app") return false

            val success = try {
                val intent = getIntentUriIntent(uri)
                intent?.let(getApplication<Application>()::startActivity)
                intent != null
            } catch (_: Exception) {
                false
            }
            if (!success) {
                failedIntentUris.apply {
                    value = value?.plus(uri) ?: listOf(uri)
                }
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
                getApplication<Application>().packageManager
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
                    getApplication<Application>().packageManager
                        .resolveActivity(it, PackageManager.MATCH_DEFAULT_ONLY) != null
                }
        }

        private fun attemptHandleByExternalApp(uri: Uri): Boolean {
            getExternalAppIntent(uri)?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                try {
                    getApplication<Application>().startActivity(it)
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
                val resolveInfo = getApplication<Application>().packageManager
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
            if (parentViewModel.useExternalBrowser) return true
            return when (uri.scheme) {
                // Open about:blank in WebView (useful for testing)
                "about" -> uri.schemeSpecificPart != "blank"
                "http", "https" ->
                    // only open http(s) links in external apps if the intent filter is a "good" match
                    resolveInfo.match and IntentFilter.MATCH_CATEGORY_MASK >= IntentFilter.MATCH_CATEGORY_HOST
                else -> true
            }
        }
    }

    private inner class MyWebChromeClient : WebChromeClient() {
        override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult
        ): Boolean {
            addDialog(JSDialogInfo.Alert(
                message = message,
                result = result)
            )
            return true
        }

        override fun onJsConfirm(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult
        ): Boolean {
            addDialog(JSDialogInfo.Confirm(
                message = message,
                result = result
            ))
            return true
        }

        override fun onJsPrompt(
            view: WebView?,
            url: String?,
            message: String?,
            defaultValue: String?,
            result: JsPromptResult
        ): Boolean {
            addDialog(JSDialogInfo.Prompt(
                message = message,
                defaultValue = defaultValue,
                result = result
            ))
            return true
        }
    }
}