package com.swedbankpay.mobilesdk.test.fragment

import android.content.Context
import android.os.Build
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swedbankpay.mobilesdk.*
import com.swedbankpay.mobilesdk.test.TestConstants
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import java.lang.RuntimeException

/**
 * Test that PaymentFragment state is reported correctly
 * in different scenarios.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class PaymentFragmentStateTest {
    private companion object {
        const val FRAGMENT_TAG = "PaymentFragmentStateTest_Fragment_Tag"
    }

    private val configuration = object : Configuration() {
        override suspend fun postConsumers(
            context: Context,
            consumer: Consumer?,
            userData: Any?
        ) = TestConstants.viewConsumerIdentificationInfo

        override suspend fun postPaymentorders(
            context: Context,
            paymentOrder: PaymentOrder?,
            userData: Any?,
            consumerProfileRef: String?
        ) = TestConstants.viewPaymentorderInfo
    }

    private val arguments = PaymentFragment.ArgumentsBuilder().build()
    private var activityScenario: ActivityScenario<FragmentTestActivity>? = null
    private var activity: FragmentTestActivity? = null

    /**
     * Set up test configuration and start scenario
     */
    @Before
    fun setup() {
        PaymentFragment.defaultConfiguration = configuration
        try {
            activityScenario = ActivityScenario.launch(FragmentTestActivity::class.java)
        } catch (err: RuntimeException) {
            //The newer method isn't finding the test-activity, fallback to the deprecated but working method
            activity = Robolectric.setupActivity(FragmentTestActivity::class.java)
        }
    }

    /**
     * Destroy scenario
     */
    @After
    fun teardown() {
        activityScenario?.close()
        activity = null
        PaymentFragment.defaultConfiguration = null
    }

    private fun getState(): PaymentViewModel.State {
        var value: PaymentViewModel.State? = null
        val observer = Observer<PaymentViewModel.State> {
            value = it
        }
        
        activityScenario?.onActivity {
            it.paymentViewModel.state.run {
                observeForever(observer)
                removeObserver(observer)
            }
        }
        activity?.let {
            it.paymentViewModel.state.run {
                observeForever(observer)
                removeObserver(observer)
            }
        }

        return checkNotNull(value) { "Could not get PaymentViewModel state" }
    }

    private fun addFragment() {
        activityScenario?.onActivity {
            val fragment = PaymentFragment()
            fragment.arguments = arguments
            it.supportFragmentManager.commitNow {
                add(android.R.id.content, fragment, FRAGMENT_TAG)
                setMaxLifecycle(fragment, Lifecycle.State.INITIALIZED)
            }
        }

        activity?.let {
            val fragment = PaymentFragment()
            fragment.arguments = arguments
            it.supportFragmentManager.commitNow {
                add(android.R.id.content, fragment, FRAGMENT_TAG)
                setMaxLifecycle(fragment, Lifecycle.State.INITIALIZED)
            }
        }
    }

    private fun resumeFragment() {
        activityScenario?.onActivity {
            it.supportFragmentManager.apply {
                val fragment = checkNotNull(findFragmentByTag(FRAGMENT_TAG))
                commitNow {
                    setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
                }
            }
        }
        activity?.let {
            it.supportFragmentManager.apply {
                val fragment = checkNotNull(findFragmentByTag(FRAGMENT_TAG))
                commitNow {
                    setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
                }
            }
        }
    }

    private fun removeFragment() {
        activityScenario?.onActivity {
            it.supportFragmentManager.apply {
                val fragment = checkNotNull(findFragmentByTag(FRAGMENT_TAG))
                commitNow {
                    remove(fragment)
                }
            }
        }
        activity?.let {
            it.supportFragmentManager.apply {
                val fragment = checkNotNull(findFragmentByTag(FRAGMENT_TAG))
                commitNow {
                    remove(fragment)
                }
            }
        }
    }

    /**
     * Check that state is IDLE after PaymentFragment is added
     * but before onCreate.
     */
    @Test
    fun itShouldStartIdle() {
        addFragment()
        val state = getState()
        Assert.assertEquals(PaymentViewModel.State.IDLE, state)
    }

    /**
     * Check that state is IN_PROGRESS after onResume
     */
    @Test
    fun itShouldMoveToInProgressWhenResumed() {
        addFragment()
        resumeFragment()
        val state = getState()
        Assert.assertEquals(PaymentViewModel.State.IN_PROGRESS, state)
    }

    /**
     * Check that state is IDLE after PaymentFragment is removed
     */
    @Test
    fun itShouldMoveToIdleWhenRemoved() {
        addFragment()
        resumeFragment()
        removeFragment()
        val state = getState()
        Assert.assertEquals(PaymentViewModel.State.IDLE, state)
    }

    /**
     * Check that state is IDLE after adding a second PaymentFragment
     * but before onCreate
     */
    @Test
    fun itShouldRemainIdleWhenAddedAgain() {
        

        addFragment()
        resumeFragment()
        removeFragment()
        addFragment()
        val state = getState()
        Assert.assertEquals(PaymentViewModel.State.IDLE, state)
    }

    /**
     * Check that state changes correctly at each step
     * when one PaymentFragment is first added, resumed, and removed;
     * and a second one is then added and resumed.
     */
    @Test
    fun itShouldProcessThroughTheStatesCorrectly() {
        addFragment()
        Assert.assertEquals(PaymentViewModel.State.IDLE, getState())

        resumeFragment()
        Assert.assertEquals(PaymentViewModel.State.IN_PROGRESS, getState())

        removeFragment()
        Assert.assertEquals(PaymentViewModel.State.IDLE, getState())

        addFragment()
        Assert.assertEquals(PaymentViewModel.State.IDLE, getState())

        resumeFragment()
        Assert.assertEquals(PaymentViewModel.State.IN_PROGRESS, getState())
    }
}
