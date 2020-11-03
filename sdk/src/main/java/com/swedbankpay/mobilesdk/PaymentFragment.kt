package com.swedbankpay.mobilesdk

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.swedbankpay.mobilesdk.PaymentFragment.ArgumentsBuilder
import com.swedbankpay.mobilesdk.PaymentFragment.Companion.ARG_VIEW_MODEL_PROVIDER_KEY
import com.swedbankpay.mobilesdk.PaymentFragment.Companion.defaultConfiguration
import com.swedbankpay.mobilesdk.internal.InternalPaymentViewModel
import com.swedbankpay.mobilesdk.internal.ToSActivity
import com.swedbankpay.mobilesdk.internal.WebViewFragment
import java.io.Serializable

/**
 * A [Fragment] that handles a payment process.
 *
 * A PaymentFragment is single-shot: It is configured for a single payment
 * and cannot be reused. You must create a new PaymentFragment for every payment
 * the user makes.
 *
 * You need a [Configuration] object for system-level setup.
 * Usually you only need one [Configuration], which
 * you can set as [defaultConfiguration].
 * For advanced use-cases, override [getConfiguration] instead.
 *
 * You must set the [arguments][setArguments] of a PaymentFragment before use.
 * The argument [Bundle] is easiest to create by [ArgumentsBuilder].
 * Alternatively, you may prepare the Bundle yourself and set appropriate
 * values for the ARG_* keys defined in [PaymentFragment.Companion].
 *
 * You may observe the state of the PaymentFragment via [PaymentViewModel].
 * Access the PaymentViewModel through the containing [activity][androidx.fragment.app.FragmentActivity]:
 *
 *     ViewModelProviders.of(activity).get(PaymentViewModel.class)
 *
 * Optionally, you may specify a custom [ViewModelProvider][androidx.lifecycle.ViewModelProvider]
 * key by [ArgumentsBuilder.viewModelProviderKey] or [ARG_VIEW_MODEL_PROVIDER_KEY],
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
 * The correct functioning of PaymentFragment depends on the argument [Bundle] having
 * the expected values for the keys defined in [PaymentFragment.Companion],
 * which is easiest to ensure by using [ArgumentsBuilder].
 * If your subclass needs custom arguments, you should add the default arguments
 * to your argument [Bundle] by [ArgumentsBuilder.build].
 *
 * If you override [onCreateView], you must call the superclass implementation and add the [View]
 * returned by that method to your layout (or use it as the return value).
 *
 * If you override [onSaveInstanceState], you must call the superclass implementation.
 *
 * All [Bundle] keys used by PaymentFragment are namespaced by a "com.swedbankpay.mobilesdk" prefix,
 * so they should not conflict with any custom keys.
 *
 * @constructor Creates a new instance of PaymentFragment. You must set the [proper arguments][ArgumentsBuilder] before use.
 */
open class PaymentFragment : Fragment() {
    /**
     * Builder class for the argument [Bundle] used by PaymentFragment.
     */
    @Suppress("unused")
    class ArgumentsBuilder {
        private var useCheckin = false
        private var consumer: Consumer? = null
        private var paymentOrder: PaymentOrder? = null
        private var userData: Any? = null
        private var viewModelKey: String? = null
        private var useExternalBrowser = false
        @DefaultUI
        private var enabledDefaultUI = RETRY_PROMPT
        private var debugIntentUris = false

        /**
         * Enables or disables checkin for this payment.
         * Mostly useful for using [userData] and a custom [Configuration].
         * @param useCheckin `true` to use checkin, `false` to skip it
         */
        fun useCheckin(useCheckin: Boolean) = apply { this.useCheckin = useCheckin }

        /**
         * Sets a consumer for this payment.
         * Also enables or disables checkin based on the argument:
         * If `consumer` is `null`, disables checkin;
         * if consumer `consumer` is not `null`, enables checkin.
         * If you wish to override this, call [useCheckin] afterwards.
         * @param consumer the consumer making this payment
         */
        fun consumer(consumer: Consumer?) = apply {
            this.consumer = consumer
            useCheckin = consumer != null
        }

        /**
         * Sets the payment order to create
         * @param paymentOrder the payment order to create
         */
        fun paymentOrder(paymentOrder: PaymentOrder?) = apply { this.paymentOrder = paymentOrder }

