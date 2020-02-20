package com.swedbankpay.mobilesdk

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.makeCreator
import com.swedbankpay.mobilesdk.internal.readEnum
import com.swedbankpay.mobilesdk.internal.remote.ExtensibleJsonObject
import com.swedbankpay.mobilesdk.internal.writeEnum

/**
 * A consumer to identify using the [checkin](https://developer.swedbankpay.com/checkout/checkin) flow.
 *
 * Please refer to the
 * [Swedbank Pay documentation](https://developer.swedbankpay.com/checkout/checkin#checkin-back-end)
 * for further information.
 */
data class Consumer(
    /**
     * The operation to perform.
     *
     * At this time, the only operation available is [ConsumerOperation.INITIATE_CONSUMER_SESSION],
     * i.e. <code>"initiate-consumer-session"</code>
     */
    @SerializedName("operation") val operation: ConsumerOperation = ConsumerOperation.INITIATE_CONSUMER_SESSION,
    /**
     * The language to use in the checkin view.
     */
    @SerializedName("language") val language: Language = Language.ENGLISH,
    /**
     * List of ISO-3166 codes of countries the merchant can ship to.
     */
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