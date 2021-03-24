package com.swedbankpay.mobilesdk

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Information about the payee (recipient) of a payment order
 */
@Parcelize
data class PayeeInfo(
    /**
     * The unique identifier of this payee set by Swedbank Pay.
     *
     * This is usually the Merchant ID. However, usually best idea to set this value in your backend
     * instead. Thus, this property defaults to the empty string, but it is included in the data
     * model for completeness.
     */
    @SerializedName("payeeId") val payeeId: String = "",
    /**
     * [A unique reference for this operation](https://developer.swedbankpay.com/checkout/other-features#payee-reference).
     *
     * Like [payeeId], this is usually best to set in your backend, and this property thus defaults
     * to the empty string.
     */
    @SerializedName("payeeReference") val payeeReference: String = "",
    /**
     * Name of the payee, usually the name of the merchant.
     */
    @SerializedName("payeeName") val payeeName: String? = null,
    /**
     * A product category or number sent in from the payee/merchant.
     *
     * This is not validated by Swedbank Pay, but will be passed through the payment process and may
     * be used in the settlement process.
     */
    @SerializedName("productCategory") val productCategory: String? = null,
    /**
     * A reference to your own merchant system.
     */
    @SerializedName("orderReference") val orderReference: String? = null,
    /**
     * Used for split settlement.
     */
    @SerializedName("subsite") val subsite: String? = null
) : Parcelable {
    companion object {
        private fun String.checkPayeeReference() = checkMaxLength(30, "payeeReference")
        private fun String.checkOrderReference() = checkMaxLength(50, "orderReference")
        private fun String.checkSubsite() = checkMaxLength(40, "subsite")

        private fun String.checkMaxLength(maxLength: Int, fieldName: String) = apply {
            require(length <= maxLength) {
                "$fieldName maximum length ($maxLength) exceeded"
            }
        }
    }

    @Suppress("unused")
    class Builder {
        private var payeeId = ""
        private var payeeReference = ""
        private var payeeName: String? = null
        private var productCategory: String? = null
        private var orderReference: String? = null
        private var subsite: String? = null

        fun payeeId(payeeId: String) = apply { this.payeeId = payeeId }
        fun payeeReference(payeeReference: String) = apply { this.payeeReference = payeeReference.checkPayeeReference() }
        fun payeeName(payeeName: String?) = apply { this.payeeName = payeeName }
        fun productCategory(productCategory: String?) = apply { this.productCategory = productCategory }
        fun orderReference(orderReference: String?) = apply { this.orderReference = orderReference?.checkOrderReference() }
        fun subsite(subsite: String?) = apply { this.subsite = subsite?.checkSubsite() }

        fun build() = PayeeInfo(
            payeeId = payeeId,
            payeeReference = payeeReference,
            payeeName = payeeName,
            productCategory = productCategory,
            orderReference = orderReference,
            subsite = subsite
        )
    }

    init {
        payeeReference.checkPayeeReference()
        orderReference?.checkOrderReference()
        subsite?.checkSubsite()
    }
}