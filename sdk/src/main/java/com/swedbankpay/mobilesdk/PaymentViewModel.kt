@file:Suppress("unused")

package com.swedbankpay.mobilesdk

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.swedbankpay.mobilesdk.internal.InternalPaymentViewModel

/**
 * Convenience for `ViewModelProvider(activity).get(PaymentViewModel::class.java)`.
 */
val FragmentActivity.paymentViewModel get() = ViewModelProvider(this)[PaymentViewModel::class.java]

/**
 * <a href="https://developer.android.com/reference/androidx/lifecycle/ViewModel" target="_blank">ViewModel</a>
 * for communicating with a [PaymentFragment].
 *
 * Get a PaymentViewModel from the containing Activity (but see notes at [PaymentFragment])
 *
 *     ViewModelProviders.of(activity).get(PaymentViewModel::class.java)
 */
class PaymentViewModel : AndroidViewModel {
    /**
     * @suppress
     */
    @Suppress("ConvertSecondaryConstructorToPrimary", "unused")
    constructor(app: Application) : super(app)

    /**
     * State of a payment process
     */
    enum class State {
        /**
         * No payment active
         */
        IDLE {
            /** `false` */
            override val isFinal get() = false
        },
        /**
         * Payment is active and is waiting either for user interaction or backend response
         */
        IN_PROGRESS {
            /** `false` */
            override val isFinal get() = false
        },
        /**
         * Payment is complete. You should hide the [PaymentFragment].
         * This status does not signal anything of whether the payment was successful.
         * You need to check the status from your servers.
         */
        COMPLETE {
            /** `true` */
            override val isFinal get() = true
        },
        /**
         * Payment was canceled by the user. You should hide the [PaymentFragment].
         */
        CANCELED {
            /** `true` */
            override val isFinal get() = true
        },
        /**
         * Payment is active, but could not proceed.
         *
         * By default, [PaymentFragment] shows an error message with a Retry button
         * when in this state. See [PaymentFragment.ArgumentsBuilder.setEnabledDefaultUI].
         */
        RETRYABLE_ERROR {
            /** `false` */
            override val isFinal get() = false
        },
        /**
         * Payment has failed. You should hide the [PaymentFragment] and show
         * a failure message.
         */
        FAILURE {
            /** `true` */
            override val isFinal get() = true
        };

        /**
         * `true` if this is a final state for [PaymentFragment], `false` otherwise
         */
        abstract val isFinal: Boolean
    }

    /**
     * Contains the state of the payment process and possible associated data.
     */
    class RichState internal constructor(
        /**
         * The state of the payment process.
         */
        val state: State,
        /**
         * If the current state is [RETRYABLE_ERROR][State.RETRYABLE_ERROR],
         * this property contains an error message describing the situation.
         */
        val retryableErrorMessage: String?,
        /**
         * If the current state is [RETRYABLE_ERROR][State.RETRYABLE_ERROR], or
         * [FAILURE][State.FAILURE] caused by an [Exception],
         * this property contains that exception.
         *
         * The exception is of any type thrown by your [Configuration].
         *
         * When using [MerchantBackendConfiguration], you should be prepared for
         * [java.io.IOException] in general, and
         * [com.swedbankpay.mobilesdk.merchantbackend.RequestProblemException] in particular.
         * If [com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration]
         * throws an [IllegalStateException], it means you are not using it correctly;
         * please refer to the exception message for advice.
         */
        val exception: Exception?,
        /**
         * If the current state is [FAILURE][State.FAILURE], and it was caused by an onError
         * callback from the Chekout API, this property contains an object describing the error.
         */
        val terminalFailure: TerminalFailure?,
        /**
         * If the current state is [IN_PROGRESS][State.IN_PROGRESS], and the payment is in
         * instrument mode, this list contains the valid instruments for the payment.
         * Note that this list is the same as what was returned from your [Configuration].
         */
        val validInstruments: List<String>?,
        /**
         * If the current state is [IN_PROGRESS][State.IN_PROGRESS], and the payment is in
         * instrument mode, the current instrument, else `null`.
         */
        val instrument: String?
    )

    /**
     * Interface you can implement to be notified when the user clicks on
     * the Terms of Service link in the Payment Menu, and optionally override
     * the behaviour.
     */
    fun interface OnTermsOfServiceClickListener {
        /**
         * Called when the user clicks on the Terms of Service link in the Payment Menu.
         *
         * @param paymentFragment the [PaymentFragment] the user is interacting with
         * @param url the Terms of Service url
         * @return `true` if you handled the event yourself and wish to disable the default behaviour, `false` if you want to let the SDK show the ToS web page.
         */
        fun onTermsOfServiceClick(paymentFragment: PaymentFragment, url: String): Boolean
    }

    private val internalVm = MutableLiveData<InternalPaymentViewModel>()

    private val internalState = Transformations.switchMap(internalVm) {
        it?.uiState
    }

    internal val onRetryPreviousAction = MutableLiveData<Unit>()

