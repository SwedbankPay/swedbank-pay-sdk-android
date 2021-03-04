package com.swedbankpay.mobilesdk.test.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.web.assertion.WebViewAssertions
import androidx.test.espresso.web.model.SimpleAtom
import androidx.test.espresso.web.sugar.Web
import com.swedbankpay.mobilesdk.internal.CallbackActivity
import com.swedbankpay.mobilesdk.test.TestConstants
import org.junit.Test

/**
 * PaymentFragment test: reload from CallbackActivity
 */
class PaymentFragmentShouldReloadPaymentorderPageAfterCallbackActivityStartedWithPaymentUrl : BasePaymentFragmentTest() {
    /**
     * Check that PaymentFragment correctly reloads the paymentorder page
     * after CallbackActivity is started with paymentUrl
     */
    @Test
    fun test() {
        stubAnonymousMockPayment()

        scenario = launchFragmentInContainer(args)

        Web.onWebView()
            .withNoTimeout()
            .checkIsShowingPaymentOrder()
            .perform(SimpleAtom("window.location = 'about:blank'"))
            .check(WebViewAssertions.webContent(HasNoScriptsMatcher()))

        val callbackIntent =
            Intent(ApplicationProvider.getApplicationContext(), CallbackActivity::class.java)
                .setData(Uri.parse(TestConstants.paymentUrl))
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ApplicationProvider.getApplicationContext<Context>().startActivity(callbackIntent)

        Web.onWebView()
            .withNoTimeout()
            .checkIsShowingPaymentOrder()
    }
}