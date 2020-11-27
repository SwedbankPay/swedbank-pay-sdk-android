package com.swedbankpay.mobilesdk.merchantbackend

import android.content.Context
import com.swedbankpay.mobilesdk.RequestDecorator
import com.swedbankpay.mobilesdk.UserHeaders
import com.swedbankpay.mobilesdk.internal.remote.Api

/**
 * Additional utilities supported by the Merchant Backend
 */
@Suppress("unused")
object MerchantBackend {
    /**
     * Retrieves the payment tokens owned by the given
     * payerReference.
     *
     * Your backend must enable this functionality separately.
     *
     * @param context a Context from your application
     * @param configuration the backend configuration
     * @param payerReference the reference to query
     * @param extraHeaderNamesAndValues any header names and values you wish to append to the request
     * @return the response from Swedbank Pay
     */
    suspend fun getPayerOwnedPaymentTokens(
        context: Context,
        configuration: MerchantBackendConfiguration,
        payerReference: String,
        vararg extraHeaderNamesAndValues: String
    ): PayerOwnedPaymentTokensResponse {
        val url = configuration
            .rootLink.href
            .newBuilder()
            .addPathSegment("payers")
            .addPathSegment(payerReference)
            .addPathSegment("paymentTokens")
            .build()
        val headersBuilder: suspend RequestDecorator.(UserHeaders) -> Unit = {
            for (i in extraHeaderNamesAndValues.indices step 2) {
                val name = extraHeaderNamesAndValues[i]
                val value = extraHeaderNamesAndValues[i + 1]
                it.add(name, value)
            }
        }

        return Api.request(
            context,
            configuration,
            "GET",
            url,
            null,
            headersBuilder,
            PayerOwnedPaymentTokensResponse::class.java
        ).value
    }
}
