package com.swedbankpay.mobilesdk.internal

import android.webkit.JavascriptInterface
import androidx.annotation.AnyThread
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.swedbankpay.mobilesdk.internal.remote.json.OnPaymentToSEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Suppress("unused")
@AnyThread
internal class JSInterface(var vm: InternalPaymentViewModel?) {
    private fun withViewModelScope(f: suspend CoroutineScope.() -> Unit) {
        vm?.viewModelScope?.launch(block = f)
    }

    private inline fun withViewModel(crossinline f: InternalPaymentViewModel.() -> Unit) {
        withViewModelScope {
            vm?.f()
        }
    }

    private fun <T> parseEvent(event: String, type: Class<T>): T? {
        return try {
            Gson().fromJson(event, type)
        } catch (_: Exception) {
            null
        }
    }

    private inline fun <reified T> withViewModelAndEvent(event: String, crossinline f: InternalPaymentViewModel.(T?) -> Unit) {
        val t = parseEvent(event, T::class.java)
        withViewModel {
            f(t)
        }
    }

    @JavascriptInterface
    fun onConsumerProfileRefAvailable(consumerProfileRef: String) = withViewModel {
        onConsumerProfileRefAvailable(consumerProfileRef)
    }

    @JavascriptInterface
    fun onIdentifyError(error: String) {
        withViewModelAndEvent(error, InternalPaymentViewModel::onError)
    }

    @JavascriptInterface
    fun onPaymentCompleted() = withViewModel {
        onPaymentSuccess()

    }

    @JavascriptInterface
    fun onPaymentFailed() = withViewModel {
        onPaymentFailed()
    }

    @JavascriptInterface
    fun onPaymentToS(event: String) {
        withViewModelAndEvent<OnPaymentToSEvent>(event) {
            it?.openUrl?.let(::onPaymentToS)
        }
    }

    @JavascriptInterface
    fun onPaymentError(error: String) {
        withViewModelAndEvent(error, InternalPaymentViewModel::onError)
    }
}