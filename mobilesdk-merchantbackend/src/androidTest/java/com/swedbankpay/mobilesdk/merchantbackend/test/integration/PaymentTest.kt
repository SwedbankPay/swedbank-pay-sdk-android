package com.swedbankpay.mobilesdk.merchantbackend.test.integration

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
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
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.swedbankpay.mobilesdk.*
import com.swedbankpay.mobilesdk.merchantbackend.test.integration.util.*
import com.swedbankpay.mobilesdk.merchantbackend.test.integration.util.clickUntilCheckedAndAssert
import com.swedbankpay.mobilesdk.merchantbackend.test.integration.util.clickUntilFocusedAndAssert
import com.swedbankpay.mobilesdk.merchantbackend.test.integration.util.waitAndScrollFullyIntoViewAndAssertExists
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

/// End-to-end tests for PaymentFragment
class PaymentTest {
    private companion object {
        const val timeout = 30_000L
        const val longTimeout = 120_000L
        // Key input to the web view is laggy, and without a delay
        // between keystrokes, the input may get jumbled.
        const val keyInputDelay = 500L

        const val noScaCardNumber1 = "4581097032723517"
        const val noScaCardNumber2 = "4925000000000004"
        const val noScaCardNumber3 = "5226600159865967"
        const val scaCardNumber1 = "4761739001010416"
        const val scaCardNumber2 = "5226612199533406"
        const val scaCardNumber3 = "4547781087013329"
        const val expiryDate = "1230"
        const val noScaCvv = "111"
        const val scaCvv = "268"
    }
    
    // Set up test configuration and start scenario for PaymentFragment
    @Before
    fun setup() {
        PaymentFragment.defaultConfiguration = paymentTestConfiguration
        paymentOrder = PaymentOrder(
            currency = Currency.getInstance("SEK"),
            amount = 100L,
            vatAmount = 20L,
            description = "Test Purchase",
            urls = PaymentOrderUrls(
                ApplicationProvider.getApplicationContext<Context>(),
                paymentTestConfiguration.backendUrl
            )
        )
    }
    
    // to repeat tests within the same run
    private fun setupAgain() {
        PaymentFragment.defaultConfiguration = paymentTestConfiguration
        scenario
    }

    // Destroy scenario
    @After
    fun teardown() {
        scenario.moveToState(Lifecycle.State.DESTROYED)
        _scenario = null
        PaymentFragment.defaultConfiguration = null
    }

    private var paymentOrder: PaymentOrder = PaymentOrder(
        currency = Currency.getInstance("SEK"),
        amount = 100L,
        vatAmount = 20L,
        description = "Test Purchase",
        urls = PaymentOrderUrls(
            ApplicationProvider.getApplicationContext<Context>(),
            paymentTestConfiguration.backendUrl
        )
    )
    
    private var arguments:Bundle? = null
    private fun buildArguments(isV3: Boolean = true, paymentOrder: PaymentOrder = this.paymentOrder): Bundle {
        arguments = PaymentFragment.ArgumentsBuilder()
            .checkoutV3(isV3)
            .paymentOrder(paymentOrder)
            .build()
        return arguments as Bundle
    }

    private var _scenario: FragmentScenario<PaymentFragment>? = null
    private var scenario: FragmentScenario<PaymentFragment> 
        get() {
            if (_scenario == null) {
                _scenario = launchFragmentInContainer(arguments ?: buildArguments())
            }
            
            return _scenario ?: throw AssertionError("Null when it cannot be")
        }
        set(value) { 
            _scenario = value 
        }
    
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
        get() = webView.getChild(UiSelector().textStartsWith("Card").checkable(true))
    private val cardDetails
        get() = webView.getChild(UiSelector().text("Pay by Card"))
    private val creditCardOption
        get() = cardDetails.getChild(UiSelector().text("Credit").checkable(true))
    private val panInput
        get() = cardDetails.getChild(UiSelector().resourceIdMatches("panInput.*"))
    private val expiryDateInput
        get() = cardDetails.getChild(UiSelector().resourceIdMatches("expiryInput.*"))

