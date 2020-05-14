package com.swedbankpay.mobilesdk.test.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.web.assertion.WebViewAssertions
import androidx.test.espresso.web.model.SimpleAtom
import androidx.test.espresso.web.sugar.Web
import com.swedbankpay.mobilesdk.test.TestConstants
import org.junit.Test

/**
 * PaymentFragment test: reload from paymentUrl navigation
 */
class PaymentFragmentShouldReloadPaymentorderPageWhenItNavigatesToPaymentUrl : BasePaymentFragmentTest() {
    /**
     * Check that PaymentFragment correctly reloads the paymentorder page
     * after a navigation to paymentUrl
     */
    @Test
    fun test() {
        stubAnonymousMockPayment()

        scenario = launchFragmentInContainer(args)

        Web.onWebView()
            .checkIsShowingPaymentOrder()
            .perform(SimpleAtom("window.location = 'about:blank'"))
            .check(WebViewAssertions.webContent(HasNoScriptsMatcher()))
            .perform(SimpleAtom("window.location = '${TestConstants.paymentUrl}'"))
            .checkIsShowingPaymentOrder()
    }
}