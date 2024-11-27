package com.swedbankpay.mobilesdk.paymentsession.exposedmodel

import android.app.Activity
import android.content.Context
import androidx.annotation.Keep
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PaymentMethod

/**
 * Instrument with needed values to make a payment attempt
 */
@Keep
sealed class PaymentAttemptInstrument(
    internal val context: Context? = null,
    internal val paymentMethod: String
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
        PaymentAttemptInstrument(
            context = activity,
            paymentMethod = GooglePay::class.java.simpleName
        )
}

@Keep
fun PaymentAttemptInstrument.instrumentModeRequired() = when (this) {
    is PaymentAttemptInstrument.NewCreditCard -> true
    else -> false
}

@Keep
fun PaymentAttemptInstrument.toInstrument(): PaymentMethod = when (this) {
    is PaymentAttemptInstrument.CreditCard -> PaymentMethod.CreditCard(this.paymentMethod)
    is PaymentAttemptInstrument.Swish -> PaymentMethod.Swish(this.paymentMethod)
    is PaymentAttemptInstrument.GooglePay -> PaymentMethod.GooglePay(this.paymentMethod)
    else -> PaymentMethod.WebBased(this.paymentMethod)
}

@Keep
fun PaymentAttemptInstrument.isSwishLocalDevice() =
    this is PaymentAttemptInstrument.Swish && this.msisdn == null