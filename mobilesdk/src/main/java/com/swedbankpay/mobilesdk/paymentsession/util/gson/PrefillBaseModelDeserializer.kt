package com.swedbankpay.mobilesdk.paymentsession.util.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.CreditCardMethodPrefillModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PrefillBaseModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.SwishMethodPrefillModel
import java.lang.reflect.Type

internal class PrefillBaseModelDeserializer : JsonDeserializer<PrefillBaseModel> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PrefillBaseModel? {
        val jsonObject = json?.asJsonObject

        val swishPrefill = jsonObject?.get("msisdn")
        val creditCardPrefill = jsonObject?.get("paymentToken")

        return when {
            swishPrefill != null -> context?.deserialize(
                jsonObject,
                SwishMethodPrefillModel::class.java
            )

            creditCardPrefill != null -> context?.deserialize(
                jsonObject,
                CreditCardMethodPrefillModel::class.java
            )

            else -> null
        }
    }
}