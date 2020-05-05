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

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ViewModelAnonymousConsumerTest : AbstractViewModelTest(), HasDefaultViewModelProviderFactory {
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
                    startAnonymousTestPayment()
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