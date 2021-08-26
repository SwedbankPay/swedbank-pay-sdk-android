package com.swedbankpay.mobilesdk.test

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.atMost
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.swedbankpay.mobilesdk.internal.CallbackActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import org.robolectric.annotation.Config

/**
 * Tests for CallbackActivity
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class CallbackActivityTest {
    /**
     * STRICT_STUBS mockito rule for cleaner tests
     */
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    /**
     * Check that CallbackActivity notifies observers of CallbackActivity.onCallbackUrlInvoked
     * when started (with FLAG_ACTIVITY_NEW_TASK; this is how Chrome should invoke it)
     */
    @Test
    fun itShouldNotifyObserversWhenStarted() {
        observing(CallbackActivity.onCallbackUrlInvoked) {
            val callbackIntent =
                Intent(ApplicationProvider.getApplicationContext(), CallbackActivity::class.java)
                    .setData(Uri.parse(TestConstants.paymentUrl))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ActivityScenario.launch<CallbackActivity>(callbackIntent).use {}

            // Adding an Observer may or may not invoke it immediately, depending on the current state of the LiveData
            // Thus, either 1 or 2 interactions means it works correctly
            verify(it, atLeastOnce()).onChanged(Unit)
            verify(it, atMost(2)).onChanged(Unit)
            verifyNoMoreInteractions(it)
            Assert.assertFalse(CallbackActivity.consumeCallbackUrl("wrongurl"))
            Assert.assertTrue(CallbackActivity.consumeCallbackUrl(TestConstants.paymentUrl))
            Assert.assertFalse(CallbackActivity.consumeCallbackUrl(TestConstants.paymentUrl))
        }
    }

    /**
     * Check that CallbackActivity notifies observers of CallbackActivity.onCallbackUrlInvoked
     * when started without FLAG_ACTIVITY_NEW_TASK.
     */
    @Test
    fun itShouldNotifyObserversWhenStartedWithoutNewTaskFlag() {
        observing(CallbackActivity.onCallbackUrlInvoked) {
            // Launch the activity without FLAG_ACTIVITY_NEW_TASK
            val callbackIntent = Intent(
                ApplicationProvider.getApplicationContext(),
                CallbackActivity::class.java
            )
                .setData(Uri.parse(TestConstants.paymentUrl))
            Intents.init()
            ActivityScenario.launch<CallbackActivity>(callbackIntent).use {}

            // CallbackActivity should start another activity in this case
            val newIntents = Intents.getIntents()
            Intents.release()
            Assert.assertTrue(newIntents.size == 1)
            val newIntent = newIntents.first()

            // The second activity should notify the observers
            ActivityScenario.launch<CallbackActivity>(newIntent).use {}

            // Adding an Observer may or may not invoke it immediately, depending on the current state of the LiveData
            // Thus, either 1 or 2 interactions means it works correctly
            verify(it, atLeastOnce()).onChanged(Unit)
            verify(it, atMost(2)).onChanged(Unit)
            verifyNoMoreInteractions(it)
            Assert.assertFalse(CallbackActivity.consumeCallbackUrl("wrongurl"))
            Assert.assertTrue(CallbackActivity.consumeCallbackUrl(TestConstants.paymentUrl))
            Assert.assertFalse(CallbackActivity.consumeCallbackUrl(TestConstants.paymentUrl))
        }
    }
}