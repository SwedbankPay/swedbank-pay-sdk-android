package com.swedbankpay.mobilesdk.test.integration

import android.content.Context
import android.os.SystemClock
import android.view.KeyEvent
import android.webkit.WebView
import android.widget.Button
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.swedbankpay.mobilesdk.*
import com.swedbankpay.mobilesdk.test.integration.util.openTestLogOutputStream
import com.swedbankpay.mobilesdk.test.integration.util.waitAndScrollFullyIntoViewAndAssertExists
import com.swedbankpay.mobilesdk.test.integration.util.waitAndScrollIntoViewAndAssertExists
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.*
import java.io.PrintStream
import java.lang.AssertionError
import java.util.*

/**
 * End-to-end tests for PaymentFragment
 */
class PaymentTest {
    private companion object {

        const val timeout = 30_000L
        // Key input to the web view is laggy, and without a delay
        // between keystrokes, the input may get jumbled.
        const val keyInputDelay = 200L

        const val noScaCardNumber = "4925000000000004"
        const val scaCardNumber = "4761739001010416"
        const val expiryDate = "1230"
        const val noScaCvv = "111"
        const val scaCvv = "268"

        const val logFileName = "log_PaymentTest"
    }

    private val paymentOrder = PaymentOrder(
        currency = Currency.getInstance("SEK"),
        amount = 100L,
        vatAmount = 20L,
        description = "Test Purchase",
        urls = PaymentOrderUrls(
            ApplicationProvider.getApplicationContext<Context>(),
            paymentTestConfiguration.backendUrl
        )
    )
    private val arguments = PaymentFragment.ArgumentsBuilder()
        .paymentOrder(paymentOrder)
        .build()

    private lateinit var scenario: FragmentScenario<PaymentFragment>

    private lateinit var log: PrintStream

    private val device by lazy {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    private val webView: UiScrollable get() {
        // Make sure device is initialized.
        // UiScrollable is still using the legacy API without an explicit UiDevice dependency.
        device

        return UiScrollable(UiSelector().className(WebView::class.java))
    }
    private val cardOption
        get() = webView.getChild(UiSelector().textStartsWith("Card").clickable(true))
    private val cardDetails
        get() = webView.getChild(UiSelector().text("Pay by Card"))
    private val creditCardOption
        get() = cardDetails.getChild(UiSelector().text("Credit").clickable(true))
    private val panInput
        get() = cardDetails.getChild(UiSelector().text("Card number"))
    private val expiryDateInput
        get() = cardDetails.getChild(UiSelector().text("MM/YY"))
    private val cvvInput
        get() = cardDetails.getChild(UiSelector().text("CVV"))
    private val payButton
        get() = cardDetails.getChild(UiSelector().className(Button::class.java).textStartsWith("Pay "))

    private val scaContinueButton
        get() = webView.getChild(UiSelector().className(Button::class.java).text("Continue"))

    private fun UiObject.inputText(text: String) {
        click()
        for (c in text) {
            device.pressKeyCode(KeyEvent.keyCodeFromString("KEYCODE_$c"))
            SystemClock.sleep(keyInputDelay) // this is horrible but you do what you gotta do
        }
    }

    /**
     * Set up test configuration and start scenario for PaymentFragment
     */
    @Before
    fun setup() {
        PaymentFragment.defaultConfiguration = paymentTestConfiguration
        scenario = launchFragmentInContainer(arguments)

        log = scenario.withFragment {
            openTestLogOutputStream(requireContext(), logFileName)
        }
    }

    /**
     * Destroy scenario
     */
    @After
    fun teardown() {
        log.close()

        scenario.moveToState(Lifecycle.State.DESTROYED)
        PaymentFragment.defaultConfiguration = null
    }

    /**
     * Sanity check: Check that a WebView is displayed by the PaymentFragment
     */
    @Test
    fun itShouldDisplayWebView() {
        Assert.assertTrue("WebView not found", webView.waitForExists(timeout))
    }

    private fun fullPaymentTest(
        cardNumber: String,
        cvv: String,
        paymentFlowHandler: () -> Unit
    ) {
        try {
            Assert.assertTrue("WebView not found", webView.waitForExists(timeout))

            webView.waitAndScrollIntoViewAndAssertExists(cardOption, timeout)
            Assert.assertTrue(cardOption.click())

            webView.waitAndScrollIntoViewAndAssertExists(cardDetails, timeout)

            webView.waitAndScrollFullyIntoViewAndAssertExists(creditCardOption, timeout)
            Assert.assertTrue(creditCardOption.click())


            webView.waitAndScrollFullyIntoViewAndAssertExists(panInput, timeout)
            panInput.inputText(cardNumber)

            webView.waitAndScrollFullyIntoViewAndAssertExists(expiryDateInput, timeout)
            expiryDateInput.inputText(expiryDate)

            webView.waitAndScrollFullyIntoViewAndAssertExists(cvvInput, timeout)
            cvvInput.inputText(cvv)

            webView.waitAndScrollFullyIntoViewAndAssertExists(payButton, timeout)
            Assert.assertTrue(payButton.click())

            paymentFlowHandler()

            val result = waitForResult()
            Assert.assertNotNull("PaymentFragment progress timeout", result)
            Assert.assertEquals(PaymentViewModel.State.COMPLETE, result)
        } catch (e: AssertionError) {
            log.println("Payment test failed at ${Date()}")
            e.printStackTrace(log)
            log.println("Window dump:")
            device.dumpWindowHierarchy(log)
            log.println()
            log.println()
            throw e
        }
    }

    private fun waitForResult(): PaymentViewModel.State? {
        val result = CompletableDeferred<PaymentViewModel.State>()
        scenario.onFragment {
            it.requireActivity().paymentViewModel.state.observe(it) { state ->
                if (
                    state != null
                    && state != PaymentViewModel.State.IDLE
                    && state != PaymentViewModel.State.IN_PROGRESS
                ) {
                    result.complete(state)
                }
            }
        }
        return runBlocking {
            withTimeoutOrNull(timeout) {
                result.await()
            }
        }
    }

    /**
     * Check that a card payment that does not invoke 3D-Secure succeeds
     */
    @Test
    fun itShouldSucceedAtPaymentWithoutSca() {
        fullPaymentTest(
            cardNumber = noScaCardNumber,
            cvv = noScaCvv
        ) {}
    }

    /**
     * Check that a card payment that does invoke 3D-Secure succeeds
     */
    @Test
    fun itShouldSucceedAtPaymentWithSca() {
        fullPaymentTest(
            cardNumber = scaCardNumber,
            cvv = scaCvv
        ) {
            webView.waitAndScrollFullyIntoViewAndAssertExists(scaContinueButton, timeout)
            Assert.assertTrue(scaContinueButton.click())
        }
    }
}
