package com.payex.mobilesdk

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.payex.mobilesdk.internal.InternalPaymentViewModel
import com.payex.mobilesdk.internal.getValueConsistentMap
import com.payex.mobilesdk.internal.getValueConsistentSwitchMap

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

    private val internalVm = MutableLiveData<InternalPaymentViewModel>()

    private val internalState = internalVm.getValueConsistentSwitchMap {
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
    }

    /**
     * The current state of the [PaymentFragment] corresponding to this [PaymentViewModel].
     */
    val state: LiveData<State> = internalState.getValueConsistentMap {
        when (it) {
            null -> State.IDLE
            InternalPaymentViewModel.UIState.Loading -> State.IN_PROGRESS
            is InternalPaymentViewModel.UIState.HtmlContent -> State.IN_PROGRESS
            is InternalPaymentViewModel.UIState.RetryableError -> State.RETRYABLE_ERROR
            InternalPaymentViewModel.UIState.Success -> State.SUCCESS
            is InternalPaymentViewModel.UIState.Failure -> State.FAILURE
        }
    }

    /**
     * If the current state is [RETRYABLE_ERROR][State.RETRYABLE_ERROR],
     * this property contains an error message describing the situation.
     */
    val retryableErrorMessage: LiveData<String> = internalState.getValueConsistentMap {
        (it as? InternalPaymentViewModel.UIState.RetryableError)
            ?.message
            ?.takeUnless { it == 0 }
            ?.let(getApplication<Application>()::getString)
    }

    /**
     * If the current state is [FAILURE][State.FAILURE], and it was caused by an onError
     * callback from PayEx, this property contains an object describing the error.
     */
    val terminalFailure: LiveData<TerminalFailure> = internalState.getValueConsistentMap {
        (it as? InternalPaymentViewModel.UIState.Failure)?.terminalFailure
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


}