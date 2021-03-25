package com.swedbankpay.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.JsonAdapter
import com.swedbankpay.mobilesdk.internal.asStringOrNull
import com.swedbankpay.mobilesdk.internal.makeCreator
import java.lang.reflect.Type

/**
 * Describes a terminal error condition signaled by an onError callback from Swedbank Pay.
 *
 * See [https://developer.swedbankpay.com/checkout/other-features#onerror]
 */
@JsonAdapter(TerminalFailure.Deserializer::class)
data class TerminalFailure internal constructor(
    val origin: String?,
    val messageId: String?,
    val details: String?
) : Parcelable {
    private constructor(parcel: Parcel) : this(
        origin = parcel.readString(),
        messageId = parcel.readString(),
        details = parcel.readString()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(origin)
        parcel.writeString(messageId)
        parcel.writeString(details)
    }
    override fun describeContents() = 0
    companion object {
        @Suppress("unused")
        @JvmField val CREATOR = makeCreator(::TerminalFailure)
    }

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