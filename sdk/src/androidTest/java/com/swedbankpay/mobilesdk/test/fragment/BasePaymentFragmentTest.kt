package com.swedbankpay.mobilesdk.test.fragment

import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.web.assertion.WebViewAssertions
import androidx.test.espresso.web.matcher.DomMatchers
import androidx.test.espresso.web.sugar.Web
import com.nhaarman.mockitokotlin2.stub
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.PaymentFragment
import com.swedbankpay.mobilesdk.test.TestConstants
import com.swedbankpay.mobilesdk.test.onPostPaymentorders
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

/**
 * Base class for instrumented PaymentFragment tests
 *
 * This class takes care of destroying the FragmentScenario used in the test after the test is done.
 *
 * UNDER SOME CONDITIONS, these tests have a tendency to crash the instrumentation when they fail.
 * This appears to happen when running in a 32-bit x86 emulator. This was not figured out for some
 * time, and as an artifact of that history, these tests were factored out to separate classes
 * allowing the use of the Android Test Orchestrator
 * (https://developer.android.com/training/testing/junit-runner#using-android-test-orchestrator)
 * to run them as separate processes and clear the test app state between each run.
 * Now that we seem to have figured this out, the tests have been reverted to use the regular
 * runner (this is much faster, for one thing), but the factoring-out to subclasses has been left,
 * as it really poses no harm.
 *
 * To anyone running these tests on your own machine: use an x86_64 emulator. Do note that
 * the AVD Manager will not list 64-bit images in the "Recommended" list.
 */
abstract class BasePaymentFragmentTest {
    /**
     * STRICT_STUBS mockito rule for cleaner tests
     */
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    /**
     * The mock Configuration used in the test scenario.
     * Each test case should stub this according to its own needs.
     */
    @Mock
    protected lateinit var configuration: Configuration

    /**
     * The FragmentScenario used in the test
     */
    protected lateinit var scenario: FragmentScenario<PaymentFragment>

    /**
     * A default set of arguments for PaymentFragment test scenarios
     */
    protected val args get() = PaymentFragment.ArgumentsBuilder().paymentOrder(TestConstants.paymentOrder).build()

    /**
     * Create mock Configuration and set it as PaymentFragment.defaultConfiguration
     */
    @Before
    fun setup() {
        PaymentFragment.defaultConfiguration = configuration
    }

    /**
     * Destroy scenario and reset PaymentFragment.defaultConfiguration
     */
    @After
    fun teardown() {
        scenario.moveToState(Lifecycle.State.DESTROYED)
        PaymentFragment.defaultConfiguration = null
    }

    /**
     * Stub the mock Configuration to successfully start an anonymous payment
     */
    protected fun stubAnonymousMockPayment() {
        configuration.stub {
            onPostPaymentorders(TestConstants.viewPaymentorderInfo)
        }
    }

    /**
     * Check that the WebView is showing the correct html content for viewing paymentorder
     */
    protected fun <T> Web.WebInteraction<T>.checkIsShowingPaymentOrder() = this
        .check(
            WebViewAssertions.webContent(
            DomMatchers.elementByXPath("//script[1]", AttributeMatcher("src", Matchers.equalTo(TestConstants.viewPaymentorderLink)))
        ))
        .check(
            WebViewAssertions.webContent(
            DomMatchers.elementByXPath("//script[2]", DomMatchers.withTextContent(Matchers.containsString("payex.hostedView.paymentMenu(")))
        ))

    private class AttributeMatcher(
        private val name: String,
        private val valueMatcher: Matcher<String>
    ) : TypeSafeMatcher<Element>() {

        override fun describeTo(description: Description) {
            description.appendText("with attribute $name: ")
            valueMatcher.describeTo(description)
        }

        override fun matchesSafely(item: Element) =
            valueMatcher.matches(item.getAttribute(name))

    }

    /**
     * Matcher that accepts html documents containing no script tags
     */
    protected class HasNoScriptsMatcher : TypeSafeMatcher<Document>() {
        /**
         * See [Matcher.describeTo]
         */
        override fun describeTo(description: Description) {
            description.appendText("has no <script> elements")
        }

        /**
         * See [TypeSafeMatcher.matchesSafely]
         */
        override fun matchesSafely(item: Document): Boolean {
            val xPath = XPathFactory.newInstance().newXPath()
            val expr = xPath.compile("//script")
            val scriptElements = expr.evaluate(
                item,
                XPathConstants.NODESET
            ) as NodeList
            return scriptElements.length == 0
        }
    }
}