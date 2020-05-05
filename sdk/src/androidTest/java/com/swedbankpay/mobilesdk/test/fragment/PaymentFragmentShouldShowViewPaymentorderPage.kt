package com.swedbankpay.mobilesdk.test.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.web.sugar.Web
import org.junit.Test

class PaymentFragmentShouldShowViewPaymentorderPage : BasePaymentFragmentTest() {
    @Test
    fun test() {
        stubAnonymousMockPayment()

        scenario = launchFragmentInContainer(args)

        Web.onWebView().checkIsShowingPaymentOrder()
    }
}