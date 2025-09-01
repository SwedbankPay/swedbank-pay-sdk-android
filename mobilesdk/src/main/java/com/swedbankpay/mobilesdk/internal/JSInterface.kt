package com.swedbankpay.mobilesdk.internal

import android.webkit.JavascriptInterface
import androidx.annotation.AnyThread
import androidx.lifecycle.viewModelScope
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
    fun onPaymentError(error: String) {
        withViewModelAndEvent(error, InternalPaymentViewModel::onError)
    }
    
    @JavascriptInterface
    fun onPaid(message: String) = withViewModel {
        onPaid(message)
    }

    @JavascriptInterface
    fun onGeneralEvent(message: String) = withViewModel {
        onGeneralEvent(message)
    }

    @JavascriptInterface
    fun onLaunchNativeGooglePay(payload:String) = withViewModel {
        onLaunchNativeGooglePay(payload)
    }

}