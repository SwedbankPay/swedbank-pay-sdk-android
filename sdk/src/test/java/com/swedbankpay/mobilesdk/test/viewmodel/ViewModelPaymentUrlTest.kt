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

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ViewModelPaymentUrlTest : AbstractViewModelTest(), HasDefaultViewModelProviderFactory {
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var configuration: Configuration

    override fun getDefaultViewModelProviderFactory() = ViewModelProvider.AndroidViewModelFactory(application)

    private val viewModel get() = getViewModel<InternalPaymentViewModel>()

    @Before
    fun setup() {
        application.stub {
            on {
                getString(R.string.swedbankpaysdk_view_paymentorder_template,
                    TestConstants.viewPaymentorderLink
                )
            } doReturn TestConstants.paymentorderHtmlPage
        }

        configuration.stub {
            onTopLevelResources(
                mockTopLevelResources(
                    null,
                    mockPaymentOrders()
                )
            )
        }

        viewModel.let {
            it.configuration = configuration
            it.start(
                consumer = null,
                paymentOrder = TestConstants.paymentOrder,
                useBrowser =  false
            )
        }
    }

    @Test
    fun itShouldLoadPaymentMenuHtml() {
        observing(viewModel.currentPage) {
            verify(it).onChanged(TestConstants.paymentorderHtmlPage)
            verifyNoMoreInteractions(it)
        }
    }

    @Test
    fun itShouldReloadPaymentMenuHtmlAfterNavigationToPaymentUrl() {
        observing(viewModel.currentPage) {
            viewModel.overrideNavigation(Uri.parse(TestConstants.paymentUrl))
            verify(
                it,
                times(2)
            ).onChanged(TestConstants.paymentorderHtmlPage)
            verifyNoMoreInteractions(it)
        }
    }

    @Test
    fun itShouldReloadPaymentMenuHtmlAfterCallbackActivityStartedWithPaymentUrl() {
        observing(viewModel.currentPage) {
            val callbackIntent =
                Intent(ApplicationProvider.getApplicationContext(), CallbackActivity::class.java)
                    .setData(Uri.parse(TestConstants.paymentUrl))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ActivityScenario.launch<CallbackActivity>(callbackIntent).use {}
            verify(
                it,
                times(2)
            ).onChanged(TestConstants.paymentorderHtmlPage)
            verifyNoMoreInteractions(it)
        }
    }
}