        /**
         * Sets custom data for the payment.
         *
         * [com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration]
         * does not use this parameter.
         * If you create a custom [Configuration], you may set any [android.os.Parcelable]
         * or [Serializable] object here, and receive it in your [Configuration] callbacks.
         * Note that due to possible saving and restoring of the argument bundle, you should
         * not rely on receiving the same object as you set here, but an equal one.
         *
         * Note that passing general Serializable objects is not recommended.
         * [String] has special treatment and is okay to pass here.
         *
         * @param userData data for your [Configuration]
         */
        fun userData(userData: Any?) = apply {
            require(userData == null || userData is Parcelable || userData is Serializable) {
                "userData must be Parcelable or Serializable"
            }
            this.userData = userData
        }

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
         * There are four:
         *  - [RETRY_PROMPT], a prompt to retry a failed request that can reasonably be retried
         *  - [COMPLETE_MESSAGE], a laconic completion message
         *  - [ERROR_MESSAGE] a less laconic, though a bit technical, error message
         *
         * If a default UI is not enabled, the fragment will be blank instead.
         *
         * The default is to only enable RETRY_PROMPT and UPDATE_PAYMENTORDER_ERROR_DIALOG.
         * This is often useful, as a custom retry prompt is likely unnecessary,
         * and failures in updating a payment order (i.e. setting the instrument)
         * are unlikely enough not to warrant customized UI, but the success and error states
         * should cause the fragment to be dismissed.
         *
         * To disable everything, pass an empty argument list here (a value of 0 also works).
         * If it is more convenient for you, you may also OR the flags manually and call this
         * method with the result value.
         *
         * @param defaultUI the default UI to enable
         */
        @SuppressLint("WrongConstant")
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
            putBoolean(ARG_USE_CHECKIN, useCheckin)
            putParcelable(ARG_CONSUMER, consumer)
            putParcelable(ARG_PAYMENT_ORDER, paymentOrder)
            userData?.let {
                when (it) {
                    is Parcelable -> putParcelable(ARG_USER_DATA, it)
                    is String -> putString(ARG_USER_DATA, it)
                    is Serializable -> putSerializable(ARG_USER_DATA, it)
                }
            }
            putBoolean(ARG_USE_BROWSER, useExternalBrowser)
            viewModelKey?.let { putString(ARG_VIEW_MODEL_PROVIDER_KEY, it) }
            putInt(ARG_ENABLED_DEFAULT_UI, enabledDefaultUI)
            putBoolean(ARG_DEBUG_INTENT_URIS, debugIntentUris)
        }

        /**
         * Convenience for `build(Bundle())`.
         * @return a new [Bundle] with the configuration from this ArgumentsBuilder
         */
        fun build() = build(Bundle(8))
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
         * Default UI flag: a laconic completion message
         * See [ArgumentsBuilder.setEnabledDefaultUI]
         */
        @SuppressLint("ShiftFlags") // something wrong with lint + kotlin (!?)
        const val COMPLETE_MESSAGE = 1 shl 1
        /**
         * Default UI flag: a less laconic, though a bit technical, error message
         * See [ArgumentsBuilder.setEnabledDefaultUI]
         */
        @SuppressLint("ShiftFlags")
        const val ERROR_MESSAGE = 1 shl 2

        /**
         * Argument key: Any data that you may need in your [Configuration] to prepare the checkin
         * and payment menu for this payment. You will receive this value as the `userData` argument
         * in your [Configuration.postConsumers] and [Configuration.postPaymentorders] methods.
         *
         * Value must be [android.os.Parcelable] or [Serializable].
         */
        const val ARG_USER_DATA = "com.swedbankpay.mobilesdk.USER_DATA"

        /**
         * Argument key: `true` to do checkin before payment menu to get a consumerProfileRef
         */
        const val ARG_USE_CHECKIN = "com.swedbankpay.mobilesdk.ARG_USE_CHECKIN"

        /**
         * Argument key: a [Consumer] object to prepare checkin.
         * You will receive this value in your [Configuration.postConsumers].
         */
        const val ARG_CONSUMER = "com.swedbankpay.mobilesdk.ARG_CONSUMER"

        /**
         * Argument key: a [PaymentOrder] object to prepare the payment menu.
         * You will receive this value in your [Configuration.postPaymentorders].
         */
        const val ARG_PAYMENT_ORDER = "com.swedbankpay.mobilesdk.ARG_PAYMENT_ORDER"

        /**
         * Argument key: the `key` to use in [ViewModelProvider.get] to retrieve the
         * [PaymentViewModel] of this PaymentFragment. Use this if you have multiple
         * PaymentFragments in the same Activity (not recommended).
         */
        const val ARG_VIEW_MODEL_PROVIDER_KEY = "com.swedbankpay.mobilesdk.ARG_VIEW_MODEL_PROVIDER_KEY"

