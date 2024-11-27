package com.swedbankpay.mobilesdk.paymentsession.util.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PaymentMethod
import java.lang.reflect.Type

class InstrumentDeserializer : JsonDeserializer<PaymentMethod> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PaymentMethod {
        return when (val jsonString = json?.asString) {
            "Swish" -> PaymentMethod.Swish(jsonString)
            "CreditCard" -> PaymentMethod.CreditCard(jsonString)
            "GooglePay" -> PaymentMethod.GooglePay(jsonString)
            else -> PaymentMethod.WebBased(jsonString ?: "WebBased")
        }
    }
}