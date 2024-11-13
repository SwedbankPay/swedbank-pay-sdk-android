package com.swedbankpay.mobilesdk.paymentsession.util.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.swedbankpay.mobilesdk.internal.asStringOrNull
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.CreditCardMethodModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.GooglePayMethodModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.MethodBaseModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.SwishMethodModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.WebBasedMethodModel
import java.lang.reflect.Type


internal class MethodBaseModelDeserializer : JsonDeserializer<MethodBaseModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MethodBaseModel? {

        val jsonObject = json?.asJsonObject

        val paymentMethod = jsonObject?.get("paymentMethod")

        return when (paymentMethod?.asStringOrNull) {
            "Swish" -> context?.deserialize(
                jsonObject,
                SwishMethodModel::class.java
            )

            "CreditCard" -> context?.deserialize(
                jsonObject,
                CreditCardMethodModel::class.java
            )

            "GooglePay" -> context?.deserialize(
                jsonObject,
                GooglePayMethodModel::class.java
            )

            else -> context?.deserialize(jsonObject, WebBasedMethodModel::class.java)
        }
    }
}