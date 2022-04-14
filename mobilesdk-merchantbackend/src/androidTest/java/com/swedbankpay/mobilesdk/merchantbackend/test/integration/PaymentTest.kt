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
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import java.lang.Thread.sleep
import java.util.*


/// End-to-end tests for PaymentFragment
class PaymentTest {
    private companion object {
        const val timeout = 30_000L
        const val longTimeout = 120_000L
        // Key input to the web view is laggy, and without a delay between keystrokes, the input may get jumbled.
        const val keyInputDelay = 500L

        const val noScaCardNumber1 = "4581097032723517"
        const val noScaCardNumber2 = "4925000000000004"
        const val noScaCardNumber3 = "5226600159865967"
        const val scaCardNumber1 = "5226612199533406" //This might have issues: 4761739001010416
        const val scaCardNumber2 = "5226612199533406"
        const val scaCardNumber3 = "4547781087013329"
        const val expiryDate = "1230"
        const val noScaCvv = "111"
        const val scaCvv = "268"
    }
    
    @Before
    /**
     * Set up test configuration and start scenario for PaymentFragment 
     */
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
    
    /// to repeat tests within the same run
    private fun setupAgain() {
        PaymentFragment.defaultConfiguration = paymentTestConfiguration
        scenario
    }
    
    @After
    /// Destroy scenario
    fun teardown() {
        scenario.moveToState(Lifecycle.State.DESTROYED)
        _scenario = null
        PaymentFragment.defaultConfiguration = null
    }
    
    // paymentOrder gets regenerated every run
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
    private fun assertWebView() {
        Assert.assertTrue("WebView not found", webView.waitForExists(timeout))
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

    private val confirmButton
        get() = cardDetails.getChild(UiSelector().className(Button::class.java).textStartsWith("Confirm"))

    private val scaContinueButton
        get() = webView.getChild(UiSelector().className(Button::class.java).text("Continue"))
    private val storeCardOption
        get() = webView.getChild(UiSelector().textStartsWith("Store this card").checkable(true))

    private val yourEmailInput
        get() = webView.getChild(UiSelector().textStartsWith("Your e-mail"))
    
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
        useConfirmButton: Boolean = false,
        paymentFlowHandler: () -> Unit
    ): Boolean {
        if (!webView.waitForExists(timeout)) {
            return false
        }

        if (!webView.waitAndScrollUntilExists(cardOption, timeout)) {
            return false
        }
        cardOption.clickUntilCheckedAndAssert(timeout)

        if (storeCard) {
            if (!storeCardOption.waitForExists(timeout)) {
                return false
            }
            storeCardOption.clickUntilCheckedAndAssert(timeout)
        }

        if (!webView.waitAndScrollUntilExists(creditCardOption, timeout)) {
            return false
        }
        creditCardOption.clickUntilCheckedAndAssert(timeout)

        if (!webView.waitAndScrollUntilExists(panInput, timeout)) {
            return false
        }
        panInput.inputText(cardNumber)

        if (!webView.waitAndScrollUntilExists(expiryDateInput, timeout)) {
            return false
        }
        expiryDateInput.inputText(expiryDate)

        if (!webView.waitAndScrollUntilExists(cvvInput, timeout)) {
            return false
        }
        cvvInput.inputText(cvv)
        return fullPaymentTestAttemptCont(useConfirmButton, paymentFlowHandler) 
    }
    
