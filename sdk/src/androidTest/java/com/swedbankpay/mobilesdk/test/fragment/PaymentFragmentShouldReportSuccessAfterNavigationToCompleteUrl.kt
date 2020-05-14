package com.swedbankpay.mobilesdk.test.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Observer
import androidx.test.espresso.web.model.SimpleAtom
import androidx.test.espresso.web.sugar.Web
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.swedbankpay.mobilesdk.PaymentViewModel
import com.swedbankpay.mobilesdk.paymentViewModel
import com.swedbankpay.mobilesdk.test.TestConstants
import org.junit.Test

/**
 * PaymentFragment test: completion
 */
class PaymentFragmentShouldReportSuccessAfterNavigationToCompleteUrl : BasePaymentFragmentTest() {
    /**
     * Check that PaymentFragment reports success
     * after a navigation to completeUrl
     */
    @Test
    fun test() {
        stubAnonymousMockPayment()

        scenario = launchFragmentInContainer(args)

        // cannot use observing() here because of the complex threading
        val observer = mock<Observer<PaymentViewModel.State>>()
        scenario.onFragment {
            it.requireActivity().paymentViewModel.state.observeForever(observer)
            // At this point, observer is being used in the main thread,
            // and we must not touch it except in another onFragment call
        }

        Web.onWebView()
            .perform(SimpleAtom("window.location = '${TestConstants.completeUrl}'"))

        scenario.onFragment {
            // Now, ideally we would verify here, but doing so will lose
            // information from the test logs, so just remove the observer
            // and verify back in instrumentation thread.
            it.requireActivity().paymentViewModel.state.removeObserver(observer)
        }

        verify(observer).onChanged(PaymentViewModel.State.SUCCESS)
    }
}