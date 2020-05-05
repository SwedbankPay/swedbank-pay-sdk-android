package com.swedbankpay.mobilesdk.test.viewmodel

import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.PaymentViewModel
import com.swedbankpay.mobilesdk.R
import com.swedbankpay.mobilesdk.internal.InternalPaymentViewModel
import com.swedbankpay.mobilesdk.test.*
import com.swedbankpay.mobilesdk.test.TestConstants.paymentOrder
import com.swedbankpay.mobilesdk.test.mockConsumers
import com.swedbankpay.mobilesdk.test.mockPaymentOrders
import com.swedbankpay.mobilesdk.test.mockTopLevelResources
import org.junit.Before
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
class ViewModelIdentifiedConsumerTest : AbstractViewModelTest(), HasDefaultViewModelProviderFactory {
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var configuration: Configuration

    override fun getDefaultViewModelProviderFactory() = ViewModelProvider.AndroidViewModelFactory(application)

    private val viewModel get() = getViewModel<InternalPaymentViewModel>()
    private val publicViewModel get() = getViewModel<PaymentViewModel>()

    @Before
    fun setup() {
        viewModel.let {
            it.publicVm = publicViewModel
            it.configuration = configuration
        }
    }

    private fun InternalPaymentViewModel.startIdentifiedTestPayment() = start(
        consumer = TestConstants.consumer,
        paymentOrder = paymentOrder,
        useBrowser = false
    )

    @Test
    fun itShouldStartIdle() {
        observing(viewModel.uiState) {
            verify(it, never()).onChanged(anyOrNull())
        }
        observing(viewModel.currentPage) {
            verify(it, never()).onChanged(anyOrNull())
        }
        observing(publicViewModel.state) {
            verify(it, never()).onChanged(anyOrNull())
        }
    }

