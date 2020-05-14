package com.swedbankpay.mobilesdk.test.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.web.sugar.Web
import org.junit.Test

/**
 * PaymentFragment test: initial content
 */
class PaymentFragmentShouldShowViewPaymentorderPage : BasePaymentFragmentTest() {
    /**
     * Check that PaymentFragment shows the paymentorder page
     * after an anonymous payment has been successfully started
     */
    @Test
    fun test() {
        stubAnonymousMockPayment()

        scenario = launchFragmentInContainer(args)

        Web.onWebView().checkIsShowingPaymentOrder()
    }
}