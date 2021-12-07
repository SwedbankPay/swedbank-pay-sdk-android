package com.swedbankpay.mobilesdk.internal

import android.content.Context
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.PaymentFragment
import com.swedbankpay.mobilesdk.R

internal data class PaymentFragmentMessage(
    val title: String,
    val body: String?
) {
    companion object {
        fun fromUIState(
            context: Context,
            configuration: Configuration,
            state: InternalPaymentViewModel.UIState,
            @PaymentFragment.DefaultUI enabledDefaultUI: Int
        ) = context.getPaymentFragmentMessage(configuration, state, enabledDefaultUI)
    }
}

private fun Context.getPaymentFragmentMessage(
    configuration: Configuration,
    state: InternalPaymentViewModel.UIState,
    @PaymentFragment.DefaultUI enabledDefaultUI: Int
): PaymentFragmentMessage? {
    fun isEnabled(@PaymentFragment.DefaultUI defaultUI: Int)
            = enabledDefaultUI and defaultUI != 0

    return when {
        state is InternalPaymentViewModel.UIState.InitializationError
                && isEnabled(PaymentFragment.ERROR_MESSAGE) ->
            PaymentFragmentMessage(
                getString(R.string.swedbankpaysdk_bad_init_request_title),
                configuration.getErrorMessage(this, state.exception)
            )

        state is InternalPaymentViewModel.UIState.RetryableError
                && isEnabled(PaymentFragment.RETRY_PROMPT) ->
            PaymentFragmentMessage(
                getString(R.string.swedbankpaysdk_retryable_error_title),
                sequenceOf(
                    getString(state.message),
                    if (isEnabled(PaymentFragment.RETRY_PROMPT_DETAIL)) {
                        configuration.getErrorMessage(this, state.exception)
                    } else {
                        null
                    },
                    getString(R.string.swedbankpaysdk_pull_to_refresh)
                ).filterNotNull().joinToString("\n\n")
            )

        state is InternalPaymentViewModel.UIState.Complete
                && isEnabled(PaymentFragment.COMPLETE_MESSAGE) ->
            PaymentFragmentMessage(
                getString(R.string.swedbankpaysdk_payment_complete),
                null
            )

        state is InternalPaymentViewModel.UIState.Failure
                && isEnabled(PaymentFragment.ERROR_MESSAGE) -> {
            val swedbankPayError = state.failureReason as? InternalPaymentViewModel.FailureReason.SwedbankPayError
            PaymentFragmentMessage(
                getString(
                    when (swedbankPayError?.terminalFailure) {
                        null -> R.string.swedbankpaysdk_payment_failed
                        else -> R.string.swedbankpaysdk_terminal_failure_title
                    }
                ),
                swedbankPayError?.terminalFailure?.messageId?.let { messageId ->
                    getString(R.string.swedbankpaysdk_terminal_failure_message, messageId)
                }
            )
        }

        else -> null
    }
}

