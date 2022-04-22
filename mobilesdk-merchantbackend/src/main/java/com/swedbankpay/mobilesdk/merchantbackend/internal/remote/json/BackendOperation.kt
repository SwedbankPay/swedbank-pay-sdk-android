package com.swedbankpay.mobilesdk.merchantbackend.internal.remote.json

import android.content.Context
import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration
import com.swedbankpay.mobilesdk.merchantbackend.RequestDecorator
import com.swedbankpay.mobilesdk.merchantbackend.UserHeaders
import com.swedbankpay.mobilesdk.merchantbackend.internal.remote.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

/**
 * The SDK contains operations one can perform. These operations need to be routed via the backend.
 * BackendOperation encapsulates these actions and relays them via the backend (at the default endPoint /patch) to Swedbank Pay.
 * This is a general approach to perform these tasks, and the list of available tasks is retrieved 
 * from responses. E.g. see [https://developer.swedbankpay.com/checkout-v3/payments-only/seamless-view] 
 * for more details.
 */
internal sealed class BackendOperation(
    val configuration: MerchantBackendConfiguration
) {
    companion object {
        private suspend fun toJsonBody(value: Any) = withContext(Dispatchers.Default) {
            GsonBuilder()
                .create()
                .toJson(value)
        }
    }
    
    protected suspend inline fun <reified T : Any> performRequestCacheable(
        context: Context,
        method: String,
        body: String?,
        url: HttpUrl,
        noinline userHeadersBuilder: suspend RequestDecorator.(UserHeaders) -> Unit
    ) = Api.request(
        context = context,
        configuration = configuration,
        method = method,
        url = url,
        body = body,
        userHeadersBuilder = userHeadersBuilder,
        entityType = T::class.java
    )

    protected suspend inline fun <reified T : Any> performRequest(
        context: Context,
        method: String = "PATCH",
        body: String?,
        url: HttpUrl,
        noinline userHeadersBuilder: suspend RequestDecorator.(UserHeaders) -> Unit
    ) = performRequestCacheable<T>(
        context, method = method, body = body, url = url, userHeadersBuilder = userHeadersBuilder
    ).value

    /**
     * Patch the current purchase with a new instrument.This is called by the PaymentFragment during
     * updatePurchase to perform the setInstrument action.
     */
    class PaymentOrderSetInstrument(
        configuration: MerchantBackendConfiguration, 
        private val href: String
        ) : BackendOperation(configuration) {

        suspend fun patch(
            context: Context,
            instrument: String,
            endpoint: String = "patch"
        ): PaymentOrderIn {
            val body = toJsonBody(mapOf(
                "paymentorder" to mapOf(
                    "operation" to "SetInstrument",
                    "instrument" to instrument
                ),
                "href" to href 
            ))
            val url = configuration.backendUrl.toHttpUrl().newBuilder()
                .addPathSegment(endpoint)
                .build()
            return performRequest(context,"PATCH", body, url) {
                decoratePaymentOrderSetInstrument(it, url.toString(), body, instrument)
            }
        }
    }

    /**
     * A general method for expanding resources like payer, paid, etc. The resulting data class is 
     * is defined by entityType.
     */
    class ExpandOperation(configuration: MerchantBackendConfiguration) : BackendOperation(configuration) {
        /**
         * Perform a POST request to the backend, to expand a resource.
         * @param paymentId the payment in question
         * @param expand what to expand
         * @param endpoint the destination on the backend which can handle/relay the operation. Defaults to "expand"
         * @param entityType the generic return type
         * @return a data class populated from the returning JSON, defined by entityType
         */
        suspend fun <T: Any> post(
            context: Context,
            paymentId: String,
            expand: Array<String>,
            endpoint: String = "expand",
            entityType: Class<T>
        ): T {
            val body = toJsonBody(mapOf(
                "resource" to paymentId,
                "expand" to expand.asList()
            ))
            val url = configuration.backendUrl.toHttpUrl().newBuilder()
                .addPathSegment(endpoint)
                .build()
            val method = "POST"

            return Api.request(
                context = context,
                configuration = configuration,
                method = method,
                url = url,
                body = body,
                userHeadersBuilder = {
                    decorateExpandRequest(it, url.toString(), body)
                },
                entityType = entityType).value
        }
    }
}
