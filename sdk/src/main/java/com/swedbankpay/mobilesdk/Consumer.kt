package com.swedbankpay.mobilesdk

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.makeCreator
import com.swedbankpay.mobilesdk.internal.readEnum
import com.swedbankpay.mobilesdk.internal.remote.ExtensibleJsonObject
import com.swedbankpay.mobilesdk.internal.writeEnum

data class Consumer(
    @SerializedName("operation") val operation: ConsumerOperation = ConsumerOperation.INITIATE_CONSUMER_SESSION,
    @SerializedName("language") val language: Language = Language.ENGLISH,
    @SerializedName("shippingAddressRestrictedToCountryCodes") val shippingAddressRestrictedToCountryCodes: List<String>,

    /** @hide */
    @Transient override val extensionProperties: Bundle? = null
) : Parcelable, ExtensibleJsonObject {
    companion object {
        @JvmField val CREATOR = makeCreator(::Consumer)
    }

    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.apply {
            writeEnum(operation)
            writeEnum(language)
            writeStringList(shippingAddressRestrictedToCountryCodes)
            writeBundle(extensionProperties)
        }
    }
    private constructor(parcel: Parcel) : this(
        operation = checkNotNull(parcel.readEnum<ConsumerOperation>()),
        language = checkNotNull(parcel.readEnum<Language>()),
        shippingAddressRestrictedToCountryCodes = checkNotNull(parcel.createStringArrayList()),
        extensionProperties = parcel.readBundle(Consumer::class.java.classLoader)
    )
}