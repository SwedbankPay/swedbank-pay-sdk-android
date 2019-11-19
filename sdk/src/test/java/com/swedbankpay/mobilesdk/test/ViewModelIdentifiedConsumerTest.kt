package com.swedbankpay.mobilesdk.test

import android.app.Application
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.PaymentViewModel
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

fun <T> observing(liveData: LiveData<T>, f: (Observer<T>) -> Unit) {
    val observer = mock<Observer<T>>()
    liveData.observeForever(observer)
    f(observer)
    liveData.removeObserver(observer)
}

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class StartIdentifiedConsumerTest {
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
            observing(uiState) {
                val argument = argumentCaptor<InternalPaymentViewModel.UIState.Failure>()
                verify(it).onChanged(argument.capture())
                argument.firstValue.apply {
                    Assert.assertNull(paymentOrderUrl)
                    Assert.assertNotNull(terminalFailure)
                    terminalFailure?.apply {
                        Assert.assertEquals(TestConstants.consumerSessionErrorOrigin, origin)
                        Assert.assertEquals(TestConstants.consumerSessionErrorMessageId, messageId)
                        Assert.assertEquals(TestConstants.consumerSessionErrorDetails, details)
                    }
                }
                verifyNoMoreInteractions(it)
            }
            observing(publicVm!!.richState) {
                val argument = argumentCaptor<PaymentViewModel.RichState>()
                verify(it).onChanged(argument.capture())
                argument.firstValue.apply {
                    Assert.assertEquals(PaymentViewModel.State.FAILURE, state)
                    Assert.assertNull(problem)
                    Assert.assertNotNull(terminalFailure)
                    terminalFailure?.apply {
                        Assert.assertEquals(TestConstants.consumerSessionErrorOrigin, origin)
                        Assert.assertEquals(TestConstants.consumerSessionErrorMessageId, messageId)
                        Assert.assertEquals(TestConstants.consumerSessionErrorDetails, details)
                    }
                    observing(failureReason) {
                        verifyNoMoreInteractions(it)
                    }
                }
                verifyNoMoreInteractions(it)
            }
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
            observing(uiState) {
                val argument = argumentCaptor<InternalPaymentViewModel.UIState.Failure>()
                verify(it).onChanged(argument.capture())
                argument.firstValue.apply {
                    Assert.assertNotNull(paymentOrderUrl)
                    //Assert.assertEquals(HttpUrl.get(TestConstants.paymentOrderUrl), paymentOrderUrl?.href)
                    Assert.assertNull(terminalFailure)
                }
                verifyNoMoreInteractions(it)
            }
            observing(publicVm!!.richState) {
                val argument = argumentCaptor<PaymentViewModel.RichState>()
                verify(it).onChanged(argument.capture())
                argument.firstValue.apply {
                    Assert.assertEquals(PaymentViewModel.State.FAILURE, state)
                    Assert.assertNull(problem)
                    Assert.assertNull(terminalFailure)
                    observing(failureReason) {
                        verify(it).onChanged(TestConstants.paymentOrderFailureReason)
                        verifyNoMoreInteractions(it)
                    }
                }
                verifyNoMoreInteractions(it)
            }
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
            observing(uiState) {
                val argument = argumentCaptor<InternalPaymentViewModel.UIState.Failure>()
                verify(it).onChanged(argument.capture())
                argument.firstValue.apply {
                    Assert.assertNull(paymentOrderUrl)
                    Assert.assertNotNull(terminalFailure)
                    terminalFailure?.apply {
                        Assert.assertEquals(TestConstants.paymentOrderErrorOrigin, origin)
                        Assert.assertEquals(TestConstants.paymentOrderErrorMessageId, messageId)
                        Assert.assertEquals(TestConstants.paymentOrderErrorDetails, details)
                    }
                }
                verifyNoMoreInteractions(it)
            }
        }
    }
}