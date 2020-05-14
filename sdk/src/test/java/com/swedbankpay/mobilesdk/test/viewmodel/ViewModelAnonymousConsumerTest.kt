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
 * Tests for PaymentViewModel and InternalPaymentViewModel: Anonymous payment
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ViewModelAnonymousConsumerTest : AbstractViewModelTest(), HasDefaultViewModelProviderFactory {
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

    private fun stubConfigurationSuccess() {
        configuration.stub {
            onTopLevelResources(
                mockTopLevelResources(
                    null,
                    mockPaymentOrders()
                )
            )
        }
    }

    private fun stubConfigurationPaymentOrdersFailure(t: Throwable) {
        configuration.stub {
            onTopLevelResources(
                mockTopLevelResources(
                    null,
                    mock {
                        onBlocking { post(any(), any(), any()) } throwKt t
                    })
            )
        }
    }

    private fun InternalPaymentViewModel.startAnonymousTestPayment() = start(
        consumer = null,
        paymentOrder = TestConstants.paymentOrder,
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
     * Check that viewmodel changes to in-progress state when payment is started
     */
    @Test
    fun itShouldMoveToLoadingState() {
        viewModel.apply {
            observing(uiState) { internalState ->
                observing(publicViewModel.state) { publicState ->
                    startAnonymousTestPayment()
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
                getString(R.string.swedbankpaysdk_failed_create_payment)
            } doReturn TestConstants.consumerRetryableErrorMessage
        }

        // Mock failure of top level resources
        configuration.stub {
            onBlocking {
                getTopLevelResources(any())
            } throwKt exception
        }
        viewModel.apply {
            startAnonymousTestPayment()
            verifyIsInRetryableErrorState(
                null,
                exception,
                R.string.swedbankpaysdk_failed_create_payment,
                TestConstants.consumerRetryableErrorMessage
            )
        }
    }

    /**
     * Check that viewmodel changes to showing-html-content state after payment is started and loads successfully
     */
    @Test
    fun itShouldMoveToHtmlContentState() {
        stubConfigurationSuccess()
        viewModel.apply {
            startAnonymousTestPayment()
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
        stubConfigurationPaymentOrdersFailure(exception)
        viewModel.apply {
            startAnonymousTestPayment()
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
        stubConfigurationPaymentOrdersFailure(exception)
        viewModel.apply {
            startAnonymousTestPayment()
            observing(uiState) { internalState ->
                observing(publicViewModel.state) { publicState ->

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
     * Check that the html content shown is correct
     */
    @Test
    fun itShouldShowExpectedHtmlPage() {
        application.stub {
            on {
                getString(R.string.swedbankpaysdk_view_paymentorder_template,
                    TestConstants.viewPaymentorderLink
                )
            } doReturn TestConstants.paymentorderHtmlPage
        }
        stubConfigurationSuccess()
        viewModel.apply {
            startAnonymousTestPayment()
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            observing(currentPage) {
                verify(it).onChanged(TestConstants.paymentorderHtmlPage)
                verifyNoMoreInteractions(it)
            }
        }
    }

    /**
     * Check that viewmodel changes to success state after it processes a navigation to completeUrl
     */
    @Test
    fun itShouldMoveToSuccessStateAfterNavigationToCompleteUrl() {
        stubConfigurationSuccess()
        viewModel.apply {
            startAnonymousTestPayment()
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
        stubConfigurationSuccess()
        viewModel.apply {
            startAnonymousTestPayment()
            observing(termsOfServiceUrl) {
                overrideNavigation(Uri.parse(TestConstants.termsOfServiceUrl))
                verify(it).onChanged(TestConstants.termsOfServiceUrl)
                verify(it).onChanged(null)
                verifyNoMoreInteractions(it)
            }
        }
    }

    /**
     * Check that viewmodel changes to error state after onError callback
     */
    @Test
    fun itShouldMoveToFailureStateAfterOnError() {
        stubConfigurationSuccess()
        viewModel.apply {
            startAnonymousTestPayment()
            javascriptInterface.onPaymentError(TestConstants.paymentOrderError)
            verifyIsInFailureStateWithTerminalFailure(
                TestConstants.paymentOrderErrorOrigin,
                TestConstants.paymentOrderErrorMessageId,
                TestConstants.paymentOrderErrorDetails
            )
        }
    }
}