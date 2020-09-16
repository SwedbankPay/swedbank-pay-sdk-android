package com.swedbankpay.mobilesdk.test.viewmodel

import androidx.annotation.StringRes
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.refEq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.swedbankpay.mobilesdk.PaymentViewModel
import com.swedbankpay.mobilesdk.Problem
import com.swedbankpay.mobilesdk.internal.InternalPaymentViewModel
import com.swedbankpay.mobilesdk.test.observing
import org.junit.Assert
import java.io.IOException

internal fun InternalPaymentViewModel.verifyIsInRetryableErrorState(
    problem: Problem?,
    ioException: IOException?,
    @StringRes messageId: Int,
    message: String? = null
) {
    observing(uiState) {
        verify(it).onChanged(
            refEq(
                InternalPaymentViewModel.UIState.RetryableError(
                    null,
                    ioException,
                    messageId
                )
            )
        )
        verifyNoMoreInteractions(it)
    }
    observing(retryActionAvailable) {
        verify(it).onChanged(true)
        verifyNoMoreInteractions(it)
    }
    observing(publicVm!!.richState) {
        val argument = argumentCaptor<PaymentViewModel.RichState>()
        verify(it).onChanged(argument.capture())
        argument.firstValue.apply {
            Assert.assertEquals(PaymentViewModel.State.RETRYABLE_ERROR, state)
            if (message != null) {
                Assert.assertEquals(message, retryableErrorMessage)
            }
            Assert.assertEquals(ioException, this.exception)
            Assert.assertEquals(problem, this.problem)
            Assert.assertNull(terminalFailure)
        }
        verifyNoMoreInteractions(it)
    }
}

internal fun InternalPaymentViewModel.verifyIsInFailureStateWithTerminalFailure(
    origin: String,
    messageId: String,
    details: String
) {
    observing(uiState) {
        val argument = argumentCaptor<InternalPaymentViewModel.UIState.Failure>()
        verify(it).onChanged(argument.capture())
        val failure = argument.firstValue
        Assert.assertNotNull(failure.terminalFailure)
        Assert.assertEquals(origin, failure.terminalFailure?.origin)
        Assert.assertEquals(messageId, failure.terminalFailure?.messageId)
        Assert.assertEquals(details, failure.terminalFailure?.details)

        verifyNoMoreInteractions(it)
    }
    observing(publicVm!!.richState) {
        val argument = argumentCaptor<PaymentViewModel.RichState>()
        verify(it).onChanged(argument.capture())
        val richState = argument.firstValue
        Assert.assertEquals(PaymentViewModel.State.FAILURE, richState.state)
        Assert.assertNull(richState.problem)
        Assert.assertNotNull(richState.terminalFailure)
        Assert.assertEquals(origin, richState.terminalFailure?.origin)
        Assert.assertEquals(messageId, richState.terminalFailure?.messageId)
        Assert.assertEquals(details, richState.terminalFailure?.details)

        verifyNoMoreInteractions(it)
    }
}