    @Test
    fun itShouldMoveToLoadingState() {
        viewModel.apply {
            observing(uiState) { internalState ->
                observing(publicViewModel.state) { publicState ->
                    startIdentifiedTestPayment()
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
            onBlocking {
                getTopLevelResources(any())
            } throwKt exception
        }
        viewModel.apply {
            startIdentifiedTestPayment()
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
            onTopLevelResources(
                mockTopLevelResources(
                    mock {
                        onBlocking {
                            post(any(), any(), any())
                        } throwKt exception
                    },
                    null
                )
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()
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
            onTopLevelResources(
                mockTopLevelResources(
                    mock {
                        onBlocking {
                            post(any(), any(), any())
                        } throwKt exception
                    },
                    null
                )
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()
            observing(uiState) { internalState ->
                observing(publicViewModel.state) { publicState ->
                    retryFromRetryableError()

                    internalState.inOrder {
                        verify().onChanged(
                            refEq(
                                InternalPaymentViewModel.UIState.RetryableError(
                                    null,
                                    exception,
                                    R.string.swedbankpaysdk_failed_init_consumer
                                )
                            )
                        )
                        verify().onChanged(InternalPaymentViewModel.UIState.Loading)
                        verify().onChanged(
                            refEq(
                                InternalPaymentViewModel.UIState.RetryableError(
                                    null,
                                    exception,
                                    R.string.swedbankpaysdk_failed_init_consumer
                                )
                            )
                        )
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
            onTopLevelResources(
                mockTopLevelResources(
                    mockConsumers(),
                    null
                )
            )
        }

        viewModel.apply {
            startIdentifiedTestPayment()
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
            observing(publicViewModel.state) {
                verify(it).onChanged(PaymentViewModel.State.IN_PROGRESS)
                verifyNoMoreInteractions(it)
            }
        }

    }

    @Test
    fun itShouldMoveToHtmlContentStateAfterRetry() {
        val exception = IOException()
        configuration.stub {
            onTopLevelResources(
                mockTopLevelResources(
                    mock {
                        onBlocking {
                            post(any(), any(), any())
                        } throwKt exception
                    },
                    null
                )
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()

            this@ViewModelIdentifiedConsumerTest.configuration.stub {
                onTopLevelResources(
                    mockTopLevelResources(
                        mockConsumers(),
                        null
                    )
                )
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
            observing(publicViewModel.state) {
                verify(it).onChanged(PaymentViewModel.State.IN_PROGRESS)
                verifyNoMoreInteractions(it)
            }
        }
    }

    @Test
    fun itShouldShowExpectedHtmlPage() {
        application.stub {
            on {
                getString(R.string.swedbankpaysdk_view_consumer_identification_template,
                    TestConstants.viewConsumerSessionLink
                )
            } doReturn TestConstants.consumerSessionHtmlPage
        }
        configuration.stub {
            onTopLevelResources(
                mockTopLevelResources(
                    mockConsumers(),
                    null
                )
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()
            observing(currentPage) {
                verify(it).onChanged(TestConstants.consumerSessionHtmlPage)
                verifyNoMoreInteractions(it)
            }
        }
    }

    @Test
    fun itShouldMoveToLoadingStateAfterOnConsumerProfileRefAvailable() {
        configuration.stub {
            onTopLevelResources(
                mockTopLevelResources(
                    mockConsumers(),
                    null
                )
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()
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
            onTopLevelResources(
                mockTopLevelResources(
                    mockConsumers(),
                    mockPaymentOrders()
                )
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()
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
            onTopLevelResources(
                mockTopLevelResources(
                    mockConsumers(),
                    mock {
                        onBlocking { post(any(), any(), any()) } throwKt exception
                    })
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()
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
            onTopLevelResources(
                mockTopLevelResources(
                    mockConsumers(),
                    mock {
                        onBlocking { post(any(), any(), any()) } throwKt exception
                    })
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            observing(uiState) { internalState ->
                observing(publicVm!!.state) { publicState ->

                    retryFromRetryableError()

                    internalState.inOrder {
                        verify().onChanged(
                            refEq(
                                InternalPaymentViewModel.UIState.RetryableError(
                                    null,
                                    exception,
                                    R.string.swedbankpaysdk_failed_create_payment
                                )
                            )
                        )
                        verify().onChanged(InternalPaymentViewModel.UIState.Loading)
                        verify().onChanged(
                            refEq(
                                InternalPaymentViewModel.UIState.RetryableError(
                                    null,
                                    exception,
                                    R.string.swedbankpaysdk_failed_create_payment
                                )
                            )
                        )
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
                getString(R.string.swedbankpaysdk_view_paymentorder_template,
                    TestConstants.viewPaymentorderLink
                )
            } doReturn TestConstants.paymentorderHtmlPage
        }
        configuration.stub {
            onTopLevelResources(
                mockTopLevelResources(
                    mockConsumers(),
                    mockPaymentOrders()
                )
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()
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
            onTopLevelResources(
                mockTopLevelResources(
                    mockConsumers(),
                    null
                )
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()
            javascriptInterface.onIdentifyError(TestConstants.consumerSessionError)
            verifyIsInFailureStateWithTerminalFailure(
                TestConstants.consumerSessionErrorOrigin,
                TestConstants.consumerSessionErrorMessageId,
                TestConstants.consumerSessionErrorDetails
            )
        }
    }

    @Test
    fun itShouldMoveToSuccessStateAfterNavigationToCompleteUrl() {
        configuration.stub {
            onTopLevelResources(
                mockTopLevelResources(
                    mockConsumers(),
                    mockPaymentOrders()
                )
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            overrideNavigation(Uri.parse(TestConstants.completeUrl))
            observing(uiState) {
                verify(it).onChanged(InternalPaymentViewModel.UIState.Success)
                verifyNoMoreInteractions(it)
            }
            observing(publicViewModel.state) {
                verify(it).onChanged(PaymentViewModel.State.SUCCESS)
                verifyNoMoreInteractions(it)
            }
        }
    }

    @Test
    fun itShouldNotifyTermsOfServiceUrlObserversAfterOnPaymentToS() {
        configuration.stub {
            onTopLevelResources(
                mockTopLevelResources(
                    mockConsumers(),
                    mockPaymentOrders()
                )
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            observing(termsOfServiceUrl) {
                overrideNavigation(Uri.parse(TestConstants.termsOfServiceUrl))
                verify(it).onChanged(TestConstants.termsOfServiceUrl)
                verify(it).onChanged(null)
                verifyNoMoreInteractions(it)
            }
        }
    }

    @Test
    fun itShouldMoveToFailureStateAfterOnError() {
        configuration.stub {
            onTopLevelResources(
                mockTopLevelResources(
                    mockConsumers(),
                    mockPaymentOrders()
                )
            )
        }
        viewModel.apply {
            startIdentifiedTestPayment()
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            javascriptInterface.onPaymentError(TestConstants.paymentOrderError)
            verifyIsInFailureStateWithTerminalFailure(
                TestConstants.paymentOrderErrorOrigin,
                TestConstants.paymentOrderErrorMessageId,
                TestConstants.paymentOrderErrorDetails
            )
        }
    }
}