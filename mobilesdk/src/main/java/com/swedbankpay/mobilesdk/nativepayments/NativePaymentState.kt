package com.swedbankpay.mobilesdk.nativepayments

import android.webkit.WebView
import androidx.annotation.Keep
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.ProblemDetails
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.AvailableInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.NativePaymentProblem

/**
 * This class is exposed to the merchant app through live data.
 * Will tell the state of the ongoing payment session
 */
@Keep
sealed class NativePaymentState {

    /**
     * Called when an list of available instruments is known.
     *
     * @param availableInstruments List of different [AvailableInstrument] that can be used fo the payment session
     */
    class AvailableInstrumentsFetched(val availableInstruments: List<AvailableInstrument>) :
        NativePaymentState()

    /**
     * Called when a webview need to be shown to the user. For example 3d-secure
     */
    class LaunchWebView(val webView: WebView) : NativePaymentState()

    object CloseWebView : NativePaymentState()

    /**
     * Called whenever the payment has been completed
     */
    object PaymentComplete : NativePaymentState()

    /**
     * Called whenever the payment has been canceled for any reason
     */
    object PaymentCanceled : NativePaymentState()

    /**
     *  Called if there is a session problem with performing the payment.
     *
     *  @param problem [ProblemDetails] that cause the failure
     */
    class SessionProblemOccurred(val problem: ProblemDetails) : NativePaymentState()

    /**
     *  Called if there is a SDK problem with performing the payment.
     *
     *  @param problem [NativePaymentProblem] that cause the failure
     */
    class SdkProblemOccurred(val problem: NativePaymentProblem) : NativePaymentState()

    /**
     * Default state
     */
    object Idle : NativePaymentState()

}