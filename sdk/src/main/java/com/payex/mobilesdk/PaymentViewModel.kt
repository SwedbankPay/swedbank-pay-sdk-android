package com.payex.mobilesdk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.payex.mobilesdk.internal.InternalPaymentViewModel

/**
 * <a href="https://developer.android.com/reference/androidx/lifecycle/ViewModel" target="_blank">ViewModel</a>
 * for communicating with a [PaymentFragment].
 *
 * Get a PaymentViewModel from the containing Activity (but see notes at [PaymentFragment])
 *
 *     ViewModelProviders.of(activity).get(PaymentViewModel::class.java)
 */
class PaymentViewModel : ViewModel {
    /**
     * @suppress
     */
    @Suppress("ConvertSecondaryConstructorToPrimary", "unused")
    constructor() : super()

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
         * Payment completed successfully. You should hide the [PaymentFragment] and show
         * a success message.
         */
        SUCCESS {
            /** `true` */
            override val isFinal get() = true
        },
        /**
         * Payment is active, but could not proceed.
         *
         * By default, [PaymentFragment] shows an error message with a Retry button
         * when in this state. See [PaymentFragment.ArgumentsBuilder.useDefaultRetryPrompt].
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


    internal val internalVm = MutableLiveData<InternalPaymentViewModel>()

    internal val _state = MutableLiveData<State>().apply { value = State.IDLE }

    internal val onRetryPreviousAction = MutableLiveData<Unit>()

    /**
     * The current state of the [PaymentFragment] corresponding to this [PaymentViewModel].
     */
    val state: LiveData<State> get() = _state

    /**
     * If the current state is [FAILURE][State.FAILURE], and it was caused by an onError
     * callback from PayEx, this property contains an object describing the error.
     */
    val terminalFailure: LiveData<TerminalFailure> = MutableLiveData()

    /**
     * If the current state is [RETRYABLE_ERROR][State.RETRYABLE_ERROR], attempts the previous
     * action again. This call transitions the state to [IN_PROGRESS][State.IN_PROGRESS].
     */
    fun retryPreviousAction() {
        onRetryPreviousAction.value = Unit
        onRetryPreviousAction.value = null
    }
}