package com.swedbankpay.mobilesdk.test.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import org.junit.Test

class PaymentFragmentShouldStartWithoutCrashing : BasePaymentFragmentTest() {
    @Test
    fun test() {
        scenario = launchFragmentInContainer(args)
    }
}