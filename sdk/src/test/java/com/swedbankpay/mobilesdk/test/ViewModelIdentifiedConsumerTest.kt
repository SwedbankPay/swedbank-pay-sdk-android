package com.swedbankpay.mobilesdk.test

import android.app.Application
import android.os.Build
import androidx.annotation.StringRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.PaymentViewModel
import com.swedbankpay.mobilesdk.Problem
import com.swedbankpay.mobilesdk.R
import com.swedbankpay.mobilesdk.internal.InternalPaymentViewModel
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import org.robolectric.annotation.Config
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ViewModelIdentifiedConsumerTest {
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var configuration: Configuration

    private fun withViewModel(f: InternalPaymentViewModel.() -> Unit) {
        val vm = InternalPaymentViewModel(application)
        vm.publicVm = PaymentViewModel(application)
        vm.configuration = configuration
        vm.f()
    }

    @Test
    fun itShouldStartIdle() {
        withViewModel {
            observing(uiState) {
                verify(it, never()).onChanged(anyOrNull())
            }
            observing(currentPage) {
                verify(it, never()).onChanged(anyOrNull())
            }
            observing(publicVm!!.state) {
                verify(it, never()).onChanged(anyOrNull())
            }
        }
    }

    @Test
    fun itShouldMoveToLoadingState() {
        withViewModel {
            observing(uiState) { internalState ->
                observing(publicVm!!.state) { publicState ->
                    startIdentifiedCustomer(
                        TestConstants.consumerIdentificationData,
                        TestConstants.merchantData
                    )
                    verify(internalState).onChanged(InternalPaymentViewModel.UIState.Loading)
                    verify(publicState).onChanged(PaymentViewModel.State.IN_PROGRESS)
                }
            }

        }
    }

    @Test
    fun itShouldMoveToRetryableErrorStateAfterTopLevelResourcesFailure() {
        val exception = IOException()
        application.stub {
            on {
                getString(R.string.swedbankpaysdk_failed_init_consumer)
            } doReturn TestConstants.consumerRetryableErrorMessage
        }
        configuration.stub {
            onBlocking { getTopLevelResources(any()) } doThrow exception
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            verifyIsInRetryableErrorState(
                null,
                exception,
                R.string.swedbankpaysdk_failed_init_consumer,
                TestConstants.consumerRetryableErrorMessage
            )
        }
    }

    @Test
    fun itShouldMoveToRetryableErrorStateAfterConsumersFailure() {
        val exception = IOException()
        application.stub {
            on {
                getString(R.string.swedbankpaysdk_failed_init_consumer)
            } doReturn TestConstants.consumerRetryableErrorMessage
        }
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mock {
                onBlocking { post(any(), any(), any()) } doThrow exception
            }, null))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            verifyIsInRetryableErrorState(
                null,
                exception,
                R.string.swedbankpaysdk_failed_init_consumer,
                TestConstants.consumerRetryableErrorMessage
            )
        }
    }

    @Test
    fun itShouldRetryConsumersAfterRetryFromRetryableError() {
        val exception = IOException()
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mock {
                onBlocking { post(any(), any(), any()) } doThrow exception
            }, null))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            observing(uiState) { internalState ->
                observing(publicVm!!.state) { publicState ->
                    retryFromRetryableError()

                    internalState.inOrder {
                        verify().onChanged(refEq(InternalPaymentViewModel.UIState.RetryableError(
                            null,
                            exception,
                            R.string.swedbankpaysdk_failed_init_consumer
                        )))
                        verify().onChanged(InternalPaymentViewModel.UIState.Loading)
                        verify().onChanged(refEq(InternalPaymentViewModel.UIState.RetryableError(
                            null,
                            exception,
                            R.string.swedbankpaysdk_failed_init_consumer
                        )))
                    }
                    verifyNoMoreInteractions(internalState)

                    publicState.inOrder {
                        verify().onChanged(PaymentViewModel.State.RETRYABLE_ERROR)
                        verify().onChanged(PaymentViewModel.State.IN_PROGRESS)
                        verify().onChanged(PaymentViewModel.State.RETRYABLE_ERROR)
                    }
                    verifyNoMoreInteractions(publicState)
                }
            }
        }
    }

    @Test
    fun itShouldMoveToHtmlContentState() {
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mockConsumers(), null))
        }

        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            observing(uiState) {
                verify(it).onChanged(
                    refEq(
                        InternalPaymentViewModel.UIState.HtmlContent(
                            R.string.swedbankpaysdk_view_consumer_identification_template,
                            TestConstants.viewConsumerSessionLink
                        )
                    )
                )
                verifyNoMoreInteractions(it)
            }
            observing(publicVm!!.state) {
                verify(it).onChanged(PaymentViewModel.State.IN_PROGRESS)
                verifyNoMoreInteractions(it)
            }
        }

    }

    @Test
    fun itShouldMoveToHtmlContentStateAfterRetry() {
        val exception = IOException()
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mock {
                onBlocking { post(any(), any(), any()) } doThrow exception
            }, null))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)

            this@ViewModelIdentifiedConsumerTest.configuration.stub {
                onTopLevelResources(mockTopLevelResources(mockConsumers(), null))
            }

            retryFromRetryableError()
            observing(uiState) {
                verify(it).onChanged(
                    refEq(
                        InternalPaymentViewModel.UIState.HtmlContent(
                            R.string.swedbankpaysdk_view_consumer_identification_template,
                            TestConstants.viewConsumerSessionLink
                        )
                    )
                )
                verifyNoMoreInteractions(it)
            }
            observing(publicVm!!.state) {
                verify(it).onChanged(PaymentViewModel.State.IN_PROGRESS)
                verifyNoMoreInteractions(it)
            }
        }
    }

    @Test
    fun itShouldShowExpectedHtmlPage() {
        application.stub {
            on {
                getString(R.string.swedbankpaysdk_view_consumer_identification_template, TestConstants.viewConsumerSessionLink)
            } doReturn TestConstants.consumerSessionHtmlPage
        }
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mockConsumers(), null))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            observing(currentPage) {
                verify(it).onChanged(TestConstants.consumerSessionHtmlPage)
                verifyNoMoreInteractions(it)
            }
        }
    }

    @Test
    fun itShouldMoveToLoadingStateAfterOnConsumerProfileRefAvailable() {
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mockConsumers(), null))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            observing(uiState) {
                verify(it).onChanged(isA<InternalPaymentViewModel.UIState.HtmlContent>())
                verifyNoMoreInteractions(it)
                javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
                verify(it).onChanged(InternalPaymentViewModel.UIState.Loading)
            }
        }
    }

    @Test
    fun itShouldMoveToHtmlContentStateAfterOnConsumerProfileRefAvailable() {
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mockConsumers(), mockPaymentOrders()))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            observing(uiState) {
                verify(it).onChanged(
                    refEq(
                        InternalPaymentViewModel.UIState.HtmlContent(
                            R.string.swedbankpaysdk_view_paymentorder_template,
                            TestConstants.viewPaymentorderLink
                        )
                    )
                )
                verifyNoMoreInteractions(it)
            }
        }
    }

    @Test
    fun itShouldMoveToRetryableErrorStateAfterPaymentOrdersFailure() {
        val exception = IOException()
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mockConsumers(), mock {
                onBlocking { post(any(), any(), anyOrNull(), anyOrNull()) } doThrow exception
            }))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            observing(uiState) {
                verifyIsInRetryableErrorState(
                    null,
                    exception,
                    R.string.swedbankpaysdk_failed_create_payment
                )
            }
        }
    }

    @Test
    fun itShouldRetryPaymentOrdersAfterRetryFromRetryableError() {
        val exception = IOException()
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mockConsumers(), mock {
                onBlocking { post(any(), any(), anyOrNull(), anyOrNull()) } doThrow exception
            }))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            observing(uiState) { internalState ->
                observing(publicVm!!.state) { publicState ->

                    retryFromRetryableError()

                    internalState.inOrder {
                        verify().onChanged(refEq(InternalPaymentViewModel.UIState.RetryableError(
                            null,
                            exception,
                            R.string.swedbankpaysdk_failed_create_payment
                        )))
                        verify().onChanged(InternalPaymentViewModel.UIState.Loading)
                        verify().onChanged(refEq(InternalPaymentViewModel.UIState.RetryableError(
                            null,
                            exception,
                            R.string.swedbankpaysdk_failed_create_payment
                        )))
                    }
                    verifyNoMoreInteractions(internalState)

                    publicState.inOrder {
                        verify().onChanged(PaymentViewModel.State.RETRYABLE_ERROR)
                        verify().onChanged(PaymentViewModel.State.IN_PROGRESS)
                        verify().onChanged(PaymentViewModel.State.RETRYABLE_ERROR)
                    }
                    verifyNoMoreInteractions(publicState)
                }
            }
        }
    }

    @Test
    fun itShouldShowExpectedHtmlPageAfterOnConsumerProfileRefAvailable() {
        application.stub {
            on {
                getString(R.string.swedbankpaysdk_view_paymentorder_template, TestConstants.viewPaymentorderLink)
            } doReturn TestConstants.paymentorderHtmlPage
        }
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mockConsumers(), mockPaymentOrders()))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            observing(currentPage) {
                verify(it).onChanged(TestConstants.paymentorderHtmlPage)
                verifyNoMoreInteractions(it)
            }
        }
    }

    @Test
    fun itShouldMoveToFailureStateAfterOnIdentifyError() {
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mockConsumers(), null))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            javascriptInterface.onIdentifyError(TestConstants.consumerSessionError)
            verifyIsInFailureStateWithTerminalFailure(
                TestConstants.consumerSessionErrorOrigin,
                TestConstants.consumerSessionErrorMessageId,
                TestConstants.consumerSessionErrorDetails
            )
        }
    }

    @Test
    fun itShouldMoveToSuccessStateAfterOnPaymentCompleted() {
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mockConsumers(), mockPaymentOrders()))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            javascriptInterface.onPaymentCompleted()
            observing(uiState) {
                verify(it).onChanged(InternalPaymentViewModel.UIState.Success)
                verifyNoMoreInteractions(it)
            }
            observing(publicVm!!.state) {
                verify(it).onChanged(PaymentViewModel.State.SUCCESS)
                verifyNoMoreInteractions(it)
            }
        }
    }

    @Test
    fun itShouldMoveToFailureStateAfterOnPaymentFailed() {
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mockConsumers(), mockPaymentOrders(TestConstants.paymentOrderFailureReason)))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            javascriptInterface.onPaymentFailed()
            verifyIsInFailureStateWithFailureReason(
                TestConstants.paymentOrderFailureReason
            )
        }
    }

    @Test
    fun itShouldNotifyTermsOfServiceUrlObserversAfterOnPaymentToS() {
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mockConsumers(), mockPaymentOrders()))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            observing(termsOfServiceUrl) {
                javascriptInterface.onPaymentToS(TestConstants.onPaymentTosEvent)
                verify(it).onChanged(TestConstants.paymentToSUrl)
                verify(it).onChanged(null)
                verifyNoMoreInteractions(it)
            }
        }
    }

    @Test
    fun itShouldMoveToFailureStateAfterOnError() {
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(mockConsumers(), mockPaymentOrders()))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            javascriptInterface.onPaymentError(TestConstants.paymentOrderError)
            verifyIsInFailureStateWithTerminalFailure(
                TestConstants.paymentOrderErrorOrigin,
                TestConstants.paymentOrderErrorMessageId,
                TestConstants.paymentOrderErrorDetails
            )
        }
    }

    private fun InternalPaymentViewModel.verifyIsInRetryableErrorState(
        problem: Problem?,
        ioException: IOException?,
        @StringRes messageId: Int,
        message: String? = null
    ) {
        observing(uiState) {
            verify(it).onChanged(refEq(InternalPaymentViewModel.UIState.RetryableError(
                null,
                ioException,
                messageId
            )))
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
                Assert.assertEquals(ioException, this.ioException)
                Assert.assertEquals(problem, this.problem)
                Assert.assertNull(terminalFailure)
            }
            verifyNoMoreInteractions(it)
        }
    }

    private fun InternalPaymentViewModel.verifyIsInFailureStateWithTerminalFailure(
        origin: String,
        messageId: String,
        details: String
    ) {
        FailureStateVerifier.TerminalFailure(origin, messageId, details).verify(this)
    }

    private fun InternalPaymentViewModel.verifyIsInFailureStateWithFailureReason(
        failureReason: String
    ) {
        FailureStateVerifier.FailureReason(failureReason).verify(this)
    }

    private sealed class FailureStateVerifier() {
        fun verify(viewModel: InternalPaymentViewModel) {
            observing(viewModel.uiState) {
                val argument = argumentCaptor<InternalPaymentViewModel.UIState.Failure>()
                verify(it).onChanged(argument.capture())
                verifyInternal(argument.firstValue)
                verifyNoMoreInteractions(it)
            }
            observing(viewModel.publicVm!!.richState) {
                val argument = argumentCaptor<PaymentViewModel.RichState>()
                verify(it).onChanged(argument.capture())
                argument.firstValue.let {
                    Assert.assertEquals(PaymentViewModel.State.FAILURE, it.state)
                    verifyPublic(it)
                }
                verifyNoMoreInteractions(it)
            }
        }

        abstract fun verifyInternal(failure: InternalPaymentViewModel.UIState.Failure)
        abstract fun verifyPublic(richState: PaymentViewModel.RichState)

        class TerminalFailure(
            val origin: String,
            val messageId: String,
            val details: String
        ) : FailureStateVerifier() {
            override fun verifyInternal(failure: InternalPaymentViewModel.UIState.Failure) {
                Assert.assertNull(failure.paymentOrderUrl)
                Assert.assertNotNull(failure.terminalFailure)
                Assert.assertEquals(origin, failure.terminalFailure?.origin)
                Assert.assertEquals(messageId, failure.terminalFailure?.messageId)
                Assert.assertEquals(details, failure.terminalFailure?.details)
            }
            override fun verifyPublic(richState: PaymentViewModel.RichState) {
                Assert.assertNull(richState.problem)
                Assert.assertNotNull(richState.terminalFailure)
                Assert.assertEquals(origin, richState.terminalFailure?.origin)
                Assert.assertEquals(messageId, richState.terminalFailure?.messageId)
                Assert.assertEquals(details, richState.terminalFailure?.details)
                observing(richState.failureReason) {
                    verifyNoMoreInteractions(it)
                }
                observing(richState.failureReasonIOException) {
                    verifyNoMoreInteractions(it)
                }
                observing(richState.failureReasonProblem) {
                    verifyNoMoreInteractions(it)
                }
            }
        }

        class FailureReason(val failureReason: String) : FailureStateVerifier() {
            override fun verifyInternal(failure: InternalPaymentViewModel.UIState.Failure) {
                Assert.assertNotNull(failure.paymentOrderUrl)
                Assert.assertNull(failure.terminalFailure)
            }

            override fun verifyPublic(richState: PaymentViewModel.RichState) {
                Assert.assertNull(richState.problem)
                Assert.assertNull(richState.terminalFailure)
                observing(richState.failureReason) {
                    verify(it).onChanged(failureReason)
                    verifyNoMoreInteractions(it)
                }
                observing(richState.failureReasonIOException) {
                    verify(it).onChanged(null)
                    verifyNoMoreInteractions(it)
                }
                observing(richState.failureReasonProblem) {
                    verify(it).onChanged(null)
                    verifyNoMoreInteractions(it)
                }
            }
        }
    }
}