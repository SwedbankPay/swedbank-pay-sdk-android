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
 * PaymentFragment test: opening the fallback url of an intent-scheme url
 */
class PaymentFragmentShouldOpenFallbackUrlAfterIntentUrlFailure : BasePaymentFragmentTest() {

    private val fallbackUrl = "https://example.com/fallback"

    private val externalAppIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://example.com/")
    ).apply {
        `package` = "invalid.com.example.app"
        putExtra("browser_fallback_url", fallbackUrl)
    }

    private val expectedExternalAppIntent = Intent(externalAppIntent).apply {
        addCategory(Intent.CATEGORY_BROWSABLE)
    }

    private val expectedFallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl)).apply {
        addCategory(Intent.CATEGORY_BROWSABLE)
    }

    private val intentUrl = externalAppIntent.toUri(Intent.URI_INTENT_SCHEME).toString()

    /**
     * Check that PaymentFragment attempts to open the fallback url
     * after it fails to start an activity from an intent-scheme url
     */
    @Test
    fun test() {
        stubAnonymousMockPayment()

        scenario = launchFragmentInContainer(intentTestArgs)

        Intents.init()
        try {
            // We want expectedExternalAppIntent to fail. Unfortunately there is no way to
            // specify this in the espresso-intents API. We just assume that the package
            // invalid.com.example.app does not exists on the test device.

            Intents.intending(IntentMatchers.filterEquals(expectedFallbackIntent)).respondWith(
                Instrumentation.ActivityResult(0, Intent())
            )

            Web.onWebView()
                .withNoTimeout()
                .perform(SimpleAtom("window.location = '${intentUrl}'"))
                .checkIsShowingPaymentOrder()

            Intents.intended(IntentMatchers.filterEquals(expectedExternalAppIntent))
            Intents.intended(IntentMatchers.filterEquals(expectedFallbackIntent))
            Intents.assertNoUnverifiedIntents()
        } finally {
            Intents.release()
        }
    }
}
