package com.swedbankpay.mobilesdk.merchantbackend

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

/**
 * Payload of [PayerOwnedPaymentTokensResponse]
 */
data class PayerOwnedPaymentTokens(
    /**
     * The id (url) of this resource.
     *
     * Note that you generally cannot dereference this from your mobile app.
     */
    val id: String,
    /**
     * The payerReference associated with these tokens
     */
    val payerReference: String,
    /**
     * The list of tokens and associated information
     */
    val paymentTokens: List<PaymentTokenInfo>
) {
    internal companion object {
        internal fun deserialize(
            element: JsonElement,
            context: JsonDeserializationContext
        ): PayerOwnedPaymentTokens {
            try {
                val json = element.asJsonObject
                return PayerOwnedPaymentTokens(
                    id = json["id"].asString,
                    payerReference = json["id"].asString,
                    paymentTokens = json["paymentTokens"]?.let {
                        context.deserialize(it, (object : TypeToken<List<PaymentTokenInfo>>() {}).type)
                    } ?: emptyList()
                )
            } catch (e: Exception) {
                throw JsonSyntaxException(e)
            }
        }
    }
}