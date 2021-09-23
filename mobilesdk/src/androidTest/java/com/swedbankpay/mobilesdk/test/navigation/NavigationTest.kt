package com.swedbankpay.mobilesdk.test.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.PaymentFragment
import com.swedbankpay.mobilesdk.test.*
import org.junit.*
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import kotlin.reflect.KClass

/**
 * Tests for the navigation target to PaymentFragment
 */
class NavigationTest {
    /**
     * STRICT_STUBS mockito rule for cleaner tests
     */
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)
    
    @Mock
    private lateinit var configuration: Configuration

    private lateinit var scenario: FragmentScenario<NavHostFragment>

    /**
     * Create mock Configuration and set it as PaymentFragment.defaultConfiguration;
     * and launch the test scenario.
     */
    @Before
    fun setup() {
        PaymentFragment.defaultConfiguration = configuration

        scenario = launchFragmentInContainer()
        scenario.onFragment {
            it.navController.setGraph(R.navigation.navigation_test)
        }
    }

    /**
     * Destroy scenario and reset PaymentFragment.defaultConfiguration
     */
    @After
    fun teardown() {
        scenario.moveToState(Lifecycle.State.DESTROYED)
        PaymentFragment.defaultConfiguration = null
    }

    private fun navigateToPaymentFragment(arguments: Bundle) {
        scenario.onFragment {
            it.navController.navigate(
                    R.id.start_fragment_to_payment_fragment,
                    arguments
            )
            it.childFragmentManager.executePendingTransactions()
        }
    }

    private fun NavHostFragment.findVisibleFragment(): Fragment? {
        return childFragmentManager.findFragmentById(id)
    }

    /**
     * Check that navigating to the payment fragment destination
     * launches PaymentFragment.
     */
    @Test
    fun itShouldLaunchPaymentFragment() {
        configuration.stub {
            onPostPaymentorders(TestConstants.viewPaymentorderInfo)
        }

        navigateToPaymentFragment(PaymentFragment.ArgumentsBuilder().build())

        var visibleFragmentClass: KClass<out Fragment>? = null
        scenario.onFragment {
            it.findVisibleFragment()?.let {
                visibleFragmentClass = it::class
            }
        }
        Assert.assertEquals(PaymentFragment::class, visibleFragmentClass)

        verifyNoMoreInteractions(configuration)
    }

    /**
     * Check that arguments are passed correctly to PaymentFragment.
     */
    @Test
    fun itShouldLaunchPaymentFragmentWithThePassedArguments() {
        configuration.stub {
            onPostConsumers(TestConstants.viewConsumerIdentificationInfo)
        }

        val arguments = PaymentFragment.ArgumentsBuilder()
            .consumer(TestConstants.consumer)
            .paymentOrder(TestConstants.paymentOrder)
            .userData("userData")
            .style(mapOf("attribute" to "value"))
            .build()

        navigateToPaymentFragment(arguments)

        var paymentFragmentArguments: Bundle? = null
        scenario.onFragment {
            paymentFragmentArguments = it.findVisibleFragment()?.arguments
        }
        Assert.assertTrue(paymentFragmentArguments?.isEqualTo(arguments) == true)

        verifyNoMoreInteractions(configuration)
    }

    /**
     * Check that navigating to the payment fragment destination
     * without arguments throws an exception.
     */
    @Test
    fun itShouldFailWithoutArguments() {
        var launchException: RuntimeException? = null

        scenario.onFragment {
            it.navController.navigate(R.id.start_fragment_to_payment_fragment)
            try {
                it.childFragmentManager.executePendingTransactions()
            } catch (e: RuntimeException) {
                launchException = e
            }
        }

        Assert.assertNotNull(launchException)
    }
}
