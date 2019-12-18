package com.swedbankpay.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.makeCreator

@Suppress("unused")
data class PayeeInfo(
    @SerializedName("payeeId") val payeeId: String = "",
    @SerializedName("payeeReference") val payeeReference: String = "",
    @SerializedName("payeeName") val payeeName: String? = null,
    @SerializedName("productCategory") val productCategory: String? = null,
    @SerializedName("orderReference") val orderReference: String? = null,
    @SerializedName("subsite") val subsite: String? = null
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR = makeCreator(::PayeeInfo)

        private fun String.checkPayeeReference() = checkMaxLength(30, "payeeReference")
        private fun String.checkOrderReference() = checkMaxLength(50, "orderReference")
        private fun String.checkSubsite() = checkMaxLength(40, "subsite")

        private fun String.checkMaxLength(maxLength: Int, fieldName: String) = apply {
            require(length <= maxLength) {
                "$fieldName maximum length ($maxLength) exceeded"
            }
        }
    }

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
            payeeId = payeeId,// checkBuilderNotNull(payeeId, "payeeId"),
            payeeReference = payeeReference,// checkBuilderNotNull(payeeReference, "payeeReference"),
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

    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.apply {
            writeString(payeeId)
            writeString(payeeReference)
            writeString(payeeName)
            writeString(productCategory)
            writeString(orderReference)
            writeString(subsite)
        }
    }
    private constructor(parcel: Parcel) : this(
        payeeId = checkNotNull(parcel.readString()),
        payeeReference = checkNotNull(parcel.readString()),
        payeeName = parcel.readString(),
        productCategory = parcel.readString(),
        orderReference = parcel.readString(),
        subsite = parcel.readString()
    )
}