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

/**
 * Tests for PaymentViewModel and InternalPaymentViewModel: Identified payment
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ViewModelIdentifiedConsumerTest : AbstractViewModelTest(), HasDefaultViewModelProviderFactory {
    /**
     * STRICT_STUBS mockito rule for cleaner tests
     */
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var configuration: Configuration

    /**
     * Use AndroidViewModelFactory when creating ViewModels for this test
     */
    override fun getDefaultViewModelProviderFactory() = ViewModelProvider.AndroidViewModelFactory(application)

    private val viewModel get() = getViewModel<InternalPaymentViewModel>()
    private val publicViewModel get() = getViewModel<PaymentViewModel>()

    /**
     * Set up viewmodels
     */
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

    /**
     * Check that a newly created viewmodel is in idle state
     */
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

    /**
     * Check that viewmodel changes to loading state when payment is started
     */
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

    /**
     * Check that viewmodel changes to retryable-error state when top-level resources fail to load
     */
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

    /**
     * Check that viewmodel changes to retryable-error state when consumer-session fails to start
     */
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

    /**
     * Check that viewmodel attempts to start consumer-session again when instructed to retry
     */
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

    /**
     * Check that viewmodel changes to showing-html-content state after consumer-session is started successfully
     */
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

    /**
     * Check that viewmodel changes to showing-html-content state after a successful retry of starting consumer-session
     */
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

    /**
     * Check that the consumer-session html content is correct
     */
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

    /**
     * Check that viewmodel changes to loading state after identification success
     */
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

    /**
     * Check that viewmodel changes to html-content state after paymentorder created succefully
     */
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

    /**
     * Check that viewmodel changes to retryable-error state when paymentorder fails to be created
     */
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

    /**
     * Check that viewmodel attempts to create paymentorder again when instructed to retry
     */
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

    /**
     * Check that the paymentorder html content is correct
     */
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

    /**
     * Check that viewmodel changes to error state after onError callback in the consumer-session html content
     */
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

    /**
     * Check that viewmodel changes to success state after it processes a navigation to completeUrl
     */
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

    /**
     * Check that viewmodel notifies observers of InternalPaymentViewModel.termsOfServiceUrl after it processes a navigation to termsOfServiceUrl
     */
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

    /**
     * Check that viewmodel changes to error state after onError callback in the paymentorder html content
     */
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