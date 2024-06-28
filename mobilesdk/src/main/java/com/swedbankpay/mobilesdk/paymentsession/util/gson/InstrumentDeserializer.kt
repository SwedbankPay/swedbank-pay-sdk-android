package com.swedbankpay.mobilesdk.paymentsession.util.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.Instrument
import java.lang.reflect.Type

class InstrumentDeserializer : JsonDeserializer<Instrument> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Instrument {
        return when(val jsonString = json?.asString) {
            "Swish" -> Instrument.Swish(jsonString)
            "CreditCard" -> Instrument.CreditCard(jsonString)
            else -> Instrument.WebBased(jsonString ?: "WebBased")
        }
    }
}