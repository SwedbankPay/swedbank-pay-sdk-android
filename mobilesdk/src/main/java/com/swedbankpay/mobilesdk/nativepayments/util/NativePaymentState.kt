package com.swedbankpay.mobilesdk.nativepayments.util

import android.net.Uri
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.AvailableInstrument

/**
 * This class is exposed to the merchant app through live data.
 * Will tell what the next step is
 */
sealed class NativePaymentState {

    /**
     * [AvailablePaymentMethods] contains available payment methods like swish or credit card
     */
    class AvailablePaymentMethods(val availableInstruments: List<AvailableInstrument>) :
        NativePaymentState()

    /**
     * [LaunchSwish] contains the uri which swish is supposed to launch with
     */
    class LaunchSwish(val uri: Uri) : NativePaymentState()

    object PaymentComplete : NativePaymentState()

    class NativeProblem(
        val title: String,
        val status: Int,
        val detail: String
    ) : NativePaymentState()

    /**
     * [Error] contains what went wrong
     * */
    class Error(val message: String) : NativePaymentState()
}