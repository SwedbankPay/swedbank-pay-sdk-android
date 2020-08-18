package com.swedbankpay.mobilesdk

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.swedbankpay.mobilesdk.PaymentFragment.ArgumentsBuilder
import com.swedbankpay.mobilesdk.PaymentFragment.Companion.defaultConfiguration
import com.swedbankpay.mobilesdk.internal.InternalPaymentViewModel
import com.swedbankpay.mobilesdk.internal.ToSActivity
import com.swedbankpay.mobilesdk.internal.WebViewFragment

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
 * The arguments must contain a [PaymentOrder] to create.
 * See [ArgumentsBuilder] for further options.
 *
 * You may observe the state of the PaymentFragment via [PaymentViewModel].
 * Access the PaymentViewModel through the containing [activity][androidx.fragment.app.FragmentActivity]:
 *
 *     ViewModelProviders.of(activity).get(PaymentViewModel.class)
 *
 * Optionally, you may specify a custom [ViewModelProvider][androidx.lifecycle.ViewModelProvider]
 * key by [ArgumentsBuilder.viewModelProviderKey],
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
        private var consumer: Consumer? = null
        private var paymentOrder: PaymentOrder? = null
        private var viewModelKey: String? = null
        private var useExternalBrowser = false
        @DefaultUI
        private var enabledDefaultUI = RETRY_PROMPT
        private var debugIntentUris = false

        /**
         * Sets a consumer for this payment.
         * @param consumer the consumer making this payment
         */
        fun consumer(consumer: Consumer?) = apply { this.consumer = consumer }

        /**
         * Sets the payment order to create
         * @param paymentOrder the payment order to create
         */
        fun paymentOrder(paymentOrder: PaymentOrder) = apply { this.paymentOrder = paymentOrder }

        /**
         * Sets the key used on the containing [activity's][androidx.fragment.app.FragmentActivity]
         * [ViewModelProvider][androidx.lifecycle.ViewModelProvider]
         * for the [PaymentViewModel]. This is only useful for special scenarios.
         * @param viewModelKey the [androidx.lifecycle.ViewModelProvider] key the PaymentFragment uses to find its [PaymentViewModel] in the containing [activity][androidx.fragment.app.FragmentActivity]
         */
        fun viewModelProviderKey(viewModelKey: String?) = apply { this.viewModelKey = viewModelKey }

        /**
         * Sets if the payment flow should open an external browser or continue in WebView.
         * @param external set tu true if the external browser should be used instead of the WebView
         */
        fun useBrowser(external: Boolean = false) = apply { this.useExternalBrowser = external }

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
         * Enables or disables verbose error dialogs when Android Intent Uris
         * do not function correctly.
         */
        fun debugIntentUris(debugIntentUris: Boolean) = apply { this.debugIntentUris = debugIntentUris }

        /**
         * Adds the values in this ArgumentsBuilder to a [Bundle].
         * @param bundle the [Bundle] to populate
         * @return the bundle
         */
        fun build(bundle: Bundle) = bundle.apply {
            putParcelable(ARG_CONSUMER, consumer)
            putParcelable(ARG_PAYMENT_ORDER, paymentOrder)
            putBoolean(ARG_USE_BROWSER, useExternalBrowser)
            viewModelKey?.let { putString(ARG_VIEW_MODEL_PROVIDER_KEY, it) }
            putInt(ARG_ENABLED_DEFAULT_UI, enabledDefaultUI)
            putBoolean(ARG_DEBUG_INTENT_URIS, debugIntentUris)
        }

        /**
         * Convenience for `build(Bundle())`.
         * @return a new [Bundle] with the configuration from this ArgumentsBuilder
         */
        fun build() = build(Bundle(4))
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

        const val ARG_CONSUMER = "com.swedbankpay.mobilesdk.ARG_CONSUMER"
        const val ARG_USE_BROWSER = "com.swedbankpay.mobilesdk.ARG_USE_BROWSER"
        const val ARG_PAYMENT_ORDER = "com.swedbankpay.mobilesdk.ARG_PAYMENT_ORDER"
        const val ARG_VIEW_MODEL_PROVIDER_KEY = "com.swedbankpay.mobilesdk.ARG_VIEW_MODEL_PROVIDER_KEY"
        const val ARG_ENABLED_DEFAULT_UI = "com.swedbankpay.mobilesdk.ARG_DEFAULT_UI"
        const val ARG_DEBUG_INTENT_URIS = "com.swedbankpay.mobilesdk.ARG_DEBUG_INTENT_URIS"

        private const val STATE_VM = "com.swedbankpay.mobilesdk.STATE_VM"
    }

    /** @hide */
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(RETRY_PROMPT, SUCCESS_MESSAGE, ERROR_MESSAGE, flag = true)
    annotation class DefaultUI

    private val publicVm get() = ViewModelProvider(requireActivity()).run {
        val key = requireArguments().getString(ARG_VIEW_MODEL_PROVIDER_KEY)
        if (key == null) {
            get(PaymentViewModel::class.java)
        } else {
            get(key, PaymentViewModel::class.java)
        }
    }
    private val vm get() = ViewModelProvider(this)[InternalPaymentViewModel::class.java]

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
        val arguments = requireArguments()
        vm.configuration = configuration
        vm.publicVm = publicVm
        vm.enabledDefaultUI.value = arguments.getInt(ARG_ENABLED_DEFAULT_UI)
        vm.debugIntentUris = arguments.getBoolean(ARG_DEBUG_INTENT_URIS)
        vm.observeLoading()
        vm.observeCurrentPage(configuration.rootLink.href.toString())
        vm.observeMessage()
        vm.observeTermsOfServicePressed()
        publicVm.observeRetryPreviousPressed()
    }

    private fun InternalPaymentViewModel.observeLoading() {
        loading.observe(this@PaymentFragment, Observer {
            updateRefreshLayoutState()
        })
    }

    private fun InternalPaymentViewModel.observeCurrentPage(baseUrl: String) {
        currentPage.observe(this@PaymentFragment, Observer { page ->
            val webFragment =
                childFragmentManager.findFragmentById(R.id.swedbankpaysdk_root_web_view_fragment) as WebViewFragment
            webFragment.apply {
                reloadRequested = if (page == null) {
                    clear()
                    false
                } else {
                    val loaded = load(baseUrl, page)
                    // only clear reload flag if WebViewFragment actually loaded the page
                    // This way we protect ourselves against arbitrary ordering of calls to
                    // this and onStart.
                    reloadRequested && !loaded
                }
            }
        })
    }

    private fun reloadPaymentMenu() {
        vm.apply {
            reloadRequested = false
            getPaymentMenuWebPage()?.let { page ->
                configuration?.rootLink?.href?.toString()?.let { baseUrl ->
                    val webFragment = childFragmentManager.findFragmentById(R.id.swedbankpaysdk_root_web_view_fragment) as WebViewFragment
                    webFragment.load(baseUrl, page)
                }
            }
        }
    }

    private fun InternalPaymentViewModel.observeMessage() {
        messageTitle.observe(this@PaymentFragment, Observer {
            requireView().findViewById<View>(R.id.swedbankpaysdk_message).visibility = if (it != null) View.VISIBLE else View.INVISIBLE
            requireView().findViewById<TextView>(R.id.swedbankpaysdk_message_title).text = it
        })

        messageBody.observe(this@PaymentFragment, Observer {
            requireView().findViewById<TextView>(R.id.swedbankpaysdk_message_title).text = it
        })

        retryActionAvailable.observe(this@PaymentFragment, Observer {
            updateRefreshLayoutState()
        })
    }

    private fun updateRefreshLayoutState() {
        val vm = this.vm
        val swipeLayout = requireView().findViewById<SwipeRefreshLayout>(R.id.swedbankpaysdk_swipe_refresh_layout)
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



    private fun startOrResumePayment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            vm.resumeFromSavedState(checkNotNull(savedInstanceState.getBundle(STATE_VM)))
        } else {
            requireArguments().apply {
                val consumer = getParcelable<Consumer>(ARG_CONSUMER)
                val paymentOrder = checkNotNull(getParcelable<PaymentOrder>(ARG_PAYMENT_ORDER)) {
                    "Fragment $this@PaymentFragment does not have a PaymentFragment.ARG_PAYMENT_ORDER argument."
                }
                val useExternal = getBoolean(ARG_USE_BROWSER)
                vm.start(consumer, paymentOrder, useExternal)
            }
        }
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.swedbankpaysdk_payment_fragment, container, false).apply {
            val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swedbankpaysdk_swipe_refresh_layout)
            swipeRefreshLayout.setOnRefreshListener {
                vm.retryFromRetryableError()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (vm.reloadRequested) {
            reloadPaymentMenu()
        }
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(STATE_VM, Bundle().also(vm::saveState))
    }
}
