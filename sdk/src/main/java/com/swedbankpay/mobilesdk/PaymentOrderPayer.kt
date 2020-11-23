package com.swedbankpay.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.makeCreator

data class PaymentOrderPayer(
    @SerializedName("consumerProfileRef") val consumerProfileRef: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("msisdn") val msisdn: String? = null,
    @SerializedName("payerReference") val payerReference: String? = null
) : Parcelable {
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = makeCreator(::PaymentOrderPayer)
    }

    @Suppress("unused")
    class Builder {
        private var consumerProfileRef: String? = null
        private var email: String? = null
        private var msisdn: String? = null
        private var payerReference: String? = null

        fun consumerProfileRef(consumerProfileRef: String?) = apply { this.consumerProfileRef = consumerProfileRef }
        fun email(email: String?) = apply { this.email = email }
        fun msisdn(msisdn: String?) = apply { this.msisdn = msisdn }
        fun payerReference(payerReference: String?) = apply { this.payerReference = payerReference}

        fun build() = PaymentOrderPayer(
            consumerProfileRef = consumerProfileRef,
            email = email,
            msisdn = msisdn,
            payerReference = payerReference
        )
    }

    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(consumerProfileRef)
        parcel.writeString(email)
        parcel.writeString(msisdn)
    }
    private constructor(parcel: Parcel) : this(
        consumerProfileRef = parcel.readString(),
        email = parcel.readString(),
        msisdn = parcel.readString()
    )
}