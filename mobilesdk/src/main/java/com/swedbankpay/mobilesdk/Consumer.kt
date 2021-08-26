package com.swedbankpay.mobilesdk

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * A consumer to identify using the [checkin](https://developer.swedbankpay.com/checkout/checkin) flow.
 *
 * Please refer to the
 * [Swedbank Pay documentation](https://developer.swedbankpay.com/checkout/checkin#checkin-back-end)
 * for further information.
 */
@Parcelize
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
) : Parcelable