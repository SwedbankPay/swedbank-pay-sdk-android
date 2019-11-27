package com.swedbankpay.mobilesdk

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.CallSuper
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.PaymentFragment.ArgumentsBuilder
import com.swedbankpay.mobilesdk.PaymentFragment.Companion.defaultConfiguration
import com.swedbankpay.mobilesdk.internal.CallbackActivity
import com.swedbankpay.mobilesdk.internal.InternalPaymentViewModel
import com.swedbankpay.mobilesdk.internal.ToSActivity
import kotlinx.android.synthetic.main.swedbankpaysdk_payment_fragment.*
import kotlinx.android.synthetic.main.swedbankpaysdk_payment_fragment.view.*

/**
 * A [Fragment] that handles a payment process.
 *
 * A PaymentFragment is single-shot: It is configured for a single payment
 * and cannot be reused. You must create a new PaymentFragment for every payment
 * the user makes.
 *
 * You need a [Configuration] object for system-level setup. Obtain one
 * from a [Configuration.Builder]. Usually you only need one [Configuration], which
 * you can set as [defaultConfiguration]. For advanced use-cases, override [getConfiguration]
 * instead.
 *
 * You must set the [arguments][setArguments] of a PaymentFragment before use.
 * The argument [Bundle] is created by [ArgumentsBuilder].
 * Payments are always made by a [Consumer], which is [anonymous][Consumer.ANONYMOUS] by default.
 * You should also supply some data that your backend will use to construct
 * the payment details. The format of this merchant specific data is not
 * otherwise specified, but it will be converted to a JSON representation:
 * You can use POJOs, [Lists][List], and [Maps][Map] for the structure; leaf values
 * must be primitives, [Strings][String] or nulls. See [ArgumentsBuilder] for further options.
 *
 * You may observe the state of the PaymentFragment via [PaymentViewModel].
 * Access the PaymentViewModel through the containing [activity][androidx.fragment.app.FragmentActivity]:
 *
 *     ViewModelProviders.of(activity).get(PaymentViewModel.class)
 *
 * Optionally, you may specify a custom [ViewModelProvider][androidx.lifecycle.ViewModelProvider]
 * key by [ArgumentsBuilder.viewModelKey],
 * e.g. if you want to support multiple PaymentFragments in an Activity (not recommended).
 *
 * After configuring the PaymentFragment, [add][androidx.fragment.app.FragmentTransaction.add] it to
 * your [activity][androidx.fragment.app.FragmentActivity]. The payment process will begin as soon as the
 * PaymentFragment is started (i.e. visible to the user).
 *
 * The containing [activity][androidx.fragment.app.FragmentActivity] should observe
 * [PaymentViewModel.state]; at a minimum it should hide the PaymentFragment when
 * [PaymentViewModel.State.isFinal] is `true`.
 *
 * Subclassing notes
 * -----------------
 *
 * The correct functioning of PaymentFragment depends on the argument [Bundle] being
 * created with [ArgumentsBuilder]. If your subclass needs custom arguments,
 * you should add the default arguments to your argument [Bundle] by [ArgumentsBuilder.build].
 *
 * If you override [onCreateView], you must call the superclass implementation and add the [View]
 * returned by that method to your layout (or use it as the return value).
 *
 * If you override [onSaveInstanceState], you must call the superclass implementation.
 *
 * All [Bundle] keys used by PaymentFragment are namespaced by a "com.swedbankpay.mobilesdk" prefix,
 * so they should not conflict with any custom keys.
 *
 * @constructor Creates a new, unconfigured instance of PaymentFragment. It must be [configured][ArgumentsBuilder] before use.
 */
open class PaymentFragment : Fragment() {
    /**
     * Builder class for the argument [Bundle] used by PaymentFragment.
     */
    @Suppress("unused")
    class ArgumentsBuilder {
        private var consumer = Consumer.ANONYMOUS
        private var merchantData: Any? = null
        private var viewModelKey: String? = null
        @DefaultUI
        private var enabledDefaultUI = RETRY_PROMPT

