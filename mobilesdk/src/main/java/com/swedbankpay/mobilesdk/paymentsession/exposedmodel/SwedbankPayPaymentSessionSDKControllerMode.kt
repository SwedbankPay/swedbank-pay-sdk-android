package com.swedbankpay.mobilesdk.paymentsession.exposedmodel

import androidx.annotation.Keep

@Keep
sealed class SwedbankPayPaymentSessionSDKControllerMode {

    data class Menu(val restrictedToInstruments: List<AvailableInstrument>?) :
        SwedbankPayPaymentSessionSDKControllerMode()

    data class InstrumentMode(val instrument: AvailableInstrument) :
        SwedbankPayPaymentSessionSDKControllerMode()
}