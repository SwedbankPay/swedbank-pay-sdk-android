package com.swedbankpay.mobilesdk.test.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import org.junit.Test

/**
 * PaymentFragment test: sanity
 */
class PaymentFragmentShouldStartWithoutCrashing : BasePaymentFragmentTest() {
    /**
     * Check that PaymentFragment starts without crashing
     * when started with correct arguments
     */
    @Test
    fun test() {
        stubAnonymousMockPayment()

        scenario = launchFragmentInContainer(args)
    }
}