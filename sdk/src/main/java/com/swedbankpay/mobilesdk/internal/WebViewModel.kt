package com.swedbankpay.mobilesdk.internal

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Message
import android.webkit.*
import android.webkit.WebView.WebViewTransport
import androidx.lifecycle.*
import com.swedbankpay.mobilesdk.R
import okhttp3.internal.toHexString

private fun <T> MutableLiveData<List<T>?>.add(t: T) {
    value = value?.plus(t) ?: listOf(t)
}

internal class WebViewModel(application: Application) : AndroidViewModel(application) {
    sealed class JSDialogInfo<T : JsResult>(
        val message: String?,
        val result: T
    ) {
        class Alert(message: String?, result: JsResult) : JSDialogInfo<JsResult>(message, result)
        class Confirm(message: String?, result: JsResult) : JSDialogInfo<JsResult>(message, result)
        class Prompt(message: String?, result: JsPromptResult, val defaultValue: String?) : JSDialogInfo<JsPromptResult>(message, result)
    }

    // Yes, a View is part of a ViewModel. Blasphemy.
    // Anyways, as ViewModel is the preferred way of retaining
    // objects over Fragment destruction and recreation
    // on configuration changes, this is what we have to do.
    // Anyway, a WebView is a monolith that contains both "view model" state
    // and "view" presentation. As long as it does not offer an API to
    // its internal view model, this remains the only way to persist it
    // (saveState/restoreState would seem to do that, but they are insufficient).
    @SuppressLint("StaticFieldLeak") // we will release it in onCleared()
    private var webView: WebView? = null

    private var lastRootHtml: String? = null

    val intentUris = MutableLiveData<List<Uri>?>()
    val externalAppIntents = MutableLiveData<List<Intent>?>()

    private val javascriptDialogs = MutableLiveData<Map<String, JSDialogInfo<*>>?>()
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

    // Explicit null initial value so observers are called with the initial null value as well.
    // Without this, some corner cases of fragment view recreation can result
    // in inconsistent states.
    val extraWebView = MutableLiveData<WebView?>(null)

    fun setup(
        context: Context,
        internalPaymentViewModelProvider: ViewModelProvider,
    ) {
        if (webView == null) {
            webView = WebView(context).apply {
                settings.apply {
                    // JavaScript is required for our use case.
                    // The SDK will only use remote content through links
                    // retrieved from the backend. The backend must be careful
                    // not to send compromised links.
                    @SuppressLint("SetJavaScriptEnabled")
                    javaScriptEnabled = true
                    setSupportMultipleWindows(true)

                    // Redirect pages may require this.
                    domStorageEnabled = true

                    builtInZoomControls = true
                    displayZoomControls = false
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

    fun onPause() {
        requireWebView().onPause()
        extraWebView.value?.onPause()
    }

    fun onResume() {
        requireWebView().onResume()
        extraWebView.value?.onResume()
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
        removeExtraWebView()
    }

    fun requireWebView() = requireNotNull(webView) { "setup not called" }

    fun isShowingContentRootedAt(htmlString: String) = htmlString == lastRootHtml

    fun clearContent() {
        lastRootHtml = null
        javascriptDialogs.value = null
        requireWebView().apply {
            clearHistory()
            loadUrl("about:blank")
        }
        removeExtraWebView()
    }

    fun loadContent(baseUrl: String?, htmlString: String) {
        lastRootHtml = htmlString
        javascriptDialogs.value = null
        requireWebView().apply {
            clearHistory()
            loadDataWithBaseURL(baseUrl, htmlString, "text/html", "utf-8", "")
        }
    }

    fun removeExtraWebView() {
        replaceExtraWebView(null)
    }

    private fun replaceExtraWebView(newExtraWebView: WebView?) {
        val removed = extraWebView.value
        extraWebView.value = newExtraWebView
        removed?.destroy()
    }

    private inner class MyWebViewClient(
        private val parentViewModel: InternalPaymentViewModel
    ) : WebViewClient() {
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
            val override = handled || !webViewCanOpen(uri)
            if (!override) {
                // loadDataWithBaseURL does not call into here,
                // so any calls here mean we are going to show a page other than the root page
                parentViewModel.webViewShowingRootPage.value = false
            }
            return override
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
            val isIntentUri = scheme == "intent" || scheme == "android-app"
            if (isIntentUri) {
                // Further handling is in the WebViewFragment,
                // as starting an Activity requires an Activity context.
                intentUris.add(uri)
            }
            return isIntentUri
        }

        private fun attemptHandleByExternalApp(uri: Uri): Boolean {
            // Check if a matching activity can be found here,
            // so that the WebView can know of failed navigations
            // to app urls. (N.B! Such is a legacy approach to launching
            // apps from web content, but we might as well do our best
            // to support it.)
            val intent = getExternalAppIntent(uri)
            // Further handling is in the WebViewFragment,
            // as starting an Activity requires an Activity context.
            intent?.let(externalAppIntents::add)
            return intent != null
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

        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            view ?: return false
            resultMsg ?: return false

            val webView = WebView(view.context)

            webView.settings.apply {
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true
                setSupportMultipleWindows(false)
                domStorageEnabled = false
                builtInZoomControls = false
                displayZoomControls = false
            }

            webView.webChromeClient = WebChromeClient()
            webView.webViewClient = WebViewClient()

            // currently, we only support one extra web view at a time
            replaceExtraWebView(webView)

            val transport = resultMsg.obj as WebViewTransport
            transport.webView = webView
            resultMsg.sendToTarget()

            return true
        }
    }
}