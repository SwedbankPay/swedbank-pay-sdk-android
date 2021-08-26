package com.swedbankpay.mobilesdk.merchantbackend

import android.content.Context
import com.swedbankpay.mobilesdk.merchantbackend.internal.remote.Api
import java.io.IOException

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

    /**
     * Deletes the specified payment token.
     *
     * Your backend must enable this functionality separately.
     * After you make this request, you should refresh your local list of tokens.
     *
     * @param context a Context from your application
     * @param configuration the backend configuration
     * @param paymentTokenInfo the token to delete
     * @param comment the reason for the deletion
     * @param extraHeaderNamesAndValues any header names and values you wish to append to the request
     */
    suspend fun deletePayerOwnerPaymentToken(
        context: Context,
        configuration: MerchantBackendConfiguration,
        paymentTokenInfo: PaymentTokenInfo,
        comment: String,
        vararg extraHeaderNamesAndValues: String
    ) {
        val link = paymentTokenInfo.mobileSDK?.delete ?: throw IOException("Missing delete link")
        link.patch(context, configuration, extraHeaderNamesAndValues, comment)
    }
}
