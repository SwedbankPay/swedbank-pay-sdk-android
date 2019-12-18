package com.swedbankpay.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.makeCreator

class NationalIdentifier(
    @SerializedName("socialSecurityNumber") val socialSecurityNumber: String,
    @SerializedName("countryCode") val countryCode: String
) : Parcelable {
    companion object {
        @JvmField val CREATOR = makeCreator(::NationalIdentifier)
    }
    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(socialSecurityNumber)
        parcel.writeString(countryCode)
    }
    private constructor(parcel: Parcel) : this(
        socialSecurityNumber = checkNotNull(parcel.readString()),
        countryCode = checkNotNull(parcel.readString())
    )
}