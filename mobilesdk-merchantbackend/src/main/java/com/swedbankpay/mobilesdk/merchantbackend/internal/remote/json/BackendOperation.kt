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
 * The SDK contains operations one can path the payment with in order to perform tasks. These operations need to be routed via the backend.
 * BackendOperation encapsulates these actions and relays them via the backend (at the default endPoint /patch) to Swedbank Pay.
 * In version 2 the backend needed specific handling of all supported operations, while now it has a more general approach. 
 */
internal sealed class BackendOperation(
    val configuration: MerchantBackendConfiguration,
    val href: String
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

    class PaymentOrderSetInstrument(configuration: MerchantBackendConfiguration, href: String) : BackendOperation(configuration, href) {
        suspend fun patch(
            context: Context,
            instrument: String,
            endPoint: String = "patch"
        ): PaymentOrderIn {
            val body = toJsonBody(mapOf(
                "paymentorder" to mapOf(
                    "operation" to "SetInstrument",
                    "instrument" to instrument
                ),
                "href" to href 
            ))
            val url = configuration.backendUrl.toHttpUrl().newBuilder()
                .addPathSegment(endPoint)
                .build()
            return performRequest(context,"PATCH", body, url) {
                decoratePaymentOrderSetInstrument(it, url.toString(), body, instrument)
            }
        }
    }
}
