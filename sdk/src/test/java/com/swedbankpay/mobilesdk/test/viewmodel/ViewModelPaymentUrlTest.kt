package com.swedbankpay.mobilesdk.test.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.R
import com.swedbankpay.mobilesdk.internal.CallbackActivity
import com.swedbankpay.mobilesdk.internal.InternalPaymentViewModel
import com.swedbankpay.mobilesdk.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import org.robolectric.annotation.Config

/**
 * Tests for PaymentViewModel and InternalPaymentViewModel: paymentUrl handling
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ViewModelPaymentUrlTest : AbstractViewModelTest(), HasDefaultViewModelProviderFactory {
    /**
     * STRICT_STUBS mockito rule for cleaner tests
     */
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var configuration: Configuration

    /**
     * Use AndroidViewModelFactory when creating ViewModels for this test
     */
    override fun getDefaultViewModelProviderFactory() = ViewModelProvider.AndroidViewModelFactory(application)

    private val viewModel get() = getViewModel<InternalPaymentViewModel>()

    /**
     * Set up viewmodels and mock paymentorder
     */
    @Before
    fun setup() {
        application.stub {
            on {
                getString(R.string.swedbankpaysdk_view_paymentorder_template,
                    TestConstants.viewPaymentorderLink,
                    null
                )
            } doReturn TestConstants.paymentorderHtmlPage
        }

        configuration.stub {
            onPostPaymentorders(TestConstants.viewPaymentorderInfo)
        }

        viewModel.let {
            it.configuration = configuration
            it.start(
                useCheckin = false,
                consumer = null,
                paymentOrder = TestConstants.paymentOrder,
                userData = null,
                useBrowser =  false,
                style = null
            )
        }
    }

    /**
     * Check that viewmodel loads the paymentorder html content when the mock payment is started
     */
    @Test
    fun itShouldLoadPaymentMenuHtml() {
        observing(viewModel.currentHtmlContent) {
            verify(it).onChanged(argThat {
                getWebViewPage(application) == TestConstants.paymentorderHtmlPage
            })
            verifyNoMoreInteractions(it)
        }
    }

    /**
     * Check that viewmodel reloads the paymentorder html content after processing a navigation to paymentUrl
     */
    @Test
    fun itShouldReloadPaymentMenuHtmlAfterNavigationToPaymentUrl() {
        observing(viewModel.currentHtmlContent) {
            viewModel.overrideNavigation(Uri.parse(TestConstants.paymentUrl))
            verify(
                it,
                times(2)
            ).onChanged(argThat {
                getWebViewPage(application) == TestConstants.paymentorderHtmlPage
            })
            verifyNoMoreInteractions(it)
        }
    }

    /**
     * Check that viewmodel reloads the paymentorder html content after CallbackActivity is started with paymentUrl
     */
    @Test
    fun itShouldReloadPaymentMenuHtmlAfterCallbackActivityStartedWithPaymentUrl() {
        observing(viewModel.currentHtmlContent) {
            val callbackIntent =
                Intent(ApplicationProvider.getApplicationContext(), CallbackActivity::class.java)
                    .setData(Uri.parse(TestConstants.paymentUrl))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ActivityScenario.launch<CallbackActivity>(callbackIntent).use {}
            verify(
                it,
                times(2)
            ).onChanged(argThat {
                getWebViewPage(application) == TestConstants.paymentorderHtmlPage
            })
            verifyNoMoreInteractions(it)
        }
    }
}