    private val cvvInput
        get() = cardDetails.getChild(UiSelector().resourceIdMatches("cvcInput.*"))

    private val payButton
        get() = cardDetails.getChild(UiSelector().className(Button::class.java).textStartsWith("Pay "))

    private val scaContinueButton
        get() = webView.getChild(UiSelector().className(Button::class.java).text("Continue"))
    private val storeCardOption
        get() = webView.getChild(UiSelector().textStartsWith("Store this card").checkable(true))

    private fun UiObject.inputText(text: String) {
        this.text = text
        clickUntilFocusedAndAssert(timeout)
        if (this.text != null && this.text != "") {
            // We have succeeded to input something, but can't verify its correct since JS reformatting.
            return
        }
        for (c in text) {
            device.pressKeyCode(KeyEvent.keyCodeFromString("KEYCODE_$c"))
            SystemClock.sleep(keyInputDelay) // this is horrible but you do what you gotta do
        }
    }

    private fun fullPaymentTestAttempt(
        cardNumber: String,
        cvv: String,
        storeCard: Boolean = false,
        paymentFlowHandler: () -> Unit
    ): Boolean {
        if (!webView.waitForExists(timeout)) {
            return false
        }

        if (!webView.waitAndScrollUntilExists(cardOption, timeout)) { return false }
        cardOption.clickUntilCheckedAndAssert(timeout)
        
        if (storeCard) {
            if (!storeCardOption.waitForExists(timeout)) { return false }
            storeCardOption.clickUntilCheckedAndAssert(timeout)
        }

        if (!webView.waitAndScrollUntilExists(creditCardOption, timeout)) { return false }
        creditCardOption.clickUntilCheckedAndAssert(timeout)

        if (!webView.waitAndScrollUntilExists(panInput, timeout)) { return false }
        panInput.inputText(cardNumber)

        if (!webView.waitAndScrollUntilExists(expiryDateInput, timeout)) { return false }
        expiryDateInput.inputText(expiryDate)

        if (!webView.waitAndScrollUntilExists(cvvInput, timeout)) { return false }
        cvvInput.inputText(cvv)

        if (!webView.waitAndScrollUntilExists(payButton, longTimeout)) { return false }

        if (!payButton.click()) { return false }

        paymentFlowHandler()
        lastResult = waitForResult()
        if (lastResult == null) { return false }
        return true
    }
    private var lastResult: PaymentViewModel.State? = null

