package com.swedbankpay.mobilesdk

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Information about the payer of a payment order
 */
@Parcelize
data class PaymentOrderPayer(
    /**
     * A consumer profile reference obtained through the Checkin flow.
     *
     * If you have your [PaymentFragment] to do the Checkin flow (see
     * [PaymentFragment.ArgumentsBuilder.consumer] and
     * [PaymentFragment.ArgumentsBuilder.useCheckin]), your [Configuration.postPaymentorders]
     * will be called with the `consumerProfileRef` received from the Checkin flow. Your
     * `Configuration` can then use that value here to forward it to your backend for
     * payment order creation.
     */
    @SerializedName("consumerProfileRef") val consumerProfileRef: String? = null,

    /**
     * The email address of the payer.
     *
     * Can be used even if you do not set a [consumerProfileRef]; will be used to prefill
     * appropriate fields.
     */
    @SerializedName("email") val email: String? = null,

    /**
     * The phone number of the payer.
     *
     * Can be used even if you do not set a [consumerProfileRef]; will be used to prefill
     * appropriate fields.
     */
    @SerializedName("msisdn") val msisdn: String? = null,

    /**
     * An opaque, unique reference to the payer. Alternative to the other fields.
     *
     * Using `payerReference` is required when generating or using payment tokens (N.B! not
     * recurrence tokens).
     *
     * The `payerReference` must be unique to a payer, and your backend must have access control
     * such that is ensures that the `payerReference` is owned by the authenticated user.
     * It is usually best to only populate this field in the backend.
     */
    @SerializedName("payerReference") val payerReference: String? = null,

    /**
     * Using the enterprise implementation a merchant can supply SSN to identify a payer which will allow for one-click payments, a more frictionless payment process.
     */
    @SerializedName("nationalIdentifier") val nationalIdentifier: PayerNationalIdentifier? = null,
    
) : Parcelable {
    @Suppress("unused")
    class Builder {
        private var consumerProfileRef: String? = null
        private var email: String? = null
        private var msisdn: String? = null
        private var payerReference: String? = null
        private var nationalIdentifier: PayerNationalIdentifier? = null

        fun consumerProfileRef(consumerProfileRef: String?) = apply { this.consumerProfileRef = consumerProfileRef }
        fun email(email: String?) = apply { this.email = email }
        fun msisdn(msisdn: String?) = apply { this.msisdn = msisdn }
        fun payerReference(payerReference: String?) = apply { this.payerReference = payerReference}
        fun nationalIdentifier(nationalIdentifier: PayerNationalIdentifier?) = apply { this.nationalIdentifier = nationalIdentifier }
        
        fun build() = PaymentOrderPayer(
            consumerProfileRef = consumerProfileRef,
            email = email,
            msisdn = msisdn,
            payerReference = payerReference,
            nationalIdentifier = nationalIdentifier
        )
    }
}
