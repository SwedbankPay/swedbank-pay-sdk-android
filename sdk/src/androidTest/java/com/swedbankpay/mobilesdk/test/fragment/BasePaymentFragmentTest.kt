package com.swedbankpay.mobilesdk.test.fragment

import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.web.assertion.WebViewAssertions
import androidx.test.espresso.web.matcher.DomMatchers
import androidx.test.espresso.web.sugar.Web
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.PaymentFragment
import com.swedbankpay.mobilesdk.internal.remote.json.Link
import com.swedbankpay.mobilesdk.test.TestConstants
import com.swedbankpay.mobilesdk.test.mockPaymentOrders
import com.swedbankpay.mobilesdk.test.mockTopLevelResources
import com.swedbankpay.mobilesdk.test.onTopLevelResources
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

/*
These tests can crash the Instrumentation if more that one is run at a time.
The crashes do not seem to produce meaningful error traces, making it difficult
to remedy this. For this reason, they are separated to individual classes
and run with the Android Test Orchestrator
(https://developer.android.com/training/testing/junit-runner#using-android-test-orchestrator)
which clears the application state between each run.
The crashes may be related to memory usage.
 */
@RunWith(AndroidJUnit4::class)
abstract class BasePaymentFragmentTest {
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock
    protected lateinit var configuration: Configuration

    protected lateinit var scenario: FragmentScenario<PaymentFragment>

    protected val args get() = PaymentFragment.ArgumentsBuilder().paymentOrder(TestConstants.paymentOrder).build()

    @Before
    fun setup() {
        configuration.stub {
            on { rootLink } doReturn Link.Root("https://rootlink.invalid".toHttpUrl())
        }

        PaymentFragment.defaultConfiguration = configuration
    }
    @After
    fun teardown() {
        scenario.moveToState(Lifecycle.State.DESTROYED)
        PaymentFragment.defaultConfiguration = null
    }

    protected fun stubAnonymousMockPayment() {
        configuration.stub {
            onTopLevelResources(
                mockTopLevelResources(
                    null,
                    mockPaymentOrders()
                )
            )
        }
    }

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

    protected class HasNoScriptsMatcher : TypeSafeMatcher<Document>() {
        override fun describeTo(description: Description) {
            description.appendText("has no <script> elements")
        }

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