package com.swedbankpay.mobilesdk.test.fragment

import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.web.model.SimpleAtom
import androidx.test.espresso.web.sugar.Web
import org.junit.Test

/**
 * PaymentFragment test: launching external apps
 */
class PaymentFragmentShouldLaunchExternalAppWhenItNavigatesToIntentUrl : BasePaymentFragmentTest() {

    private val externalAppIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://example.com/")
    ).apply { `package` = "com.example.app" }

    private val expectedIntent = Intent(externalAppIntent).apply {
        // CATEGORY_BROWSABLE should be added to all external app intents
        addCategory(Intent.CATEGORY_BROWSABLE)
    }

    private val intentUrl = externalAppIntent.toUri(Intent.URI_INTENT_SCHEME).toString()

    /**
     * Check that PaymentFragment starts an Activity when it navigates
     * to an intent-scheme url
     */
    @Test
    fun test() {
        stubAnonymousMockPayment()

        scenario = launchFragmentInContainer(intentTestArgs)

        Intents.init()
        try {
            Intents.intending(IntentMatchers.filterEquals(expectedIntent)).respondWith(
                Instrumentation.ActivityResult(0, Intent())
            )

            Web.onWebView()
                .withNoTimeout()
                .perform(SimpleAtom("window.location = '${intentUrl}'"))
                .checkIsShowingPaymentOrder()
            // See comment at PaymentFragmentShouldReportSuccessAfterNavigationToCompleteUrl

            Intents.intended(IntentMatchers.filterEquals(expectedIntent))
            Intents.assertNoUnverifiedIntents()
        } finally {
            Intents.release()
        }
    }
}