    /// Due to codacy complexity rules we must break up this function. All it does is to continue the process.
    private fun fullPaymentTestAttemptCont(
        useConfirmButton: Boolean = false,
        paymentFlowHandler: () -> Unit
    ): Boolean {
        
        if (useConfirmButton) {
            if (!webView.waitAndScrollUntilExists(confirmButton, longTimeout)) { return false }
            if (!confirmButton.click()) { return false }
        } else {
            if (!webView.waitAndScrollUntilExists(payButton, longTimeout)) { return false }
            if (!payButton.click()) { return false }
        }

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
        useConfirmButton: Boolean = false,
        paymentFlowHandler: () -> Unit
    ) {
        scenario
        
        var success = false
        for (cardNumber in cardNumbers) {
            success = fullPaymentTestAttempt(cardNumber, cvv, storeCard, useConfirmButton, paymentFlowHandler)
            if (success) {
                break
            }
            // it failed try again with another number
            teardown()
            setupAgain()
        }

        if (!success) {

            val cardNumber = cardNumbers[0]
            assertWebView()

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
            
            if (useConfirmButton) {
                //webView.waitAndScrollFullyIntoViewAndAssertExists(confirmButton, longTimeout)
                Assert.assertTrue(confirmButton.click())
            } else {
                webView.waitAndScrollFullyIntoViewAndAssertExists(payButton, longTimeout)
                Assert.assertTrue(payButton.click())
            }

            paymentFlowHandler()
            lastResult = waitForResult()
        }
        Assert.assertNotNull("PaymentFragment progress timeout", lastResult)
        Assert.assertEquals(PaymentViewModel.State.COMPLETE, lastResult)
    }

