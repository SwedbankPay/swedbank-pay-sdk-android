package com.swedbankpay.mobilesdk.paymentsession.exposedmodel

import android.content.Context
import androidx.annotation.Keep
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.Instrument

/**
 * Instrument with needed values to make a payment attempt
 */
@Keep
sealed class PaymentAttemptInstrument(
    internal val context: Context? = null,
    internal val name: String
) {
    @Keep
    data class Swish(
        val msisdn: String? = null,
        val localStartContext: Context? = null
    ) : PaymentAttemptInstrument(localStartContext, Instrument.SWISH.rawValue) {
        override val rawValue: String
            get() = "Swish"
    }

    @Keep
    data class CreditCard(
        val prefill: CreditCardPrefill,
        val localStartContext: Context
    ) : PaymentAttemptInstrument(localStartContext, Instrument.CREDIT_CARD.rawValue) {
        override val rawValue: String
            get() = "CreditCard"
    }

    abstract val rawValue: String

}

@Keep
fun PaymentAttemptInstrument.toInstrument(): Instrument = when (this) {
    is PaymentAttemptInstrument.CreditCard -> Instrument.CREDIT_CARD
    is PaymentAttemptInstrument.Swish -> Instrument.SWISH
}