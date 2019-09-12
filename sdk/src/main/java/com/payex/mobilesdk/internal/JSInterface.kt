package com.payex.mobilesdk.internal

import android.util.Log
import android.webkit.JavascriptInterface
import androidx.annotation.AnyThread
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.payex.mobilesdk.TerminalFailure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
        Log.d(LOG_TAG, "onPaymentMenuInstrumentSelected")
    }

    @JavascriptInterface
    fun onPaymentCreated(event: String) {
        Log.d(LOG_TAG, "onPaymentCreated")
    }

    @JavascriptInterface
    fun onPaymentCompleted(event: String) = withVmScope {
        Log.d(LOG_TAG, "onPaymentCompleted")
        vm?.onPaymentSuccess()

    }

    @JavascriptInterface
    fun onPaymentCanceled(event: String) {
        Log.d(LOG_TAG, "onPaymentCanceled")
    }

    @JavascriptInterface
    fun onPaymentFailed(event: String) = withVmScope {
        Log.d(LOG_TAG, "onPaymentFailed")
        vm?.onPaymentFailed()
    }

    @JavascriptInterface
    fun onPaymentToS(event: String) {
        Log.d(LOG_TAG, "onPaymentToS")
    }

    @JavascriptInterface
    fun onPaymentError(error: String) = withVmScope {
        Log.d(LOG_TAG, "onPaymentError")
        vm?.onError(Gson().fromJson(error, TerminalFailure::class.java))
    }
}