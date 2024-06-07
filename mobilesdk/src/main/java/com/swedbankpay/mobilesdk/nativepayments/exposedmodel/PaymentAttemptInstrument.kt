package com.swedbankpay.mobilesdk.nativepayments.exposedmodel

import android.content.Context
import androidx.annotation.Keep
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.Instrument

/**
 * Instrument with needed values to make a payment attempt
 */
@Keep
sealed class PaymentAttemptInstrument {
    @Keep
    data class Swish(
        val msisdn: String? = null,
        val localStartContext: Context? = null
    ) : PaymentAttemptInstrument()

    @Keep
    data class CreditCard(val prefill: CreditCardPrefill) : PaymentAttemptInstrument()

}

@Keep
fun PaymentAttemptInstrument.toInstrument(): Instrument = when (this) {
    is PaymentAttemptInstrument.CreditCard -> Instrument.CREDIT_CARD
    is PaymentAttemptInstrument.Swish -> Instrument.SWISH
}