        /**
         * Sets the consumer for this payment. Defaults to [Consumer.ANONYMOUS].
         * @param consumer the consumer making this payment
         */
        fun consumer(consumer: Consumer) = apply { this.consumer = consumer }

        /**
         * Sets the merchant data for this payment.
         * @param merchantData the merchant data to send to the backend. The value will be serialized to JSON.
         */
        fun merchantData(merchantData: Any?) = apply { this.merchantData = merchantData }

        /**
         * Sets the key used on the containing [activity's][androidx.fragment.app.FragmentActivity]
         * [ViewModelProvider][androidx.lifecycle.ViewModelProvider]
         * for the [PaymentViewModel]. This is only useful for special scenarios.
         * @param viewModelKey the [androidx.lifecycle.ViewModelProvider] key the PaymentFragment uses to find its [PaymentViewModel] in the containing [activity][androidx.fragment.app.FragmentActivity]
         */
        fun viewModelKey(viewModelKey: String?) = apply { this.viewModelKey = viewModelKey }

        /**
         * Set the enabled default user interfaces.
         *
         * There are three:
         *  - [RETRY_PROMPT], a prompt to retry a failed request that can reasonably be retried
         *  - [SUCCESS_MESSAGE], a laconic success message
         *  - [ERROR_MESSAGE] a less laconic, though a bit technical, error message
         *
         * If a default UI is not enabled, the fragment will be blank instead.
         *
         * The default is to only enable RETRY_PROMPT. This is often useful,
         * as a custom retry prompt is likely unnecessary, but the success and error states
         * should cause the fragment to be dismissed.
         *
         * To disable everything, pass an empty argument list here (a value of 0 also works).
         * If it is more convenient for you, you may also OR the flags manually and call this
         * method with the result value.
         *
         * @param defaultUI the default UI to enable
         */
        fun setEnabledDefaultUI(@DefaultUI vararg defaultUI: Int) = apply {
            enabledDefaultUI = defaultUI.fold(0, Int::or)
        }

        /**
         * Adds the values in this ArgumentsBuilder to a [Bundle].
         * @param bundle the [Bundle] to populate
         * @return the bundle
         */
        fun build(bundle: Bundle) = bundle.apply {
            val consumer = consumer
            val serializedMerchantData = GsonBuilder()
                .serializeNulls()
                .create()
                .toJson(merchantData)

            putInt(ARG_ID_MODE, consumer.getIdMode())
            consumer.getIdData()?.let { putString(ARG_ID_DATA, it) }
            putString(ARG_MERCHANT_DATA, serializedMerchantData)
            viewModelKey?.let { putString(ARG_VM_KEY, it) }
            putInt(ARG_DEFAULT_UI, enabledDefaultUI)
        }

        /**
         * Convenience for `build(Bundle())`.
         * @return a new [Bundle] with the configuration from this ArgumentsBuilder
         */
        fun build() = build(Bundle(5))
    }

    companion object {
        /**
         * The [Configuration] to use if [getConfiguration] is not overridden.
         *
         * See note on [getConfiguration] if you change the value of this property
         * dynamically.
         */
        var defaultConfiguration: Configuration? = null

        /**
         * Default UI flag: a prompt to retry a failed request that can reasonably be retried
         * See [ArgumentsBuilder.setEnabledDefaultUI]
         */
        const val RETRY_PROMPT = 1 shl 0
        /**
         * Default UI flag: a laconic success message
         * See [ArgumentsBuilder.setEnabledDefaultUI]
         */
        const val SUCCESS_MESSAGE = 1 shl 1
        /**
         * Default UI flag: a less laconic, though a bit technical, error message
         * See [ArgumentsBuilder.setEnabledDefaultUI]
         */
        const val ERROR_MESSAGE = 1 shl 2

        private const val ARG_ID_MODE = "com.swedbankpay.mobilesdk.ARG_ID_MODE"
        private const val ARG_ID_DATA = "com.swedbankpay.mobilesdk.ARG_ID_DATA"
        private const val ARG_MERCHANT_DATA = "com.swedbankpay.mobilesdk.ARG_MERCHANT_DATA"
        private const val ARG_VM_KEY = "com.swedbankpay.mobilesdk.ARG_VM_KEY"
        private const val ARG_DEFAULT_UI = "com.swedbankpay.mobilesdk.ARG_DEFAULT_UI"

        private const val STATE_VM = "com.swedbankpay.mobilesdk.STATE_VM"

        internal const val ID_MODE_ANONYMOUS = 0
        internal const val ID_MODE_STORED = 1
        internal const val ID_MODE_ONLINE = 2
    }

