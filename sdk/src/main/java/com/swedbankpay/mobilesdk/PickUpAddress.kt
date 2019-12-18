package com.swedbankpay.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.makeCreator

data class PickUpAddress(
    @SerializedName("name") val name: String? = null,
    @SerializedName("streetAddress") val streetAddress: String? = null,
    @SerializedName("coAddress") val coAddress: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("zipCode") val zipCode: String? = null,
    @SerializedName("countryCode") val countryCode: String? = null
) : Parcelable {
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = makeCreator(::PickUpAddress)
    }

    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.apply {
            writeString(name)
            writeString(streetAddress)
            writeString(coAddress)
            writeString(city)
            writeString(zipCode)
            writeString(countryCode)
        }
    }
    constructor(parcel: Parcel) : this(
        name = parcel.readString(),
        streetAddress = parcel.readString(),
        coAddress = parcel.readString(),
        city = parcel.readString(),
        zipCode = parcel.readString(),
        countryCode = parcel.readString()
    )
}