package com.swedbankpay.mobilesdk.merchantbackend.test.integration

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.swedbankpay.mobilesdk.*
import com.swedbankpay.mobilesdk.merchantbackend.UnexpectedResponseException
import com.swedbankpay.mobilesdk.merchantbackend.test.integration.util.*
import kotlinx.coroutines.*
import java.lang.Thread.sleep
import java.util.*
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor
import androidx.test.runner.screenshot.ScreenCaptureProcessor
import androidx.test.runner.screenshot.Screenshot
import org.junit.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.File
import java.io.IOException

/**
 * End-to-end tests for PaymentFragment
 */
class PaymentTest {
    private companion object {
        const val shortTimeout = 10_000L
        const val timeout = 30_000L
        const val longTimeout = 60_000L
        // Key input to the web view is laggy, and without a delay between keystrokes, the input may get jumbled.
        const val keyInputDelay = 500L
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
        refreshPaymentOrder()
    }
    
    private fun refreshPaymentOrder() {
        paymentOrder = PaymentOrder(
            currency = Currency.getInstance("SEK"),
            amount = 200L,
            vatAmount = 50L,
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
        refreshPaymentOrder()
        scenario
        sleep(1000)
    }
    
    @After
    /**
     * Destroy scenario
     */
    fun teardown() {
        scenario.moveToState(Lifecycle.State.DESTROYED)
        _scenario = null
        PaymentFragment.defaultConfiguration = null
        paymentTestConfiguration = paymentOnlyTestConfiguration
    }
    
    // paymentOrder gets regenerated every run
    private var paymentOrder: PaymentOrder = PaymentOrder(
        currency = Currency.getInstance("SEK"),
        amount = 200L,
        vatAmount = 50L,
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
        UiDevice.getInstance(getInstrumentation())
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
    private val prefilledCardButton
        get() = cardDetails.getChild(UiSelector().textContains(prefilledCardSecret))
    private val addAnotherCardLink
        get() = webView.getChild(UiSelector().textContains("add another card"))
    private val confirmButton
        get() = cardDetails.getChild(UiSelector().className(Button::class.java).textStartsWith("Confirm"))

    private val scaContinueButton
        get() = webView.getChild(UiSelector().className(Button::class.java).text("Continue"))
    private val storeCardOption
        get() = webView.getChild(UiSelector().textStartsWith("Store this card").checkable(true))
    private val whitelistMerchantBox
        get() = webView.getChild(UiSelector().resourceIdMatches("whitelistMerchant").checkable(true))
    private val sendOtpButton
        get() = webView.getChild(UiSelector().resourceIdMatches("sendOtp"))

    private val yourEmailInput
        get() = webView.getChild(UiSelector().textStartsWith("Your e-mail"))
    
    private val yourEmailInputOther
        get() = webView.getChild(UiSelector().textStartsWith("Your email"))
    
    private val ndmChallangeInput
        get() = webView.getChild(UiSelector().className(EditText::class.java).instance(0))
    
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
        scaPaymentButton: Boolean = false
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
            if (!storeCardOption.isChecked) {
                storeCardOption.clickUntilCheckedAndAssert(timeout)
            }
        }

        if (!fillInCardDetails(cardNumber, cvv, useConfirmButton, scaPaymentButton)) { return false }
        
        return fullPaymentTestAttemptCont() 
    }
    
    private fun fillInCardDetails(cardNumber: String, cvv: String, useConfirmButton: Boolean, scaPaymentButton: Boolean): Boolean {
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

        if (useConfirmButton) {
            if (!webView.waitAndScrollUntilExists(confirmButton, longTimeout)) { return false }
            if (!confirmButton.click()) { return false }
        } else {
            if (!webView.waitAndScrollUntilExists(payButton, longTimeout)) { return false }
            if (!payButton.click()) { return false }
        }
        
        if (scaPaymentButton) {
            if (!continueSCAPayment()) return false
        }
        return true
    }

    private fun prefilledPaymentAttempt() {
        
        prefilledCardButton.assertExist(timeout, "Could not find prefilledCardButton")
        prefilledCardButton.click()
        
        if (!webView.waitAndScrollUntilExists(payButton, longTimeout)) { Assert.fail("Could not scroll payButton in prefilledPayment") }
        if (!payButton.click()) { Assert.fail("Could not click payButton in prefilledPayment") }
        /*
        if (scaPaymentButton) {
            if (!webView.waitAndScrollUntilExists(scaContinueButton, timeout)) { return false }
            if (!scaContinueButton.click()) { return false }
        }
         */
    }
    
