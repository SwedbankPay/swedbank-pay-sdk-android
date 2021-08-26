package com.swedbankpay.mobilesdk.merchantbackend

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.JsonAdapter
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Response to [MerchantBackend.getPayerOwnedPaymentTokens]
 */
@JsonAdapter(PayerOwnedPaymentTokensResponse.Deserializer::class)
data class PayerOwnedPaymentTokensResponse(
    /**
     * The response payload.
     */
    val payerOwnedPaymentTokens: PayerOwnedPaymentTokens,
    /**
     * Operations you can perform on the whole list of tokens.
     * Note that you generally cannot call these from your mobile app.
     */
    val operations: List<Operation>
) {
    internal class Deserializer : JsonDeserializer<PayerOwnedPaymentTokensResponse> {
        override fun deserialize(
            element: JsonElement,
            typeOfT: Type?,
            context: JsonDeserializationContext
        ): PayerOwnedPaymentTokensResponse? {
            if (element.isJsonNull) return null

            try {
                val json = element.asJsonObject
                return PayerOwnedPaymentTokensResponse(
                    payerOwnedPaymentTokens = PayerOwnedPaymentTokens.deserialize(
                        json["payerOwnedPaymentTokens"],
                        context
                    ),
                    operations = json["operations"]?.let {
                        context.deserialize(it, (object : TypeToken<List<Operation>>() {}).type)
                    } ?: emptyList()
                )
            } catch (e: Exception) {
                throw JsonSyntaxException(e)
            }
        }
    }
}
