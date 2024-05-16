package com.swedbankpay.mobilesdk.nativepayments.util.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.CreditCardPrefillModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.PrefillBaseModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.SwishPrefillModel
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

        if (swishPrefill != null) {
            return context?.deserialize(jsonObject, SwishPrefillModel::class.java)
        }

        if (creditCardPrefill != null) {
            return context?.deserialize(jsonObject, CreditCardPrefillModel::class.java)
        }

        return null
    }
}