    /// sometimes the ndm-challange form appears here instead of regular 3d-secure. We need to wait to see which appears
    private fun continueSCAPayment(): Boolean {
        val existingObject = waitForOne(timeout, arrayOf(scaContinueButton, whitelistMerchantBox))
        if (scaContinueButton == existingObject) {
            if (!scaContinueButton.click()) { return false }
        }
        else if (ndmChallangeInput.waitForExists(timeout)) {
            ndmChallangeInput.inputText("1234")
            if (!whitelistMerchantBox.isChecked) {
                whitelistMerchantBox.clickUntilCheckedAndAssert(timeout)
            }
            retryUntilTrue(timeout) {
                sendOtpButton.click()
            }
        }
        else {
            return false
        }
        return true
    }
    
    /// Due to codacy complexity rules we must break up this function. All it does is to continue the process.
    private fun fullPaymentTestAttemptCont(): Boolean {
        
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
        scaPaymentButton: Boolean = false
    ) {
        scenario
        
        var success = false
        for (cardNumber in cardNumbers) {
            success = fullPaymentTestAttempt(cardNumber, cvv, storeCard, useConfirmButton, scaPaymentButton)
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
    
    /**
     * Sanity check: Check that a WebView is displayed by the PaymentFragment
     */
    @Test
    fun itShouldDisplayWebView() {
        buildArguments(isV3 = false)
        scenario
        assertWebView()
        Assert.assertTrue("Card options not found", cardOption.waitForExists(timeout))
    }

    /**
     * Check that a card payment that does not invoke 3D-Secure succeeds
     */
    @Test
    fun itShouldSucceedAtPaymentWithoutSca() {
        buildArguments(isV3 = false)
        fullPaymentTest(
            cardNumbers = nonScaCardNumbers,
            cvv = noScaCvv
        )
    }

    /**
     * Check that a card payment that does invoke 3D-Secure succeeds
     */
    @Test
    fun itShouldSucceedAtPaymentWithSca() {
        buildArguments(isV3 = false)
        fullPaymentTest(
            cardNumbers = scaCardNumbers,
            cvv = scaCvv,
            scaPaymentButton = true
        )
    }

    /**
     * Check that a card payment that does invoke 3D-Secure succeeds
     */
    @Test
    fun itShouldSucceedAtPaymentWithScaV3() {
        fullPaymentTest(
            cardNumbers = scaCardNumbers,
            cvv = scaCvv,
            scaPaymentButton = true
        )
    }

    /**
     * Check that a card payment that does invoke 3D-Secure succeeds
     */
    @Test
    fun itShouldSucceedAtPaymentWithoutScaV3() {
        for (config in testConfigurations) {
            teardown()
            paymentTestConfiguration = config
            setupAgain()
            fullPaymentTest(
                cardNumbers = nonScaCardNumbers,
                cvv = noScaCvv
            )
        }
    }
    
    // generate a payerReference with a random string
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private var payerReference: String = ""
    
    /**
     * Check that paymentTokens work with V3
     * https://developer.swedbankpay.com/checkout-v3/payments-only/features/optional/one-click-payments
     */
    @Test
    fun testOneClickV3PaymentsOnly() {
        // create a random string as reference
        payerReference = (1..15)
            .map { Random().nextInt(charPool.size) }
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
            cardNumbers = scaCardNumbers,
            cvv = scaCvv,
            storeCard = true,
            scaPaymentButton = true
        )
        
        expandedOrder = getPaymentToken(paymentId)?.paymentOrder
        Assert.assertNotNull(expandedOrder?.paid?.tokens?.first()?.token)
        val token = expandedOrder?.paid?.tokens?.first()?.token!!
        order = paymentOrder.copy(generatePaymentToken = false, payer = payer, paymentToken = token)
        //Now we must wait for BottomSheetBehavior.java to stop messaging the webview, otherwise test will crash. The error is in Google's code so not much we can do.
        sleep(2000)
        teardown()
        buildArguments(isV3 = true, paymentOrder = order)
        setupAgain()
        
        //now redo this with only "Pay SEK" button
        assertWebView()
        
        webView.waitAndScrollFullyIntoViewAndAssertExists(payButton, timeout)
        Assert.assertTrue(payButton.click())

        continueSCAPayment()
        
        lastResult = waitForResult(longTimeout)
        Assert.assertNotNull("PaymentFragment progress timeout", lastResult)
        Assert.assertEquals("PaymentState is not complete", PaymentViewModel.State.COMPLETE, lastResult)
    }
    
    /**
     * Check that oneClick works for enterprise merchants in V3 using social security numbers.
     */
    @Test
    fun testOneClickV3EnterpriseNationalIdentifier() {

        Log.i("SDK", "starting testOneClickV3EnterpriseNationalIdentifier")
        for (i in 0..3) {
            try {
                runOneClickV3EnterpriseNationalIdentifier()
                return
            } catch (error: AssertionError) {
                // Attempt i did fail
                teardown()
            }
        }
        //one last try without catch
        runOneClickV3EnterpriseNationalIdentifier()
    }
    
    private fun runOneClickV3EnterpriseNationalIdentifier() {
        
        paymentTestConfiguration = enterpriseTestConfiguration
        PaymentFragment.defaultConfiguration = paymentTestConfiguration

        // create a random string as reference
        payerReference = (1..15)
            .map { Random().nextInt(charPool.size) }
            .map(charPool::get)
            .joinToString("")
        
        val payer = PaymentOrderPayer(
            payerReference = payerReference, nationalIdentifier = PayerNationalIdentifier(
                socialSecurityNumber = "199710202392", countryCode = "SE"
            )
        )
        prefilledCardPurchase(payer)
    }

    /**
     * Check that oneClick works for enterprise merchants in V3
     * https://developer.swedbankpay.com/checkout-v3/enterprise/features/optional/enterprise-payer-reference
     */
    @Test
    fun testOneClickV3EnterprisePayerReference() {
        
        Log.i("SDK", "starting testOneClickV3EnterprisePayerReference")
        for (i in 0..2) {
            try {
                oneClickV3EnterprisePayerReferenceRun()
                return
            } catch (err: Throwable) {
                //Still error, try again.
                teardown()
            }
        }
        oneClickV3EnterprisePayerReferenceRun()
    }
    
    private fun oneClickV3EnterprisePayerReferenceRun() { 
        paymentTestConfiguration = enterpriseTestConfiguration
        PaymentFragment.defaultConfiguration = paymentTestConfiguration

        // create a random string as reference
        payerReference = (1..15)
            .map { Random().nextInt(charPool.size) }
            .map(charPool::get)
            .joinToString("")

        val payer = PaymentOrderPayer(payerReference = payerReference, email = "leia.ahlstrom@payex.com", msisdn = "+46739000001")
        prefilledCardPurchase(payer)
    }
    
    private fun prefilledCardPurchase(payer: PaymentOrderPayer, knownReturningPayer: Boolean = false) {
        
        val order = paymentOrder.copy(payer = payer)
        buildArguments(isV3 = true, paymentOrder = order)
        scenario
        sleep(2000)

        waitForCard()
        
        // Check if the user has card details, otherwise fill them in and retry. If the payer is known, prefilled options must exist.
        val first = if (knownReturningPayer) {
            //we know this exist already
            prefilledCardButton
        } else {
            //could be either one of these
            waitForOne(shortTimeout, arrayOf(creditCardOption, prefilledCardButton))
        }
        if (first == null || creditCardOption == first) {
            //Create the card, we need to know which card is used, since not all cards work for us.
            if (first == null) {
                addAnotherCardLink.click()
            }

            if (!fillInCardDetails(
                    prefilledCardNo, scaCvv,
                    useConfirmButton = false,
                    scaPaymentButton = false
                )) {
                Assert.fail("Could not find prefilled options, and could not fill in new card")
            }
            if (scaContinueButton.waitForExists(timeout)) {
                Assert.assertTrue("scaContinueButton could not be clicked", scaContinueButton.click())
            }
            waitForResult()
            
            teardown()

            paymentTestConfiguration = enterpriseTestConfiguration
            PaymentFragment.defaultConfiguration = paymentTestConfiguration
            prefilledCardPurchase(payer, knownReturningPayer = true)
            return
        }
        //otherwise the card has already been created, so we just need select & tap

        prefilledPaymentAttempt()
        
        // Since we don't know if the stored card is an sca-card or not, we can't assert on the continue button
        // perhaps speed thing up by getting the result first...
        if (scaContinueButton.waitForExists(timeout)) {
            Assert.assertTrue("scaContinueButton could not be clicked", scaContinueButton.click())
        }
        
        lastResult = waitForResult(timeout)
        // if "PaymentFragment progress timeout" happens it's usually the dreaded "Something went wrong!" error, which gives no feedback of any kind. 
        //there is nothing we can do about that but try again in a few hours.
        Assert.assertNotNull("PaymentFragment progress timeout", lastResult)
        Assert.assertEquals(PaymentViewModel.State.COMPLETE, lastResult)
    }
    
    private fun waitForCard() {
        if (!webView.waitForExists(timeout)) {
            Assert.fail("No webview while waiting for card")
        }
        
        if (!webView.waitAndScrollUntilExists(cardOption, timeout)) {
            Assert.fail("Could not scroll to see cardOption while waiting for card")
        }
        
        val didClick = retryUntilTrue(longTimeout) {
            cardOption.click()
        }
        Assert.assertTrue("Could not click cardOption while waiting for card", didClick)
    }

    /**
     * Test specifying and switching instruments.
     */
    @Test
    fun testPaymentInstrumentsV3() {
        for (i in 0..6) {
            try {
                testPaymentInstrumentsV3Run((i % 2) == 0)
                return
            } catch (err: Throwable) {
                //Still error, try again.
                teardown()
                PaymentFragment.defaultConfiguration = paymentTestConfiguration
            }
        }
        testPaymentInstrumentsV3Run(true)
    }

    private fun testPaymentInstrumentsV3Run(useCreditAccount: Boolean) {
        val instrument = if (useCreditAccount) PaymentInstruments.CREDIT_ACCOUNT else PaymentInstruments.INVOICE_SE
        val emailInput = if (useCreditAccount) yourEmailInputOther else yourEmailInput
        val order = paymentOrder.copy(instrument = instrument)
        buildArguments(isV3 = true, paymentOrder = order)
        scenario
        webView.assertExist(timeout)
        emailInput.assertExist(timeout)
        
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
        
        creditCardOption.assertExist(longTimeout)
        //we managed to change the instrument!

        scenario.onFragment {
            val vm = it.requireActivity().paymentViewModel
            vm.updatePaymentOrder(instrument)
        }
        webView.assertExist(timeout)
        SystemClock.sleep(1000)
        emailInput.assertExist(timeout)
    }

    /**
     * Test that instruments still work in v2
     * we don't need to test this anymore, focus on V3
    @Test
    fun testPaymentInstrumentsV2() {
        for (i in 0..3) {
            try {
                testPaymentInstrumentsV2Run()
                return
            } catch (error: AssertionError) {
                // Attempt i did fail
                teardown()
                setupAgain()
            }
        }
        //one last try without catch
        testPaymentInstrumentsV2Run()
    }
    
    private fun testPaymentInstrumentsV2Run() {
        val order = paymentOrder.copy(instrument = PaymentInstruments.INVOICE_SE)
        buildArguments(isV3 = false, paymentOrder = order)
        scenario
        webView.assertExist(timeout)
        //somehow the tests break here (only github actions), a delay seems to work but what could be the cause? 
        sleep(100)
        yourEmailInput.assertExist(timeout)
        
        var didPass = false
        for (i in 0..4) {
            
            val orderInfo = waitForNewOrderInfo()
            if (orderInfo?.instrument == null) {
                continue
            }
            didPass = true
            scenario.onFragment {
                val vm = it.requireActivity().paymentViewModel
                vm.updatePaymentOrder(PaymentInstruments.CREDIT_CARD)
            }
            sleep(100)
            break
        }
        assert(didPass) {
            "Did not get order info from paymentViewModel"
        }
        creditCardOption.assertExist(shortTimeout)
        //we managed to change the instrument!

        scenario.onFragment {
            val vm = it.requireActivity().paymentViewModel
            vm.updatePaymentOrder(PaymentInstruments.INVOICE_SE)
        }
        webView.assertExist(timeout)
        if (!yourEmailInput.waitForExists(shortTimeout)) {
            //try to change again
            scenario.onFragment {
                val vm = it.requireActivity().paymentViewModel
                vm.updatePaymentOrder(PaymentInstruments.INVOICE_SE)
            }
        }
        yourEmailInput.assertExist(shortTimeout)
    }
     */

    /**
     * Test that we can perform a verify request and set the recur and unscheduled tokens.
     */
    @Test
    fun testVerifyRecurTokenV3() {
        for (i in 0..3) {
            try {
                runVerifyRecurTokenV3()
                return
            } catch (error: AssertionError) {
                // Attempt i did fail
                teardown()
            }
        }
        //one last try without catch
        runVerifyRecurTokenV3()
    }
    
    @OptIn(DelicateCoroutinesApi::class)
    private fun runVerifyRecurTokenV3() {
        val order = paymentOrder.copy(operation = PaymentOrderOperation.VERIFY, generateRecurrenceToken = true, generateUnscheduledToken = true)
        //val order = paymentOrder.copy(operation = PaymentOrderOperation.VERIFY, 
        //    payer = PaymentOrderPayer(email = "leia.ahlstrom@payex.com", msisdn = "+46739000001", payerReference = "unique-identifier")
        //)
        buildArguments(isV3 = true, paymentOrder = order)
        scenario
        
        // Remember the paymentId for later lookup
        Assert.assertTrue(webView.waitForExists(longTimeout))
        val orderInfo = waitForAnyOrderInfo()
        Assert.assertNotNull(orderInfo?.id)
        val paymentId = orderInfo?.id!!
        
        //perform a regular SCA purchase - it must be secure to retrieve tokens
        fullPaymentTest(
            cardNumbers = scaCardNumbers,
            cvv = scaCvv,
            useConfirmButton = true,
            scaPaymentButton = true
        )

        val conf = PaymentFragment.defaultConfiguration
        scenario.onFragment {
            
            var result: PaymentTokenResponse? = null
            //Then implement expand operation to see that everything worked
            GlobalScope.launch (Dispatchers.Default) {
                try {
                    result = conf?.expandOperation(
                        it.requireActivity().application,
                        paymentId,
                        arrayOf("paid"),
                        "tokens",
                        PaymentTokenResponse::class.java
                    )
                } catch (error: UnexpectedResponseException) {
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
    
    @OptIn(DelicateCoroutinesApi::class)
    private fun getPaymentToken(paymentId: String): ExpandedPaymentOrder?  {

        var result: ExpandedPaymentOrder? = null
        val conf = PaymentFragment.defaultConfiguration
        scenario.onFragment {
            
            //Then implement expand operation to see that everything worked
            GlobalScope.launch (Dispatchers.Default) {
                try {
                    result = conf?.expandOperation(
                        it.requireActivity().application, 
                        paymentId, 
                        arrayOf("paid"), 
                        "expand",
                        ExpandedPaymentOrder::class.java
                    )
                } catch (error: UnexpectedResponseException) {
                    val message = error.localizedMessage
                    Assert.assertNull("Error when fetching payment tokens", message)
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
    
    @get:Rule
    val screenshotTestRule = ScreenshotTestRule()
}

/**
 * A data class to help testing expansions. 
 */
data class ExpandedPaymentOrder(
    val paymentOrder: ViewPaymentOrderInfo
)

/**
 * Set it to create screenshots on test failures
 */
class ScreenshotTestRule : TestWatcher() {
    override fun finished(description: Description?) {
        super.finished(description)

        val className = description?.testClass?.simpleName ?: "NullClassname"
        val methodName = description?.methodName ?: "NullMethodName"
        val filename = "$className - $methodName"
        captureScreen(filename)
    }

    /**
     * Capture a screenshot, and store it in the Pictures folder in the sdcard:
     * /sdcard/Android/data/com.swedbankpay.mobilesdk.merchantbackend.test/files/Pictures
     */
    fun captureScreen(filename: String) {
        
        val capture = Screenshot.capture()
        capture.name = filename
        capture.format = Bitmap.CompressFormat.PNG

        val processors = HashSet<ScreenCaptureProcessor>()
        processors.add(IDTScreenCaptureProcessor())

        try {
            capture.process(processors)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}

/**
 * Helper class for when generating screenshots
 */
class IDTScreenCaptureProcessor : BasicScreenCaptureProcessor() {
    init {
        mTag = "IDTScreenCaptureProcessor"
        mFileNameDelimiter = "-"
        mDefaultFilenamePrefix = "Swedbank"
        mDefaultScreenshotPath = getNewFilename()
    }

    private fun getNewFilename(): File? {
        val context = getInstrumentation().targetContext.applicationContext
        return context.getExternalFilesDir(DIRECTORY_PICTURES)
    }
}