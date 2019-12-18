package com.swedbankpay.mobilesdk.internal.remote.json

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.*
import com.swedbankpay.mobilesdk.internal.BundleTypeAdapterFactory
import com.swedbankpay.mobilesdk.internal.remote.Api
import com.swedbankpay.mobilesdk.internal.remote.ExtensibleJsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.Response
import java.io.IOException

internal sealed class Link(
    val href: HttpUrl
) {
    companion object {
        fun getDeserializer(response: Response): JsonDeserializer<Link?> {
            val requestUrl = response.request().url()
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

    protected suspend inline fun <reified T : Any> get(
        context: Context,
        configuration: Configuration,
        noinline userHeadersBuilder: suspend RequestDecorator.(UserHeaders) -> Unit
    ) = getCacheable<T>(context, configuration, userHeadersBuilder).value

    protected suspend inline fun <reified T : Any> getCacheable(
        context: Context,
        configuration: Configuration,
        noinline userHeadersBuilder: suspend RequestDecorator.(UserHeaders) -> Unit
    ) = Api.get(context, configuration, href, userHeadersBuilder, T::class.java)

    protected suspend inline fun <reified T : Any> post(
        context: Context,
        configuration: Configuration,
        body: String,
        noinline userHeadersBuilder: suspend RequestDecorator.(UserHeaders) -> Unit
    ) = Api.post(context, configuration, href, body, userHeadersBuilder, T::class.java).value

    class Root(href: HttpUrl) : Link(href) {
        suspend fun get(context: Context, configuration: Configuration) = getCacheable<TopLevelResources>(context, configuration) {
            decorateGetTopLevelResources(it)
        }
    }

    class Consumers(href: HttpUrl) : Link(href) {
        @Throws(IOException::class) // for testing; see Configuration.getTopLevelResources
        suspend fun post(context: Context, configuration: Configuration, consumer: Consumer): ConsumerSession {
            val body = toJsonBody(consumer)
            return post(context, configuration, body) {
                decorateInitiateConsumerSession(it, body, consumer)
            }
        }
    }

    class PaymentOrders(href: HttpUrl) : Link(href) {
        @Throws(IOException::class) // for testing
        suspend fun post(
            context: Context,
            configuration: Configuration,
            paymentOrder: PaymentOrder
        ): PaymentOrderIn {
            val body = toJsonBody(Body(paymentOrder))
            return post(context, configuration, body) {
                decorateCreatePaymentOrder(it, body, paymentOrder)
            }
        }

        private class Body(
            @Suppress("unused")
            @SerializedName("paymentorder")
            val paymentorder: PaymentOrder
        )
    }
}
