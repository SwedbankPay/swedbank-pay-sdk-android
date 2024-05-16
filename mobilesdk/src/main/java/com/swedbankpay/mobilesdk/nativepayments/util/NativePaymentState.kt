package com.swedbankpay.mobilesdk.nativepayments.util

import android.net.Uri
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.AvailableInstrument

/**
 * This class is exposed to the merchant app through live data.
 * Will tell what the next step is
 */
sealed class NativePaymentState {

    /**
     * [AvailableInstrumentsFetched] contains available payment instruments like swish or credit card
     */
    class AvailableInstrumentsFetched(val availableInstruments: List<AvailableInstrument>) :
        NativePaymentState()

    /**
     * [LaunchClientApp] contains the uri which is supposed to open another app by the merchant
     */
    class LaunchClientApp(val uri: Uri) : NativePaymentState()

    object PaymentSucceeded : NativePaymentState()

    /**
     * [PaymentFailed] contains an error from [Problem] node
     */
    class PaymentFailed(
        val title: String,
        val status: Int,
        val detail: String
    ) : NativePaymentState()

    /**
     * [Error] contains an unexpected error
     * */
    class Error(val message: String) : NativePaymentState()
}