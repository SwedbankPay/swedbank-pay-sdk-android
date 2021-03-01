package com.swedbankpay.mobilesdk.merchantbackend

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.swedbankpay.mobilesdk.internal.asStringOrNull
import com.swedbankpay.mobilesdk.internal.remote.json.Link
import java.lang.reflect.Type

@JsonAdapter(PaymentTokenInfo.Deserializer::class)
data class PaymentTokenInfo internal constructor(
    /**
     * The actual paymentToken
     */
    val paymentToken: String,
    /**
     * Payment instrument type of this token
     */
    val instrument: String?,
    /**
     * User-friendly name of the payment instrument
     */
    val instrumentDisplayName: String?,
    /**
     * Instrument-specific parameters.
     */
    val instrumentParameters: Map<String, String>?,
    /**
     * Operations you can perform on this token.
     * Note that you generally cannot call these from your mobile app.
     */
    val operations: List<Operation>,

    internal val mobileSDK: MobileSDK?
) {
    internal class MobileSDK {
        @SerializedName("delete")
        internal var delete: Link.DeletePaymentToken? = null
    }

    internal class Deserializer : JsonDeserializer<PaymentTokenInfo?> {
        override fun deserialize(
            element: JsonElement,
            typeOfT: Type?,
            context: JsonDeserializationContext
        ): PaymentTokenInfo? {
            if (element.isJsonNull) return null

            try {
                val json = element.asJsonObject
                return PaymentTokenInfo(
                    paymentToken = json["paymentToken"].asString,
                    instrument = json["instrument"]?.asStringOrNull,
                    instrumentDisplayName = json["instrumentDisplayName"]?.asStringOrNull,
                    instrumentParameters = json["instrumentParameters"]?.let {
                        context.deserialize(it, (object : TypeToken<Map<String, String>>() {}).type)
                    },
                    operations = json["operations"]?.let {
                        context.deserialize(it, (object : TypeToken<List<Operation>>() {}).type)
                    } ?: emptyList(),
                    mobileSDK = json["mobileSDK"]?.let {
                        context.deserialize(it, MobileSDK::class.java)
                    }
                )
            } catch (e: Exception) {
                throw JsonSyntaxException(e)
            }
        }
    }
}