    private fun waitForResult(waitTime: Long = timeout): PaymentViewModel.State? {
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
            withTimeoutOrNull(waitTime) {
                result.await()
            }
        }
    }


    private fun waitForAnyOrderInfo(waitTime: Long = timeout): ViewPaymentOrderInfo? {
        val result = CompletableDeferred<ViewPaymentOrderInfo>()
        scenario.onFragment {
            it.requireActivity().paymentViewModel.state.observe(it) { _ ->
                val vm = it.requireActivity().paymentViewModel
                val info = vm.richState.value?.viewPaymentOrderInfo
                if (info != null) {
                    result.complete(info)
                }
            }
        }
        return runBlocking {
            withTimeoutOrNull(waitTime) {
                result.await()
            }
        }
    }
    
    private fun waitForNewOrderInfo(waitTime: Long = timeout): ViewPaymentOrderInfo? {
        val result = CompletableDeferred<ViewPaymentOrderInfo>()
        scenario.onFragment {
            it.requireActivity().paymentViewModel.state.observe(it) { state ->
                if (
                    state != null
                    && state != PaymentViewModel.State.IDLE
                ) {
                    val vm = it.requireActivity().paymentViewModel
                    val info = vm.richState.value?.viewPaymentOrderInfo

                    if (info != null) {
                        result.complete(info)
                    }
                }
            }
        }
        return runBlocking {
            withTimeoutOrNull(waitTime) {
                result.await()
            }
        }
    }
    
    // Sanity check: Check that a WebView is displayed by the PaymentFragment
    @Test
    fun itShouldDisplayWebView() {
        buildArguments(isV3 = false)
        scenario
        assertWebView()
        Assert.assertTrue("Card options not found", cardOption.waitForExists(timeout))
    }
    
    @Test
    // Check that a card payment that does not invoke 3D-Secure succeeds
    fun itShouldSucceedAtPaymentWithoutSca() {
        buildArguments(isV3 = false)
        fullPaymentTest(
            cardNumbers = arrayOf(noScaCardNumber1, noScaCardNumber2, noScaCardNumber3),
            cvv = noScaCvv
        ) {}
    }
    
    @Test
    //Check that a card payment that does invoke 3D-Secure succeeds
    fun itShouldSucceedAtPaymentWithSca() {
        buildArguments(isV3 = false)
        fullPaymentTest(
            cardNumbers = arrayOf(scaCardNumber1, scaCardNumber2, scaCardNumber3, scaCardNumber1),
            cvv = scaCvv
        ) {
            webView.waitAndScrollFullyIntoViewAndAssertExists(scaContinueButton, timeout)
            Assert.assertTrue(scaContinueButton.click())
        }
    }
    
    @Test
    /// Check that a card payment that does invoke 3D-Secure succeeds
    fun itShouldSucceedAtPaymentWithScaV3() {
        fullPaymentTest(
            cardNumbers = arrayOf(scaCardNumber1, scaCardNumber2, scaCardNumber3, scaCardNumber1),
            cvv = scaCvv
        ) {
            webView.waitAndScrollFullyIntoViewAndAssertExists(scaContinueButton, timeout)
            Assert.assertTrue(scaContinueButton.click())
        }
    }
    
    @Test
    /// Check that a card payment that does invoke 3D-Secure succeeds
    fun itShouldSucceedAtPaymentWithoutScaV3() {
        fullPaymentTest(
            cardNumbers = arrayOf(noScaCardNumber1, noScaCardNumber2, noScaCardNumber3),
            cvv = noScaCvv
        ) {}
    }
    
    // generate a payerReference with a random string
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private var payerReference: String = ""
    
    /// Check that paymentTokens work with V3
    /// https://developer.swedbankpay.com/checkout-v3/payments-only/features/optional/one-click-payments
    // @Test
    fun testPaymentTokensV3() {
        // create a random string as reference
        payerReference = (1..15)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
        
        val payer = PaymentOrderPayer(payerReference = payerReference)
        var order = paymentOrder.copy(generatePaymentToken = true, payer = payer)
        buildArguments(isV3 = true, paymentOrder = order)

        val orderInfo = waitForAnyOrderInfo()
        Assert.assertNotNull(orderInfo?.id)
        val paymentId = orderInfo?.id!!
        var expandedOrder = getPaymentToken(paymentId)?.paymentOrder
        Assert.assertNotNull(expandedOrder?.id)
        
        fullPaymentTest(
            cardNumbers = arrayOf(scaCardNumber1, scaCardNumber3, scaCardNumber2),
            cvv = scaCvv,
            storeCard = true
        ) {
            webView.waitAndScrollFullyIntoViewAndAssertExists(scaContinueButton, timeout)
            Assert.assertTrue(scaContinueButton.click())
        }
        
        expandedOrder = getPaymentToken(paymentId)?.paymentOrder
        Assert.assertNotNull(expandedOrder?.paid?.tokens?.first()?.token)
        val token = expandedOrder?.paid?.tokens?.first()?.token!!
        order = paymentOrder.copy(generatePaymentToken = false, payer = payer, paymentToken = token)
        
        buildArguments(isV3 = true, paymentOrder = order)
        teardown()
        setupAgain()
        
        //now redo this with only "Pay SEK" button
        assertWebView()

        webView.waitAndScrollFullyIntoViewAndAssertExists(cardOption, timeout)
        cardOption.clickUntilCheckedAndAssert(timeout)
        
        webView.waitAndScrollFullyIntoViewAndAssertExists(payButton, timeout)
        Assert.assertTrue(payButton.click())
        
        lastResult = waitForResult()
        Assert.assertNotNull("PaymentFragment progress timeout", lastResult)
        Assert.assertEquals(PaymentViewModel.State.COMPLETE, lastResult)
        
        // One can expand "authorizations" of the payment to see the paymentToken value - but since we send it in we know what it is and it wouldn't complete the purchase if it didn't work. So this is enough.
    }

    @Test
    /// Test specifying and switching instruments.
    fun testPaymentInstrumentsV3() {
        val order = paymentOrder.copy(instrument = PaymentInstruments.INVOICE_SE)
        buildArguments(isV3 = true, paymentOrder = order)
        scenario
        webView.assertExist(timeout)
        yourEmailInput.assertExist(timeout)
        
        for (i in 0..4) {
            val orderInfo = waitForNewOrderInfo()
            if (orderInfo?.instrument == null) {
                continue
            }
            scenario.onFragment {
                val vm = it.requireActivity().paymentViewModel
                vm.updatePaymentOrder(PaymentInstruments.CREDIT_CARD)
            }
            break
        }
        creditCardOption.assertExist(timeout)
        //we managed to change the instrument!

        scenario.onFragment {
            val vm = it.requireActivity().paymentViewModel
            vm.updatePaymentOrder(PaymentInstruments.INVOICE_SE)
        }
        webView.assertExist(timeout)
        SystemClock.sleep(1000)
        yourEmailInput.assertExist(timeout)
    }

    @Test
    /// Test that instruments still work in v2
    fun testPaymentInstrumentsV2() {
        val order = paymentOrder.copy(instrument = PaymentInstruments.INVOICE_SE)
        buildArguments(isV3 = false, paymentOrder = order)
        scenario
        webView.assertExist(timeout)
        yourEmailInput.assertExist(timeout)

        for (i in 0..4) {
            val orderInfo = waitForNewOrderInfo()
            if (orderInfo?.instrument == null) {
                continue
            }
            scenario.onFragment {
                val vm = it.requireActivity().paymentViewModel
                vm.updatePaymentOrder(PaymentInstruments.CREDIT_CARD)
            }
            break
        }
        creditCardOption.assertExist(timeout)
        //we managed to change the instrument!

        scenario.onFragment {
            val vm = it.requireActivity().paymentViewModel
            vm.updatePaymentOrder(PaymentInstruments.INVOICE_SE)
        }
        webView.assertExist(timeout)
        SystemClock.sleep(1000)
        yourEmailInput.assertExist(timeout)
    }
    
    @Test
    /// Test that we can perform a verifu request and set the recur and unscheduled tokens.
    fun testVerifyRecurTokenV3() {
        val order = paymentOrder.copy(operation = PaymentOrderOperation.VERIFY, generateRecurrenceToken = true, generateUnscheduledToken = true)
        buildArguments(isV3 = true, paymentOrder = order)
        scenario
        
        //val oldId = "/psp/paymentorders/d14e582f-dbd5-42b4-f582-08da17b00645"
        
        // Remember the paymentId for later lookup
        Assert.assertTrue(webView.waitForExists(longTimeout))
        val orderInfo = waitForAnyOrderInfo()
        Assert.assertNotNull(orderInfo?.id)
        val paymentId = orderInfo?.id!!
        
        //perform a regular SCA purchase - it must be secure to retrieve tokens
        fullPaymentTest(
            cardNumbers = arrayOf(scaCardNumber2, scaCardNumber3),
            cvv = scaCvv,
            useConfirmButton = true
        ) {
            webView.waitAndScrollFullyIntoViewAndAssertExists(scaContinueButton, timeout)
            Assert.assertTrue(scaContinueButton.click())
        }

        val conf = PaymentFragment.defaultConfiguration
        scenario.onFragment {
            
            var result: PaymentTokenResponse? = null
            val vm = it.requireActivity().paymentViewModel
            //Then implement expand operation to see that everything worked
            GlobalScope.launch (Dispatchers.Default) {
                try {
                    result = conf?.expandOperation(it.requireActivity().application, paymentId, arrayOf<String>("paid"), "tokens", PaymentTokenResponse::class.java)
                } catch (error: Exception) {
                    val message = error.localizedMessage
                    Assert.assertNull("Error when fetching tokens", message)
                }
                Assert.assertNotNull(result)
            }
            
            //Wait for the process to finish
            for (noop in 1..10000) {
                sleep(500)
                if (result != null) {
                    break
                }
            }
            Assert.assertTrue(result?.recurrence ?: false)
            Assert.assertTrue(result?.unscheduled ?: false)
        }
    }
    
    fun getPaymentToken(paymentId: String): ExpandedPaymentOrder?  {

        var result: ExpandedPaymentOrder? = null
        val conf = PaymentFragment.defaultConfiguration
        scenario.onFragment {
            
            val vm = it.requireActivity().paymentViewModel
            //Then implement expand operation to see that everything worked
            GlobalScope.launch (Dispatchers.Default) {
                try {
                    result = conf?.expandOperation(
                        it.requireActivity().application, 
                        paymentId, 
                        arrayOf<String>("paid"), 
                        "expand",
                        ExpandedPaymentOrder::class.java
                    )
                } catch (error: Exception) {
                    val message = error.localizedMessage
                    Assert.assertNull("Error when fetching tokens", message)
                }
            }

            //Wait for the process to finish
            for (noop in 1..10000) {
                sleep(500)
                if (result != null) {
                    break
                }
            }
        }
        return result
    }
}

data class ExpandedPaymentOrder(
    val paymentOrder: ViewPaymentOrderInfo
)