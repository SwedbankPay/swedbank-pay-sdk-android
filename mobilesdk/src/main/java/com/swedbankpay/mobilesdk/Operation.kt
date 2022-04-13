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

/// Find the first operation by rel-tag.
fun List<Operation>.find(rel: String) = firstOrNull { it.rel == rel }

/**
 * Swedbank Pay Operation. Operations are invoked by making an HTTP request.
 *
 * Please refer to the
 * [Swedbank Pay documentation](https://developer.swedbankpay.com/checkout/other-features#operations).
 */
@JsonAdapter(Operation.Deserializer::class)
@Parcelize
data class Operation(
    /**
     * The purpose of the operation. The exact meaning is dependent on the Operation context.
     */
    val rel: String?,
    /**
     * The request method
     */
    val method: String?,
    /**
     * The request URL
     */
    val href: String?,
    /**
     * The Content-Type of the response
     */
    val contentType: String?
) : Parcelable {
    internal class Deserializer : JsonDeserializer<Operation?> {
        override fun deserialize(
            element: JsonElement,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Operation? {
            if (element.isJsonNull) return null

            val json = try {
                element.asJsonObject
            } catch (e: Exception) {
                throw JsonSyntaxException(e)
            }

            return Operation(
                rel = json["rel"]?.asStringOrNull,
                method = json["method"]?.asStringOrNull,
                href = json["href"]?.asStringOrNull,
                contentType = json["contentType"]?.asStringOrNull
            )
        }
    }
}