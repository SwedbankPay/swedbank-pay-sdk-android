package com.swedbankpay.mobilesdk.test

import android.app.Application
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.R
import com.swedbankpay.mobilesdk.internal.InternalPaymentViewModel
import okhttp3.HttpUrl
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
        }
    }

    @Test
    fun itShouldMoveToLoadingState() {
        withViewModel {
            observing(uiState) {
                startIdentifiedCustomer(
                    TestConstants.consumerIdentificationData,
                    TestConstants.merchantData
                )
                verify(it).onChanged(InternalPaymentViewModel.UIState.Loading)
            }
        }
    }

    @Test
    fun itShouldMoveToHtmlContentState() {
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(true, false))
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
            onTopLevelResources(mockTopLevelResources(true, false))
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
            onTopLevelResources(mockTopLevelResources(true, false))
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
            onTopLevelResources(mockTopLevelResources(true, true))
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
            onTopLevelResources(mockTopLevelResources(true, true))
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
            onTopLevelResources(mockTopLevelResources(true, false))
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
        }
    }

    @Test
    fun itShouldMoveToSuccessStateAfterOnPaymentCompleted() {
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(true, true))
        }
        withViewModel {
            startIdentifiedCustomer(TestConstants.consumerIdentificationData, TestConstants.merchantData)
            javascriptInterface.onConsumerProfileRefAvailable(TestConstants.consumerProfileRef)
            javascriptInterface.onPaymentCompleted()
            observing(uiState) {
                verify(it).onChanged(InternalPaymentViewModel.UIState.Success)
                verifyNoMoreInteractions(it)
            }
        }
    }

    @Test
    fun itShouldMoveToFailureStateAfterOnPaymentFailed() {
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(true, true))
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
                    Assert.assertEquals(HttpUrl.get(TestConstants.paymentOrderUrl), paymentOrderUrl?.href)
                    Assert.assertNull(terminalFailure)
                }
                verifyNoMoreInteractions(it)
            }
        }
    }

    @Test
    fun itShouldNotifyTermsOfServiceUrlObserversAfterOnPaymentToS() {
        configuration.stub {
            onTopLevelResources(mockTopLevelResources(true, true))
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
            onTopLevelResources(mockTopLevelResources(true, true))
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

    //val app = ApplicationProvider.getApplicationContext<Application>()



    /*private var mockServer: MockWebServer? = null

    @Before
    fun startServer() {
        val mockServer = MockWebServer()
        this.mockServer = mockServer

        //TopLevelResources.purge()
        mockServer.enqueue(MockResponses.getRoot)
        mockServer.enqueue(MockResponses.postConsumers)

        mockServer.start(0)
        val url = mockServer.url("/").toString()
        //PayEx.configure(Configuration.Builder(url).build())
        Api.skipProviderInstallerForTests()
    }

    @After
    fun shutdownServer() {
        mockServer?.shutdown()
        mockServer = null
    }

    @Test
    fun itShouldShowHtmlPage() = testSuspend {
        /*val vm = InternalPaymentViewModel(app)
        vm.startIdentifiedCustomer("idData", "merchantData")
        vm.waitForNonTransientState()
        var html: String? = null
        val observer = Observer<String?> {
            if (it != null) {
                html = it
            }
        }
        vm.currentPage.observeForever(observer)
        vm.currentPage.removeObserver(observer)

        Assert.assertNotNull(html)*/
    }*/
/*
    private fun testSuspend(timeout: Long = 5_000L, f: suspend CoroutineScope.() -> Unit) {
        val looper = shadowOf(getMainLooper())
        looper.pause()
        var result: Any? = null
        MainScope().launch {
            result = try {
                f()
                Unit
            } catch (t: Throwable) {
                t
            }
        }
        val expirationTime = System.nanoTime() + timeout * 1_000_000L
        looper.runToEndOfTasks()
        while (result == null && System.nanoTime() < expirationTime) {
            Thread.sleep(100L)
            looper.runToEndOfTasks()
        }
        Assert.assertNotNull("Coroutine still active; increasing the timeout may help.", result)
        (result as? Throwable)?.let {
            throw it
        }
    }*/
/*
    @Test
    fun sanityCheck() = testSuspend {
        val child = launch(Dispatchers.IO) {
            Thread.sleep(1000L)
        }
        child.join()
        Assert.assertTrue(child.isCompleted)
    }*/
}