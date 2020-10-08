package com.swedbankpay.mobilesdk.internal.remote.json

import android.content.Context
import android.os.Parcel
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.*
import com.swedbankpay.mobilesdk.internal.BundleTypeAdapterFactory
import com.swedbankpay.mobilesdk.internal.remote.Api
import com.swedbankpay.mobilesdk.internal.remote.ExtensibleJsonObject
import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Response

internal sealed class Link(
    val href: HttpUrl
) {
    companion object {
        fun getDeserializer(response: Response): JsonDeserializer<Link?> {
            val requestUrl = response.request.url
            return JsonDeserializer { element, type, _ ->
                if (element.isJsonNull) return@JsonDeserializer null

                val link = try {
                    element.asString
                } catch (e: Exception) {
                    throw JsonSyntaxException(e)
                }
                val href = requestUrl.resolve(link)
                    ?: throw JsonSyntaxException("Invalid link: $link")
                (type as Class<*>)
                    .asSubclass(Link::class.java)
                    .getConstructor(HttpUrl::class.java)
                    .newInstance(href)
            }
        }

        private suspend fun toJsonBody(value: Any) = withContext(Dispatchers.Default) {
            GsonBuilder()
                .registerTypeAdapterFactory(ExtensibleJsonObject.TypeAdapterFactory)
                .registerTypeAdapterFactory(BundleTypeAdapterFactory)
                .create()
                .toJson(value)
        }
    }

    protected suspend inline fun <reified T : Any> performRequestCacheable(
        context: Context,
        configuration: MerchantBackendConfiguration,
        method: String,
        body: String?,
        noinline userHeadersBuilder: suspend RequestDecorator.(UserHeaders) -> Unit
    ) = Api.request(
        context = context,
        configuration = configuration,
        method = method,
        url = href,
        body = body,
        userHeadersBuilder = userHeadersBuilder,
        entityType = T::class.java
    )

    protected suspend inline fun <reified T : Any> performRequest(
        context: Context,
        configuration: MerchantBackendConfiguration,
        method: String,
        body: String?,
        noinline userHeadersBuilder: suspend RequestDecorator.(UserHeaders) -> Unit
    ) = performRequestCacheable<T>(
        context, configuration, method, body, userHeadersBuilder
    ).value

    class Root(href: HttpUrl) : Link(href) {
        suspend fun get(context: Context, configuration: MerchantBackendConfiguration) =
            performRequestCacheable<TopLevelResources>(
                context, configuration,
                "GET", null
            ) {
                decorateGetTopLevelResources(it)
            }
    }

    class Consumers(href: HttpUrl) : Link(href) {
        suspend fun post(
            context: Context,
            configuration: MerchantBackendConfiguration,
            consumer: Consumer
        ): ConsumerSession {
            val body = toJsonBody(consumer)
            return performRequest(context, configuration, "POST", body) {
                decorateInitiateConsumerSession(it, body, consumer)
            }
        }
    }

    class PaymentOrders(href: HttpUrl) : Link(href) {
        suspend fun post(
            context: Context,
            configuration: MerchantBackendConfiguration,
            paymentOrder: PaymentOrder
        ): PaymentOrderIn {
            val body = toJsonBody(Body(paymentOrder))
            return performRequest(context, configuration, "POST", body) {
                decorateCreatePaymentOrder(it, body, paymentOrder)
            }
        }

        private class Body(
            @Suppress("unused")
            @SerializedName("paymentorder")
            val paymentorder: PaymentOrder
        )
    }

    class PaymentOrderSetInstrument(href: HttpUrl) : Link(href) {
        suspend fun patch(
            context: Context,
            configuration: MerchantBackendConfiguration,
            instrument: String
        ): PaymentOrderIn {
            val body = toJsonBody(mapOf(
                "paymentorder" to mapOf(
                    "operation" to "SetInstrument",
                    "instrument" to instrument
                )
            ))
            return performRequest(context, configuration, "PATCH", body) {
                decoratePaymentOrderSetInstrument(it, href.toString(), body, instrument)
            }
        }
    }
}

internal fun Parcel.writeLink(l: Link?) = writeString(l?.href?.toString())
internal inline fun <T : Link> Parcel.readLink(constructor: (HttpUrl) -> T) =
    readString()?.toHttpUrl()?.let(constructor)
