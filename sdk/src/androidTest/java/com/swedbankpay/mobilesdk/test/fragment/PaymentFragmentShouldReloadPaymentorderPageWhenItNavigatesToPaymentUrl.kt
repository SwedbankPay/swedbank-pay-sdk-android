package com.swedbankpay.mobilesdk.test.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.web.assertion.WebViewAssertions
import androidx.test.espresso.web.model.SimpleAtom
import androidx.test.espresso.web.sugar.Web
import com.swedbankpay.mobilesdk.test.TestConstants
import org.junit.Test

class PaymentFragmentShouldReloadPaymentorderPageWhenItNavigatesToPaymentUrl : BasePaymentFragmentTest() {
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