    private fun fullPaymentTest(
        cardNumbers: Array<String>,
        cvv: String,
        storeCard: Boolean = false,
        paymentFlowHandler: () -> Unit
    ) {
        scenario
        
        var success = false
        for (cardNumber in cardNumbers) {
            success = fullPaymentTestAttempt(cardNumber, cvv, storeCard, paymentFlowHandler)
            if (success) {
                break
            }
            // it failed try again with another number
            teardown()
            setupAgain()
        }

        if (!success) {

            val cardNumber = cardNumbers[0]
            Assert.assertTrue("WebView not found", webView.waitForExists(timeout))

            webView.waitAndScrollFullyIntoViewAndAssertExists(cardOption, timeout)
            cardOption.clickUntilCheckedAndAssert(timeout)

            webView.waitAndScrollFullyIntoViewAndAssertExists(creditCardOption, timeout)
            creditCardOption.clickUntilCheckedAndAssert(timeout)

            if (storeCard) {
                webView.waitAndScrollFullyIntoViewAndAssertExists(storeCardOption, timeout)
                storeCardOption.clickUntilCheckedAndAssert(timeout)
            }

            webView.waitAndScrollFullyIntoViewAndAssertExists(panInput, timeout)
            panInput.inputText(cardNumber)

            webView.waitAndScrollFullyIntoViewAndAssertExists(expiryDateInput, timeout)
            expiryDateInput.inputText(expiryDate)

            webView.waitAndScrollFullyIntoViewAndAssertExists(cvvInput, timeout)
            cvvInput.inputText(cvv)

            webView.waitAndScrollFullyIntoViewAndAssertExists(payButton, longTimeout)
            Assert.assertTrue(payButton.click())

            paymentFlowHandler()
            lastResult = waitForResult()
        }
        Assert.assertNotNull("PaymentFragment progress timeout", lastResult)
        Assert.assertEquals(PaymentViewModel.State.COMPLETE, lastResult)
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

    /*During building we just comment V2 TODO: UnComment!
    
    
    // Sanity check: Check that a WebView is displayed by the PaymentFragment
    @Test
    fun itShouldDisplayWebViewV3() {
        isV3 = true
        Assert.assertTrue("WebView not found", webView.waitForExists(timeout))

        Assert.assertTrue("Card options not found", cardOption.waitForExists(timeout))
    }
    
    // Sanity check: Check that a WebView is displayed by the PaymentFragment
    @Test
    fun itShouldDisplayWebView() {
        isV3 = false
        Assert.assertTrue("WebView not found", webView.waitForExists(timeout))
        Assert.assertTrue("Card options not found", cardOption.waitForExists(timeout))
    }

    // Check that a card payment that does not invoke 3D-Secure succeeds
    @Test
    fun itShouldSucceedAtPaymentWithoutSca() {
        isV3 = false
        fullPaymentTest(
            cardNumbers = arrayOf(noScaCardNumber1, noScaCardNumber2, noScaCardNumber3),
            cvv = noScaCvv
        ) {}
    }

    //Check that a card payment that does invoke 3D-Secure succeeds
    @Test
    fun itShouldSucceedAtPaymentWithSca() {
        isV3 = false
        fullPaymentTest(
            cardNumbers = arrayOf(scaCardNumber1, scaCardNumber2, scaCardNumber3, scaCardNumber1),
            cvv = scaCvv
        ) {
            webView.waitAndScrollFullyIntoViewAndAssertExists(scaContinueButton, timeout)
            Assert.assertTrue(scaContinueButton.click())
        }
    }
    
    //Check that a card payment that does invoke 3D-Secure succeeds
    @Test
    fun itShouldSucceedAtPaymentWithScaV3() {
        isV3 = true
        fullPaymentTest(
            cardNumbers = arrayOf(scaCardNumber1, scaCardNumber2, scaCardNumber3, scaCardNumber1),
            cvv = scaCvv
        ) {
            webView.waitAndScrollFullyIntoViewAndAssertExists(scaContinueButton, timeout)
            Assert.assertTrue(scaContinueButton.click())
        }
    }
    
    //Check that a card payment that does invoke 3D-Secure succeeds
    @Test
    fun itShouldSucceedAtPaymentWithoutScaV3() {
        isV3 = true
        fullPaymentTest(
            cardNumbers = arrayOf(noScaCardNumber1, noScaCardNumber2, noScaCardNumber3),
            cvv = noScaCvv
        ) {}
    }
    
    */

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private var payerReference: String = ""
    
    // Check that paymentTokens work with V3
    // https://developer.swedbankpay.com/checkout-v3/payments-only/features/optional/one-click-payments
    @Test
    fun testPaymentTokensV3() {
        // create a random string as reference
        payerReference = (1..15)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");
        
        val payer = PaymentOrderPayer(payerReference = payerReference)
        var order = paymentOrder.copy(generatePaymentToken = true, payer = payer)
        buildArguments(isV3 = true, paymentOrder = order)
        
        fullPaymentTest(
            cardNumbers = arrayOf(noScaCardNumber1, noScaCardNumber2, noScaCardNumber3),
            cvv = noScaCvv,
            storeCard = true
        ) {}

        teardown()
        setupAgain()
        
        order = paymentOrder.copy(generatePaymentToken = false, payer = payer)
        buildArguments(isV3 = true, paymentOrder = order)
        
        //now redo this with only "Pay SEK" button
        Assert.assertTrue("WebView not found", webView.waitForExists(timeout))
        webView.waitAndScrollFullyIntoViewAndAssertExists(payButton, timeout)
        payButton.clickUntilCheckedAndAssert(timeout)
        
    }
}