        /**
         * Argument key: the enabled deafult UI.
         * A bitwise combination of [RETRY_PROMPT], [COMPLETE_MESSAGE], and/or [ERROR_MESSAGE]
         */
        const val ARG_ENABLED_DEFAULT_UI = "com.swedbankpay.mobilesdk.ARG_DEFAULT_UI"

        /**
         * Argument key: if `true`, any navigation to out of the payment menu will be done
         * in the web browser app instead. This can be useful for debugging, but is not
         * recommended for general use.
         */
        const val ARG_USE_BROWSER = "com.swedbankpay.mobilesdk.ARG_USE_BROWSER"

        /**
         * Argument key: if `true`, will add debugging information to the error dialog when
         * a web site attempts to start an Activity but fails.
         */
        const val ARG_DEBUG_INTENT_URIS = "com.swedbankpay.mobilesdk.ARG_DEBUG_INTENT_URIS"

        private const val STATE_VM = "com.swedbankpay.mobilesdk.STATE_VM"
    }

    /** @hide */
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        RETRY_PROMPT,
        COMPLETE_MESSAGE,
        ERROR_MESSAGE,
        flag = true
    )
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
        vm.observeCurrentPage()
        vm.observeMessage()
        vm.observeTermsOfServicePressed()
        publicVm.observeRetryPreviousPressed()
    }

    private fun InternalPaymentViewModel.observeLoading() {
        loading.observe(this@PaymentFragment, {
            updateRefreshLayoutState()
        })
    }

    private fun InternalPaymentViewModel.observeCurrentPage() {
        currentHtmlContent.observe(this@PaymentFragment, {
            val webFragment =
                childFragmentManager.findFragmentById(R.id.swedbankpaysdk_root_web_view_fragment) as WebViewFragment
            webFragment.apply {
                reloadRequested = if (it == null) {
                    clear()
                    webViewShowingRootPage.value = false
                    false
                } else {
                    val loaded = load(it.baseUrl, it.getWebViewPage(requireContext()))
                    if (loaded) {
                        webViewShowingRootPage.value = true
                    } // else don't touch it. This will keep the correct state in either case.

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
            getPaymentMenuHtmlContent()?.let {
                val webFragment = childFragmentManager.findFragmentById(R.id.swedbankpaysdk_root_web_view_fragment) as WebViewFragment
                webFragment.load(it.baseUrl, it.getWebViewPage(requireContext()))
                webViewShowingRootPage.value = true
            }
        }
    }

    private fun InternalPaymentViewModel.observeMessage() {
        messageTitle.observe(this@PaymentFragment, {
            requireView().findViewById<View>(R.id.swedbankpaysdk_message).visibility = if (it != null) View.VISIBLE else View.INVISIBLE
            requireView().findViewById<TextView>(R.id.swedbankpaysdk_message_title).text = it
        })

        messageBody.observe(this@PaymentFragment, {
            requireView().findViewById<TextView>(R.id.swedbankpaysdk_message_title).text = it
        })

        retryActionAvailable.observe(this@PaymentFragment, {
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
        termsOfServiceUrl.observe(this@PaymentFragment, { url ->
            url?.let(::onTermsOfServiceClick)
        })
    }

    private fun PaymentViewModel.observeRetryPreviousPressed() {
        onRetryPreviousAction.observe(this@PaymentFragment, {
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
                val useCheckin = getBoolean(ARG_USE_CHECKIN)
                val consumer = getParcelable<Consumer>(ARG_CONSUMER)
                val paymentOrder = getParcelable<PaymentOrder>(ARG_PAYMENT_ORDER)
                val userData = get(ARG_USER_DATA)
                val useExternal = getBoolean(ARG_USE_BROWSER)
                vm.start(useCheckin, consumer, paymentOrder, userData, useExternal)
            }
        }
    }

    private fun onTermsOfServiceClick(url: String) {
        val handled = publicVm.onTermsOfServiceClickListener
            ?.onTermsOfServiceClick(this, url) == true

        if (!handled) {
            context?.let {
                it.startActivity(
                    Intent(it, ToSActivity::class.java)
                        .putExtra(ToSActivity.EXTRA_URL, url)
                )
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

            childFragmentManager.apply {
                if (findFragmentById(R.id.swedbankpaysdk_root_web_view_fragment) == null) {
                    beginTransaction()
                        .add(R.id.swedbankpaysdk_root_web_view_fragment, WebViewFragment())
                        .commit()
                }
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
