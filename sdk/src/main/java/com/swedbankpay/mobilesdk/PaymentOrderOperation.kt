package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

/**
 * Type of operation the payment order performs
 */
@Suppress("unused")
enum class PaymentOrderOperation {
    /**
     * A purchase, i.e. a single payment
     */
    @SerializedName("Purchase") PURCHASE,

    /**
     * Pre-verification of a payment method. This operation will not charge the payment method,
     * but it can create a token for future payments.
     *
     * See [PaymentOrder.generateRecurrenceToken], [PaymentOrder.generatePaymentToken]
     */
    @SerializedName("Verify") VERIFY
}