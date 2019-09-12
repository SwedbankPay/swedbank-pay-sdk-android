package com.payex.mobilesdk

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.GsonBuilder
import com.payex.mobilesdk.PaymentFragment.ArgumentsBuilder
import com.payex.mobilesdk.PaymentFragment.Companion.defaultConfiguration
import com.payex.mobilesdk.internal.InternalPaymentViewModel
import com.payex.mobilesdk.internal.LOG_TAG

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
 * Optionally, you may specify a custom [ViewModelProvider][androidx.lifecycle.ViewModelProvider] key in [ArgumentsBuilder],
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
 * All [Bundle] keys used by PaymentFragment are namespaced by a "com.payex.mobilesdk" prefix,
 * so they should not conflict with any custom keys.
 *
 * @constructor Creates a new, unconfigured instance of PaymentFragment. You must configure it using [setArguments] before use.
 */
open class PaymentFragment : Fragment() {
    /**
     * Builder class for the argument [Bundle] used by PaymentFragment.
     */
    class ArgumentsBuilder {
        private var consumer = Consumer.ANONYMOUS
        private var merchantData: Any? = null
        private var viewModelKey: String? = null
        private var useDefaultRetryPrompt = true

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
         * @see [PaymentViewModel.retryPreviousAction]
         */
        fun viewModelKey(viewModelKey: String?) = apply { this.viewModelKey = viewModelKey }

        /**
         * Enables or disables the default UI for 'retryable error' situations.
         *
         * If this is set to `false`, you should show your own UI when
         * [PaymentViewModel.state] is [PaymentViewModel.State.RETRYABLE_ERROR].
         * The PaymentFragment will be blank in this case.
         *
         * Defaults to `true`.
         *
         * @param useDefaultRetryPrompt `false` to disable the default UI, `true` to enable it.
         */
        fun useDefaultRetryPrompt(useDefaultRetryPrompt: Boolean) = apply {
            this.useDefaultRetryPrompt = useDefaultRetryPrompt
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
            putString(ARG_ID_DATA, consumer.getIdData())
            putString(ARG_MERCHANT_DATA, serializedMerchantData)
            putString(ARG_VM_KEY, viewModelKey)
            putBoolean(ARG_USE_DEFAULT_RETRY, useDefaultRetryPrompt)
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

        private const val ARG_ID_MODE = "com.payex.mobilesdk.ARG_ID_MODE"
        private const val ARG_ID_DATA = "com.payex.mobilesdk.ARG_ID_DATA"
        private const val ARG_MERCHANT_DATA = "com.payex.mobilesdk.ARG_MERCHANT_DATA"
        private const val ARG_VM_KEY = "com.payex.mobilesdk.ARG_VM_KEY"
        private const val ARG_USE_DEFAULT_RETRY = "com.payex.mobilesdk.ARG_USE_DEFAULT_RETRY"

        private const val STATE_VM = "com.payex.mobilesdk.STATE_VM"

        internal const val ID_MODE_ANONYMOUS = 0
        internal const val ID_MODE_STORED = 1
        internal const val ID_MODE_ONLINE = 2
    }

    private val publicVm get() = ViewModelProviders.of(requireActivity()).run {
        val key = checkNotNull(arguments).getString(ARG_VM_KEY)
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

    /**
     * Convenience for `arguments = builder.build()`.
     * @param builder the [ArgumentsBuilder] to apply to this PaymentFrament
     */
    fun setArguments(builder: ArgumentsBuilder) {
        arguments = builder.build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModels(getConfiguration())
        startOrResumePayment(savedInstanceState)
    }

    private fun setupViewModels(configuration: Configuration) {
        val vm = vm
        vm.setConfiguration(configuration)
        val baseUrl = configuration.rootLink.href.toString()
        vm.currentPage.observe(this, Observer {
            if (it != null) {
                (view as? WebView)?.loadDataWithBaseURL(baseUrl, it, "text/html", "utf-8", "")
            }
        })

        publicVm.onRetryPreviousAction.observe(this, Observer {
            if (it != null) {
                this.vm.retryFromRetryableError()
            }
        })
    }

    private fun startOrResumePayment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            vm.resumeFromSavedState(checkNotNull(savedInstanceState.getBundle(STATE_VM)))
        } else {
            val arguments = checkNotNull(arguments) { "setArguments not called" }
            arguments.apply {
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
        return WebView(context).apply {
            settings.apply {
                // JavaScript is required for our use case.
                // The SDK will only use remote content through links
                // retrieved from the backend. The backend must be careful
                // not to send compromised links.
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true

                domStorageEnabled = true
            }
            addJavascriptInterface(vm.javascriptInterface, getString(R.string.javascript_interface_name))
            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d(LOG_TAG, consoleMessage.message())
                    return true
                }
            }
        }
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(STATE_VM, Bundle().also(vm::saveState))
    }
}