    /** @hide */
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(RETRY_PROMPT, SUCCESS_MESSAGE, ERROR_MESSAGE, flag = true)
    annotation class DefaultUI

    private val publicVm get() = ViewModelProviders.of(requireActivity()).run {
        val key = requireArguments().getString(ARG_VM_KEY)
        if (key == null) {
            get(PaymentViewModel::class.java)
        } else {
            get(key, PaymentViewModel::class.java)
        }
    }
    private val vm get() = ViewModelProviders.of(this)[InternalPaymentViewModel::class.java]

    /**
     * Provides the [Configuration] for this PaymentFragment.
     *
     * The default implementation returns the value set in [defaultConfiguration],
     * throwing an exception if it is not set. Override this method to choose
     * the [Configuration] dynamically. Note, however, that this method is only called
     * once for each PaymentFragment instance, namely in the [onCreate] method.
     * This means that the Configuration of a given PaymentFragment instance
     * cannot change once set.
     */
    protected open fun getConfiguration(): Configuration {
        return checkNotNull(defaultConfiguration) { "Default Configuration not set" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModels(getConfiguration())
        startOrResumePayment(savedInstanceState)
    }

    private fun setupViewModels(configuration: Configuration) {
        // N.B! The Fragment does not have a view at this point;
        // however, a lifecycle-bound observer will only start
        // observing after the owning lifecycle is STARTED,
        // which happens after onCreateView. Thus we can assume
        // the observer callbacks will only ever be called when we
        // actually have a view. It does mean we cannot cache
        // the subviews here, but that is a minor penalty, as the
        // kotlin-android-extensions synthetic accessors will
        // cache them for us.
        val vm = vm
        val publicVm = publicVm
        vm.configuration = configuration
        vm.publicVm = publicVm
        vm.enabledDefaultUI.value = requireArguments().getInt(ARG_DEFAULT_UI)
        vm.observeLoading()
        vm.observeCurrentPage(configuration.rootLink.href.toString())
        vm.observeMessage()
        vm.observeTermsOfServicePressed()
        publicVm.observeRetryPreviousPressed()
        observeOnReloadPaymentMenu()
    }

    private fun InternalPaymentViewModel.observeLoading() {
        loading.observe(this@PaymentFragment, Observer {
            updateRefreshLayoutState()
        })
    }

    private fun InternalPaymentViewModel.observeCurrentPage(baseUrl: String) {
        currentPage.observe(this@PaymentFragment, Observer {
            swedbankpaysdk_web_view?.apply {
                clearHistory()
                if (it == null) {
                    loadUrl("about:blank")
                } else {
                    loadDataWithBaseURL(baseUrl, it, "text/html", "utf-8", "")
                }
            }
        })
    }

    private fun InternalPaymentViewModel.observeMessage() {
        messageTitle.observe(this@PaymentFragment, Observer {
            swedbankpaysdk_message.visibility = if (it != null) View.VISIBLE else View.INVISIBLE
            swedbankpaysdk_message_title?.text = it
        })

        messageBody.observe(this@PaymentFragment, Observer {
            swedbankpaysdk_message_body?.text = it
        })

        retryActionAvailable.observe(this@PaymentFragment, Observer {
            updateRefreshLayoutState()
        })
    }

    private fun updateRefreshLayoutState() {
        val vm = this.vm
        val swipeLayout = swedbankpaysdk_swipe_refresh_layout
        val loading = vm.loading.value == true
        swipeLayout.isRefreshing = loading
        swipeLayout.isEnabled = loading || vm.retryActionAvailable.value == true
    }

    private fun InternalPaymentViewModel.observeTermsOfServicePressed() {
        termsOfServiceUrl.observe(this@PaymentFragment, Observer { url ->
            if (url != null) {
                context?.let {
                    it.startActivity(
                        Intent(it, ToSActivity::class.java)
                            .putExtra(ToSActivity.EXTRA_URL, url)
                    )
                }
            }
        })
    }

    private fun PaymentViewModel.observeRetryPreviousPressed() {
        onRetryPreviousAction.observe(this@PaymentFragment, Observer {
            if (it != null) {
                vm.retryFromRetryableError()
            }
        })
    }

    private fun observeOnReloadPaymentMenu() {
        CallbackActivity.onReloadPaymentMenu.observe(this, Observer {
            if (it != null) {
                reloadPaymentMenu()
            }
        })
    }

    private fun reloadPaymentMenu() {
        vm.apply {
            getPaymentMenuWebPage()?.let { page ->
                configuration?.rootLink?.href?.toString()?.let { baseUrl ->
                    swedbankpaysdk_web_view?.apply {
                        clearHistory()
                        loadDataWithBaseURL(baseUrl, page, "text/html", "utf-8", "")
                    }
                }
            }
        }
    }

    private fun startOrResumePayment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            vm.resumeFromSavedState(checkNotNull(savedInstanceState.getBundle(STATE_VM)))
        } else {
            requireArguments().apply {
                val merchantData = getString(ARG_MERCHANT_DATA)
                when (getInt(ARG_ID_MODE)) {
                    ID_MODE_ANONYMOUS -> vm.startStoredOrAnonymousCustomer(null, merchantData)
                    ID_MODE_STORED -> vm.startStoredOrAnonymousCustomer(checkNotNull(getString(ARG_ID_DATA)), merchantData)
                    ID_MODE_ONLINE -> vm.startIdentifiedCustomer(checkNotNull(getString(ARG_ID_DATA)), merchantData)
                    else -> throw IllegalStateException()
                }
            }
        }
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.swedbankpaysdk_payment_fragment, container, false).apply {
            swedbankpaysdk_web_view.apply {
                settings.apply {
                    // JavaScript is required for our use case.
                    // The SDK will only use remote content through links
                    // retrieved from the backend. The backend must be careful
                    // not to send compromised links.
                    @SuppressLint("SetJavaScriptEnabled")
                    javaScriptEnabled = true
                }
                addJavascriptInterface(vm.javascriptInterface, getString(R.string.swedbankpaysdk_javascript_interface_name))

                webViewClient = object : WebViewClient() {

                    override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                        try {
                            url?.let(Uri::parse)?.let { openRedirect(view.context, it) }
                        } catch (_: Exception) {}
                        return true
                    }

                    @TargetApi(Build.VERSION_CODES.N)
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest?
                    ): Boolean {
                        request?.url?.let { openRedirect(view.context, it) }
                        return true
                    }

                    private val packagesForSchemes = mapOf(
                        "swish" to "se.bankgirot.swish"
                    )

                    private fun openRedirect(context: Context, url: Uri) {
                        val intent = Intent(Intent.ACTION_VIEW, url)
                        val packageName = packagesForSchemes[url.scheme]
                        packageName?.let(intent::setPackage)
                        val resolveInfo = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
                        val intentToStart = if (resolveInfo != null) {
                            intent
                        } else {
                            packageName?.let(::getPlayStoreIntent)
                        }
                        try {
                            intentToStart?.addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                            )
                            intentToStart?.let(context::startActivity)
                        } catch (_: Exception) {
                            // can happen if Google Play is not installed
                        }
                    }

                    private fun getPlayStoreIntent(packageName: String): Intent {
                        return Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(
                                "https://play.google.com/store/apps/details?id=$packageName")
                            setPackage("com.android.vending")
                        }
                    }

                }
            }

            swedbankpaysdk_swipe_refresh_layout.setOnRefreshListener {
                vm.retryFromRetryableError()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        swedbankpaysdk_web_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        swedbankpaysdk_web_view.onPause()
    }

    override fun onStart() {
        super.onStart()
        reloadPaymentMenu()
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(STATE_VM, Bundle().also(vm::saveState))
    }
}
