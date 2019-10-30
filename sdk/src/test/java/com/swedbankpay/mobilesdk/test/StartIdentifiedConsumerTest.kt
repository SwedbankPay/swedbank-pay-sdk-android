package com.swedbankpay.mobilesdk.test

import android.app.Application
import android.os.Looper.getMainLooper
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.internal.InternalPaymentViewModel
import com.swedbankpay.mobilesdk.internal.remote.Api
import com.swedbankpay.mobilesdk.internal.remote.json.TopLevelResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class StartIdentifiedConsumerTest {
    val app = ApplicationProvider.getApplicationContext<Application>()

    private var mockServer: MockWebServer? = null

    @Before
    fun startServer() {
        val mockServer = MockWebServer()
        this.mockServer = mockServer

        TopLevelResources.purge()
        mockServer.enqueue(MockResponses.getRoot)
        mockServer.enqueue(MockResponses.postConsumers)

        mockServer.start(0)
        val url = mockServer.url("/").toString()
        PayEx.configure(Configuration.Builder(url).build())
        Api.skipProviderInstallerForTests()
    }

    @After
    fun shutdownServer() {
        mockServer?.shutdown()
        mockServer = null
    }

    @Test
    fun itShouldShowHtmlPage() = testSuspend {
        val vm = InternalPaymentViewModel(app)
        vm.startIdentifiedCustomer("idData", "merchantData")
        vm.waitForNonTransientState()
        var html: String? = null
        val observer = Observer<String?> {
            if (it != null) {
                html = it
            }
        }
        vm.currentPage.observeForever(observer)
        vm.currentPage.removeObserver(observer)

        Assert.assertNotNull(html)
    }

    private fun testSuspend(timeout: Long = 5_000L, f: suspend CoroutineScope.() -> Unit) {
        val looper = shadowOf(getMainLooper())
        looper.pause()
        var result: Any? = null
        MainScope().launch {
            result = try {
                f()
                Unit
            } catch (t: Throwable) {
                t
            }
        }
        val expirationTime = System.nanoTime() + timeout * 1_000_000L
        looper.runToEndOfTasks()
        while (result == null && System.nanoTime() < expirationTime) {
            Thread.sleep(100L)
            looper.runToEndOfTasks()
        }
        Assert.assertNotNull("Coroutine still active; increasing the timeout may help.", result)
        (result as? Throwable)?.let {
            throw it
        }
    }

    @Test
    fun sanityCheck() = testSuspend {
        val child = launch(Dispatchers.IO) {
            Thread.sleep(1000L)
        }
        child.join()
        Assert.assertTrue(child.isCompleted)
    }
}