package com.swedbankpay.mobilesdk.nativepayments.exposedmodel

import com.swedbankpay.mobilesdk.nativepayments.model.response.Instrument

sealed class PaymentAttemptInstrument {

    data class Swish(val msisdn: String? = null) : PaymentAttemptInstrument()
    data class CreditCard(val paymentToken: String? = null) : PaymentAttemptInstrument()

}

fun PaymentAttemptInstrument.toInstrument() : Instrument = when(this) {
    is PaymentAttemptInstrument.CreditCard -> Instrument.CREDIT_CARD
    is PaymentAttemptInstrument.Swish -> Instrument.SWISH
}