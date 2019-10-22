package com.payex.mobilesdk

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.payex.mobilesdk.internal.InternalPaymentViewModel
import java.io.IOException

/**
 * Convenience for `ViewModelProviders.of(activity).get(PaymentViewModel::class.java)`.
 */
val FragmentActivity.paymentViewModel get() = ViewModelProviders.of(this)[PaymentViewModel::class.java]

/**
 * <a href="https://developer.android.com/reference/androidx/lifecycle/ViewModel" target="_blank">ViewModel</a>
 * for communicating with a [PaymentFragment].
 *
 * Get a PaymentViewModel from the containing Activity (but see notes at [PaymentFragment])
 *
 *     ViewModelProviders.of(activity).get(PaymentViewModel::class.java)
 */
@Suppress("unused")
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
         * If the current state is [RETRYABLE_ERROR][State.RETRYABLE_ERROR], and it was caused
         * by am [IOException], this property contains that exception.
         */
        val ioException: IOException?,
        /**
         * If the current state is [RETRYABLE_ERROR][State.RETRYABLE_ERROR] or [FAILURE][State.FAILURE],
         * and it was caused by a problem response, this property contains an object describing the problem.
         */
        val problem: Problem?,
        /**
         * If the current state is [FAILURE][State.FAILURE], and it was caused by an onError
         * callback from PayEx, this property contains an object describing the error.
         */
        val terminalFailure: TerminalFailure?
    )

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
    }

    /**
     * The current state and associated data of the [PaymentFragment] corresponding to this [PaymentViewModel].
     *
     * For convenience, this property will retain the last-known state of a PaymentFragment
     * after it has been removed. When a new PaymentFragment is added to the same Activity,
     * this property will reflect that PaymentFragment from there on. To support multiple
     * PaymentFragments in an Activity, see [PaymentFragment.ArgumentsBuilder.viewModelKey].
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
            is InternalPaymentViewModel.UIState.HtmlContent -> State.IN_PROGRESS
            is InternalPaymentViewModel.UIState.InitializationError -> State.FAILURE
            is InternalPaymentViewModel.UIState.RetryableError -> State.RETRYABLE_ERROR
            InternalPaymentViewModel.UIState.Success -> State.SUCCESS
            is InternalPaymentViewModel.UIState.Failure -> State.FAILURE
        }
        val retryableErrorMessage = (it as? InternalPaymentViewModel.UIState.RetryableError)
            ?.message
            ?.takeUnless { it == 0 }
            ?.let(getApplication<Application>()::getString)
        val ioException = (it as? InternalPaymentViewModel.UIState.RetryableError)?.ioException
        val problem = when (it) {
            is InternalPaymentViewModel.UIState.InitializationError -> it.problem
            is InternalPaymentViewModel.UIState.RetryableError -> it.problem
            else -> null
        }
        val terminalFailure = (it as? InternalPaymentViewModel.UIState.Failure)?.terminalFailure

        RichState(state, retryableErrorMessage, ioException, problem, terminalFailure)
    }

    /**
     * The current state of the [PaymentFragment] corresponding to this [PaymentViewModel].
     *
     * See notes at [richState].
     */
    val state = Transformations.map(richState) { it.state }

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