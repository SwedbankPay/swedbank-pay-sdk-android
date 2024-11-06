package com.swedbankpay.mobilesdk.paymentsession.exposedmodel

import android.app.Activity
import android.content.Context
import androidx.annotation.Keep
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.Instrument

/**
 * Instrument with needed values to make a payment attempt
 */
@Keep
sealed class PaymentAttemptInstrument(
    internal val context: Context? = null,
    internal val identifier: String
) {
    @Keep
    data class Swish(
        val msisdn: String? = null,
        val localStartContext: Context? = null
    ) : PaymentAttemptInstrument(localStartContext, Swish::class.java.simpleName)

    @Keep
    data class CreditCard(
        val prefill: CreditCardPrefill,
        val localStartContext: Context
    ) : PaymentAttemptInstrument(localStartContext, CreditCard::class.java.simpleName)

    @Keep
    data class NewCreditCard(
        val enabledPaymentDetailsConsentCheckbox: Boolean
    ) : PaymentAttemptInstrument(null, "CreditCard")

    @Keep
    class GooglePay(internal val activity: Activity) :
        PaymentAttemptInstrument(context = activity, identifier = GooglePay::class.java.simpleName)

    @Keep
    class WebBased(
        identifier: String
    ) : PaymentAttemptInstrument(context = null, identifier = identifier)
}

@Keep
fun PaymentAttemptInstrument.toInstrument(): Instrument = when (this) {
    is PaymentAttemptInstrument.CreditCard -> Instrument.CreditCard(this.identifier)
    is PaymentAttemptInstrument.Swish -> Instrument.Swish(this.identifier)
    is PaymentAttemptInstrument.GooglePay -> Instrument.GooglePay(this.identifier)
    else -> Instrument.WebBased(this.identifier)
}

@Keep
fun PaymentAttemptInstrument.isSwishLocalDevice() =
    this is PaymentAttemptInstrument.Swish && this.msisdn == null