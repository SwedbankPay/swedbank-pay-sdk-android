package com.swedbankpay.mobilesdk.nativepayments.exposedmodel

import android.content.Context
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.Instrument

sealed class PaymentAttemptInstrument {

    data class Swish(val msisdn: String? = null,
                     val localStartContext: Context? = null) :
        PaymentAttemptInstrument()

    data class CreditCard(val paymentToken: String? = null) : PaymentAttemptInstrument()

}

fun PaymentAttemptInstrument.toInstrument(): Instrument = when (this) {
    is PaymentAttemptInstrument.CreditCard -> Instrument.CREDIT_CARD
    is PaymentAttemptInstrument.Swish -> Instrument.SWISH
}