    internal fun attachInternalViewModel(internalVm: InternalPaymentViewModel) {
        this.internalVm.value = internalVm
    }
    internal fun detachInternalViewModel(internalVm: InternalPaymentViewModel) {
        this.internalVm.apply {
            if (value == internalVm) {
                value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        internalVm.value = null
        onTermsOfServiceClickListener = null
        onTermsOfServiceClickListenerOwner = null
    }

    /**
     * The current state and associated data of the [PaymentFragment] corresponding to this [PaymentViewModel].
     *
     * For convenience, this property will retain the last-known state of a PaymentFragment
     * after it has been removed. When a new PaymentFragment is added to the same Activity,
     * this property will reflect that PaymentFragment from there on. To support multiple
     * PaymentFragments in an Activity, see [PaymentFragment.ArgumentsBuilder.viewModelProviderKey].
     *
     * Due to the semantics of [Transformations], you should be careful if accessing
     * this value using [LiveData.getValue] directly rather than by an [Observer].
     * Specifically, if nothing is observing this property (possibly indirectly, such as through
     * the [state] property), then the value will not be updated, and the state may be permanently
     * lost if the PaymentFragment is removed before adding an observer to this property.
     */
    val richState = Transformations.map(internalState) {
        val state = when (it) {
            null -> State.IDLE
            InternalPaymentViewModel.UIState.Loading -> State.IN_PROGRESS
            is InternalPaymentViewModel.UIState.PlainHtmlContent -> State.IN_PROGRESS
            is InternalPaymentViewModel.UIState.InstrumentModePaymentOrder -> State.IN_PROGRESS
            is InternalPaymentViewModel.UIState.UpdatingPaymentOrder -> State.IN_PROGRESS
            is InternalPaymentViewModel.UIState.InitializationError -> State.FAILURE
            is InternalPaymentViewModel.UIState.RetryableError -> State.RETRYABLE_ERROR
            InternalPaymentViewModel.UIState.Complete -> State.COMPLETE
            InternalPaymentViewModel.UIState.Canceled -> State.CANCELED
            is InternalPaymentViewModel.UIState.Failure -> State.FAILURE
        }
        val retryableErrorMessage = (it as? InternalPaymentViewModel.UIState.RetryableError)
            ?.message
            ?.takeUnless { it == 0 }
            ?.let(getApplication<Application>()::getString)
        val exception = when (it) {
            is InternalPaymentViewModel.UIState.RetryableError -> it.exception
            else -> null
        }

        val terminalFailure = (it as? InternalPaymentViewModel.UIState.Failure)?.terminalFailure

        val validInstruments: List<String>?
        val instrument: String?
        when (it) {
            is InternalPaymentViewModel.UIState.InstrumentModePaymentOrder -> {
                validInstruments = it.instruments
                instrument = validInstruments[it.selectedInstrumentIndex]
            }

            is InternalPaymentViewModel.UIState.UpdatingPaymentOrder -> {
                validInstruments = it.instruments
                instrument = it.previousInstrument
            }

            else -> {
                validInstruments = null
                instrument = null
            }
        }

        RichState(
            state,
            retryableErrorMessage,
            exception,
            terminalFailure,
            validInstruments,
            instrument,
        )
    }

    /**
     * The current state of the [PaymentFragment] corresponding to this [PaymentViewModel].
     *
     * See notes at [richState].
     */
    val state = Transformations.map(richState) { it.state }

    /**
     * If the current state is [IN_PROGRESS][State.IN_PROGRESS], and the payment is in
     * instrument mode, and the instrument failed to update, you can get the error message
     * here.
     */
    val lastUpdateInstrumentErrorMessage: MutableLiveData<String> =
        MediatorLiveData<String>().apply {
            var source: LiveData<String>? = null
            addSource(internalVm) { vm ->
                val newSource = vm?.updatePaymentOrderErrorMessage
                if (newSource != source) {
                    source?.let(::removeSource)
                    source = newSource
                    newSource?.let { addSource(it) { message ->
                        message?.let(::setValue)
                    } }
                }
            }
        }

    internal var onTermsOfServiceClickListener: OnTermsOfServiceClickListener? = null
    private var onTermsOfServiceClickListenerOwner: LifecycleOwner? = null
        set(value) {
            field?.lifecycle?.removeObserver(onTermsOfServiceClickListenerLifecycleObserver)
            field = value
            value?.lifecycle?.addObserver(onTermsOfServiceClickListenerLifecycleObserver)
        }
    private val onTermsOfServiceClickListenerLifecycleObserver =
        LifecycleEventObserver { source, _ ->
            if (source.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                onTermsOfServiceClickListener = null
                onTermsOfServiceClickListenerOwner = null
            }
        }

    /**
     * Set an OnTermsOfServiceClickListener to be notified when the user clicks on the Terms of Service link
     * in the Payment Menu.
     *
     * Optionally, you may provide a [LifecycleOwner] that this listener is bound to.
     * It will then be automatically removed when the LifecycleOwner is destroyed.
     * If you do not provide a LifecycleOwner, be careful not to leak expensive objects here.
     *
     * @param lifecycleOwner: the LifecycleOwner to bind the listener to, or `null` to keep the listener until the next call to this method
     * @param listener the OnTermsOfServiceClickListener to set, or `null` to remove the listener
     */
    fun setOnTermsOfServiceClickListener(
        lifecycleOwner: LifecycleOwner?,
        listener: OnTermsOfServiceClickListener?
    ) {
        onTermsOfServiceClickListener = listener
        onTermsOfServiceClickListenerOwner = when (listener) {
            null -> null
            else -> lifecycleOwner
        }
    }

    /**
     * If the current state is [RETRYABLE_ERROR][State.RETRYABLE_ERROR], attempts the previous
     * action again. This call transitions the state to [IN_PROGRESS][State.IN_PROGRESS].
     */
    fun retryPreviousAction() {
        // PaymentFragment observes onRetryPreviousAction.
        // Setting a new value notifies the observer, but also causes
        // any future observer added to immediately receive a callback.
        // To have retryPreviousAction only cause a single retry attempt,
        // we nullify the value immediately here, and check for null in the
        // observer, ignoring the quiescent null state.
        onRetryPreviousAction.value = Unit
        onRetryPreviousAction.value = null
    }

    /**
     * If the current state is [IN_PROGRESS][State.IN_PROGRESS], and the payment is in
     * instrument mode, sets the instrument.
     */
    fun setInstrument(instrument: String) = internalVm.value?.setInstrument(instrument)
}