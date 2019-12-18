package com.swedbankpay.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.makeCreator

data class PaymentOrderPayer(
    @SerializedName("consumerProfileRef") val consumerProfileRef: String
) : Parcelable {
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = makeCreator(::PaymentOrderPayer)
    }

    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(consumerProfileRef)
    }
    private constructor(parcel: Parcel) : this(
        consumerProfileRef = checkNotNull(parcel.readString())
    )
}