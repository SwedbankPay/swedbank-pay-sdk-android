package com.payex.mobilesdk.internal.remote

import android.content.Context
import com.google.android.gms.security.ProviderInstaller
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.payex.mobilesdk.BadRequestDescription
import com.payex.mobilesdk.Configuration
import com.payex.mobilesdk.RequestDecorator
import com.payex.mobilesdk.UserHeaders
import com.payex.mobilesdk.internal.remote.json.Link
import com.payex.mobilesdk.internal.remote.json.annotations.Required
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.annotations.TestOnly
import java.io.IOException
import java.io.Reader

internal object Api {
    private val JSON_MEDIA_TYPE = MediaType.get("application/json")

    private val lazyClient = lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
    private suspend fun getClient(context: Context): OkHttpClient {
        if (!lazyClient.isInitialized()) {
            withContext(Dispatchers.IO) {
                ProviderInstaller.installIfNeeded(context)
            }
        }
        return lazyClient.value
    }
    @TestOnly
    internal fun skipProviderInstallerForTests() {
        lazyClient.value
    }

    suspend fun <T : Any> get(
        context: Context,
        configuration: Configuration,
        url: HttpUrl,
        userHeadersBuilder: RequestDecorator.(UserHeaders) -> Unit,
        entityType: Class<T>
    ) = request(context, configuration, "GET", url, null, userHeadersBuilder, entityType)

    suspend fun <T : Any> post(
        context: Context,
        configuration: Configuration,
        url: HttpUrl,
        body: String,
        userHeadersBuilder: RequestDecorator.(UserHeaders) -> Unit,
        entityType: Class<T>
    ) = request(context, configuration, "POST", url, body, userHeadersBuilder, entityType)

    private suspend fun <T : Any> request(
        context: Context,
        configuration: Configuration,
        method: String,
        url: HttpUrl,
        body: String?,
        userHeadersBuilder: RequestDecorator.(UserHeaders) -> Unit,
        entityType: Class<T>
    ): CacheableResult<T> {
        val request = buildRequest(configuration, method, url, body, userHeadersBuilder)
        return executeRequest(context, request, entityType)
    }

    private fun buildRequest(
        configuration: Configuration,
        method: String,
        url: HttpUrl,
        body: String?,
        userHeadersBuilder: RequestDecorator.(UserHeaders) -> Unit
    ): Request {
        val domain = url.host()
        if (configuration.domainWhitelist.none { it.matches(domain) }) {
            throw IOException("Non-whitelisted domain: $domain")
        }

        val headers = UserHeaders().also {
            configuration.requestDecorator?.apply {
                decorateAnyRequest(it, method, url.toString(), body)
                userHeadersBuilder(it)
            }
        }.toHeaders()

        val requestBody = body?.let { RequestBody.create(JSON_MEDIA_TYPE, it) }

        return Request.Builder()
            .url(url)
            .headers(headers)
            .method(method, requestBody)
            .build()
    }

    private suspend fun <T : Any> executeRequest(
        context: Context,
        request: Request,
        entityType: Class<T>
    ): CacheableResult<T> {
        val client = getClient(context)
        val call = client.newCall(request)
        val result = CompletableDeferred<CacheableResult<T>>()
        call.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    try {
                        checkClientError(it)
                        val entity = parseResponse(it, entityType)
                        it.receivedResponseAtMillis()
                        result.complete(CacheableResult(entity, it.validUntilMillis))
                    } catch (t: Throwable) {
                        result.completeExceptionally(t)
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                result.completeExceptionally(e)
            }
        })
        try {
            return result.await()
        } catch (c: CancellationException) {
            call.cancel()
            throw c
        }
    }

    private fun checkClientError(response: Response) {
        val code = response.code()
        if (code in 400..499) {
            val body = getClientErrorBody(response)
            val description = BadRequestDescription(
                code,
                body?.first?.toString(),
                body?.second
            )
            throw BadRequestException(description)
        }
    }

    private fun getClientErrorBody(response: Response): Pair<MediaType, String>? {
        return response.body()?.run {
            contentType()?.let {
                try {
                    Pair(it, string())
                } catch (_: Exception) {
                    null
                }
            }
        }
    }

    private fun <T : Any> parseResponse(response: Response, entityType: Class<T>): T {
        return GsonBuilder()
            .registerTypeHierarchyAdapter(Link::class.java, Link.getDeserializer(response))
            .create()
            .fromJson(response.jsonStream(), entityType)
            .also(::validateResponse)
    }

    private fun Response.jsonStream(): Reader {
        val body = body() ?: throw IOException("Missing response body")
        val contentType = body.contentType()
        if (contentType == null || contentType.type() != JSON_MEDIA_TYPE.type() || contentType.subtype() != JSON_MEDIA_TYPE.subtype()) {
            throw IOException("Invalid Content-Type: $contentType")
        }
        return body.charStream()
    }

    private fun validateResponse(response: Any) {
        for (field in response.javaClass.declaredFields) {
            if (field.isAnnotationPresent(Required::class.java)) {
                field.isAccessible = true
                if (field.get(response) == null) {
                    val fieldName = field.getAnnotation(SerializedName::class.java)?.value ?: field.name
                    throw IOException("Missing required field $fieldName")
                }
            }
        }
    }

    private val Response.validUntilMillis: Long?
        get() = cacheControl().run {
            maxAgeSeconds().takeIf {
                it > 0 && !noStore() && !noCache()
            }
        }?.times(1000L)?.plus(receivedResponseAtMillis())
}