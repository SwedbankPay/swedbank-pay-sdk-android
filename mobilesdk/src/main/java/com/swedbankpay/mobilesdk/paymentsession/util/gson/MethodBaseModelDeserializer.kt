package com.swedbankpay.mobilesdk.paymentsession.util.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.swedbankpay.mobilesdk.internal.asStringOrNull
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.CreditCardMethodModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.Instrument
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.MethodBaseModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.SwishMethodModel
import java.lang.reflect.Type


internal class MethodBaseModelDeserializer : JsonDeserializer<MethodBaseModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MethodBaseModel? {

        val jsonObject = json?.asJsonObject

        val instrument = jsonObject?.get("instrument")

        return when (instrument?.asStringOrNull) {
            Instrument.SWISH.rawValue -> context?.deserialize(
                jsonObject,
                SwishMethodModel::class.java
            )

            Instrument.CREDIT_CARD.rawValue -> context?.deserialize(
                jsonObject,
                CreditCardMethodModel::class.java
            )

            else -> null
        }
    }
}