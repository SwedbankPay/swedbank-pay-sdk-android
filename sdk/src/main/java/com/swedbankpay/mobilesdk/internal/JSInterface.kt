package com.swedbankpay.mobilesdk.internal

import android.webkit.JavascriptInterface
import androidx.annotation.AnyThread
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.swedbankpay.mobilesdk.TerminalFailure
import com.swedbankpay.mobilesdk.internal.remote.json.OnPaymentToSEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Suppress("unused")
@AnyThread
internal class JSInterface(var vm: InternalPaymentViewModel?) {
    private fun withVmScope(f: suspend CoroutineScope.() -> Unit) {
        vm?.viewModelScope?.launch(block = f)
    }

    @JavascriptInterface
    fun onConsumerProfileRefAvailable(consumerProfileRef: String) = withVmScope {
        vm?.onConsumerProfileRefAvailable(consumerProfileRef)
    }

    @JavascriptInterface
    fun onIdentifyError(error: String) = withVmScope {
        vm?.onError(Gson().fromJson(error, TerminalFailure::class.java))
    }

    @JavascriptInterface
    fun onPaymentMenuInstrumentSelected(event: String) {
        // TODO: Analytics?
    }

    @JavascriptInterface
    fun onPaymentCreated(event: String) {
        // TODO: Analytics?
    }

    @JavascriptInterface
    fun onPaymentCompleted(event: String) = withVmScope {
        vm?.onPaymentSuccess()

    }

    @JavascriptInterface
    fun onPaymentCanceled(event: String) {
        // TODO: Analytics?
    }

    @JavascriptInterface
    fun onPaymentFailed(event: String) = withVmScope {
        vm?.onPaymentFailed()
    }

    @JavascriptInterface
    fun onPaymentToS(event: String) {
        try {
            Gson().fromJson(event, OnPaymentToSEvent::class.java).openUrl?.let {
                withVmScope {
                    vm?.onPaymentToS(it)
                }
            }
        } catch (_: Exception) {
            return
        }
    }

    @JavascriptInterface
    fun onPaymentError(error: String) = withVmScope {
        vm?.onError(Gson().fromJson(error, TerminalFailure::class.java))
    }
}