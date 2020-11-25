package com.swedbankpay.mobilesdk.merchantbackend

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.JsonAdapter
import com.swedbankpay.mobilesdk.internal.asStringOrNull
import java.lang.reflect.Type

@JsonAdapter(Operation.Deserializer::class)
data class Operation(
    val rel: String?,
    val href: String?
) {
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
                href = json["href"]?.asStringOrNull
            )
        }
    }
}