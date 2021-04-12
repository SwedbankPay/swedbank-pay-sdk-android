package com.swedbankpay.mobilesdk.test.integration

import android.content.Context
import android.view.KeyEvent
import android.webkit.WebView
import android.widget.Button
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.swedbankpay.mobilesdk.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * End-to-end tests for PaymentFragment
 */
class PaymentTest {
    private companion object {
        const val timeout = 30_000L
        // Key input to the web view is laggy, and without a delay
        // between keystrokes, the input may get jumbled.
        const val keyInputDelay = 50L

        const val noScaCardNumber = "4925000000000004"
        const val scaCardNumber = "4761739001010416"
        const val expiryDate = "1230"
        const val noScaCvv = "111"
        const val scaCvv = "268"
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

    private val device by lazy {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    private val webView
        get() = device.findObject(UiSelector().className(WebView::class.java))
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

    private fun UiObject.waitAndAssertExists() {
        val exists = waitForExists(timeout)
        Assert.assertTrue("Widget not found: $selector", exists)
    }

    private fun UiObject.inputText(text: String) {
        click()
        for (c in text) {
            device.pressKeyCode(KeyEvent.keyCodeFromString("KEYCODE_$c"))
            Thread.sleep(keyInputDelay) // this is horrible but you do what you gotta do
        }
    }

    /**
     * Set up test configuration and start scenario for PaymentFragment
     */
    @Before
    fun setup() {
        PaymentFragment.defaultConfiguration = paymentTestConfiguration
        scenario = launchFragmentInContainer(arguments)
    }

    /**
     * Destroy scenario
     */
    @After
    fun teardown() {
        scenario.moveToState(Lifecycle.State.DESTROYED)
        PaymentFragment.defaultConfiguration = null
    }

    /**
     * Sanity check: Check that a WebView is displayed by the PaymentFragment
     */
    @Test
    fun itShouldDisplayWebView() {
        webView.waitAndAssertExists()
    }

    private fun fullPaymentTest(
        cardNumber: String,
        cvv: String,
        paymentFlowHandler: () -> Unit
    ) {
        webView.waitAndAssertExists()

        cardOption.waitAndAssertExists()
        Assert.assertTrue(cardOption.click())

        cardDetails.waitAndAssertExists()

        creditCardOption.waitAndAssertExists()
        Assert.assertTrue(creditCardOption.click())

        panInput.waitAndAssertExists()
        panInput.inputText(cardNumber)

        expiryDateInput.waitAndAssertExists()
        expiryDateInput.inputText(expiryDate)

        cvvInput.waitAndAssertExists()
        cvvInput.inputText(cvv)

        payButton.waitAndAssertExists()
        Assert.assertTrue(payButton.click())

        paymentFlowHandler()

        val result = waitForResult()
        Assert.assertNotNull("PaymentFragment progress timeout", result)
        Assert.assertEquals(PaymentViewModel.State.COMPLETE, result)
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
            scaContinueButton.waitAndAssertExists()
            Assert.assertTrue(scaContinueButton.click())
        }
    }
}
