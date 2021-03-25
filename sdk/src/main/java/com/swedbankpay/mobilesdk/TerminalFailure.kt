package com.swedbankpay.mobilesdk

import android.os.Parcelable
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.JsonAdapter
import com.swedbankpay.mobilesdk.internal.asStringOrNull
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Type

/**
 * Describes a terminal error condition signaled by an onError callback from Swedbank Pay.
 *
 * See [https://developer.swedbankpay.com/checkout/other-features#onerror]
 */
@JsonAdapter(TerminalFailure.Deserializer::class)
@Parcelize
data class TerminalFailure internal constructor(
    /**
     * `"consumer"`, `"paymentmenu"`, `"creditcard"`, identifies the system that originated the error.
     */
    val origin: String?,
    /**
     * A unique identifier for the message.
     */
    val messageId: String?,
    /**
     * A human readable and descriptive text of the error.
     */
    val details: String?
) : Parcelable {
    internal class Deserializer : JsonDeserializer<TerminalFailure?> {
        override fun deserialize(
            element: JsonElement,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): TerminalFailure? {
            if (element.isJsonNull) return null

            val json = try {
                element.asJsonObject
            } catch (e: Exception) {
                throw JsonSyntaxException(e)
            }

            return TerminalFailure(
                origin = json["origin"]?.asStringOrNull,
                messageId = json["messageId"]?.asStringOrNull,
                details = json["details"]?.asStringOrNull
            )
        }
    }
}