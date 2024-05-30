package com.swedbankpay.mobilesdk.nativepayments.api

import com.swedbankpay.mobilesdk.logging.BeaconService
import com.swedbankpay.mobilesdk.logging.model.EventAction
import com.swedbankpay.mobilesdk.logging.model.HttpModel
import com.swedbankpay.mobilesdk.nativepayments.OperationStep
import com.swedbankpay.mobilesdk.nativepayments.api.model.SwedbankPayAPIError
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.NativePaymentResponse
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.ProblemDetails
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.RequestMethod
import com.swedbankpay.mobilesdk.nativepayments.util.JsonUtil.toPaymentOutputModel
import com.swedbankpay.mobilesdk.nativepayments.util.toExtensionsModel
import java.io.OutputStreamWriter
import java.net.MalformedURLException
import java.net.SocketTimeoutException
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class NativePaymentsAPIClient {

    suspend fun executeNextRequest(
        operation: OperationStep
    ): NativePaymentResponse {
        val paymentResponse = when (operation.requestMethod) {
            RequestMethod.GET -> {
                getRequest(operation.url)
            }

            RequestMethod.POST -> {
                postRequest(operation.url, operation.data ?: "")
            }

            else -> NativePaymentResponse.Error(SwedbankPayAPIError.Unknown)
        }

        return suspendCoroutine {
            it.resume(paymentResponse)
        }
    }

    private suspend fun getRequest(
        url: URL?
    ): NativePaymentResponse = suspendCoroutine { continuation ->
        val start = System.currentTimeMillis()

        url?.let { requestUrl ->
            try {
                val connection = requestUrl.openConnection() as HttpsURLConnection

                connection.connectTimeout = REQUEST_TIMEOUT_IN_MS
                connection.readTimeout = REQUEST_TIMEOUT_IN_MS

                connection.requestMethod = "GET"
                connection.setRequestProperty("Accept", "application/json")
                connection.doInput = true
                connection.doOutput = false

                val responseCode = connection.responseCode
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader()
                        .use { it.readText() }

                    logAPICall(
                        url = requestUrl.toString(),
                        method = "GET",
                        duration = System.currentTimeMillis() - start,
                        responseStatusCode = responseCode
                    )

                    continuation.resume(
                        NativePaymentResponse.Success(paymentOutputModel = response.toPaymentOutputModel())
                    )
                } else if ((500.until(600)).contains(responseCode)) {
                    logAPICall(
                        url = requestUrl.toString(),
                        method = "GET",
                        duration = System.currentTimeMillis() - start,
                        responseStatusCode = responseCode,
                    )

                    // When we get a server error we want to retry the request
                    continuation.resume(
                        NativePaymentResponse.Retry(
                            SwedbankPayAPIError.Error(
                                message = "Internal server error",
                                responseCode = responseCode
                            )
                        )
                    )
                } else {
                    logAPICall(
                        url = requestUrl.toString(),
                        method = "GET",
                        duration = System.currentTimeMillis() - start,
                        responseStatusCode = responseCode,
                    )

                    continuation.resume(
                        NativePaymentResponse.Error(
                            SwedbankPayAPIError.Error(
                                responseCode = responseCode
                            )
                        )
                    )
                }
            } catch (timeoutException: SocketTimeoutException) {
                val error = SwedbankPayAPIError.Error(message = timeoutException.localizedMessage)
                logAPICall(
                    url = url.toString(),
                    method = "GET",
                    duration = System.currentTimeMillis() - start,
                    error = error
                )

                // When we get a time out we want to retry the request
                continuation.resume(NativePaymentResponse.Retry(error))
            } catch (e: Exception) {
                val error = SwedbankPayAPIError.Error(message = e.localizedMessage)
                logAPICall(
                    url = url.toString(),
                    method = "GET",
                    duration = System.currentTimeMillis() - start,
                    error = error
                )

                continuation.resume(
                    NativePaymentResponse.Error(error)
                )
            }
        } ?: kotlin.run {
            val error = SwedbankPayAPIError.InvalidUrl
            logAPICall(
                url = "",
                method = "GET",
                duration = System.currentTimeMillis() - start,
                error = error
            )
            continuation.resume(NativePaymentResponse.Error(error))
        }

    }

    private suspend fun postRequest(
        url: URL?,
        data: String
    ): NativePaymentResponse = suspendCoroutine { continuation ->
        val start = System.currentTimeMillis()
        url?.let { requestUrl ->
            try {
                val connection = requestUrl.openConnection() as HttpsURLConnection

                connection.connectTimeout = REQUEST_TIMEOUT_IN_MS
                connection.readTimeout = REQUEST_TIMEOUT_IN_MS

                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept", "application/json")
                connection.doInput = true
                connection.doOutput = true

                val outputStreamWriter = OutputStreamWriter(connection.outputStream)
                outputStreamWriter.write(data)
                outputStreamWriter.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader()
                        .use { it.readText() }

                    logAPICall(
                        url = requestUrl.toString(),
                        method = "POST",
                        duration = System.currentTimeMillis() - start,
                        responseStatusCode = responseCode
                    )

                    continuation.resume(NativePaymentResponse.Success(response.toPaymentOutputModel()))
                } else if ((500.until(600)).contains(responseCode)) {
                    logAPICall(
                        url = requestUrl.toString(),
                        method = "POST",
                        duration = System.currentTimeMillis() - start,
                        responseStatusCode = responseCode,
                    )

                    // When we get a server error we want to retry the request
                    continuation.resume(
                        NativePaymentResponse.Retry(
                            SwedbankPayAPIError.Error(
                                message = "Internal server error",
                                responseCode = responseCode
                            )
                        )
                    )
                } else {
                    logAPICall(
                        url = requestUrl.toString(),
                        method = "POST",
                        duration = System.currentTimeMillis() - start,
                        responseStatusCode = responseCode
                    )
                    continuation.resume(
                        NativePaymentResponse.Error(
                            SwedbankPayAPIError.Error(
                                responseCode = responseCode
                            )
                        )
                    )
                }
            } catch (timeoutException: SocketTimeoutException) {
                val error = SwedbankPayAPIError.Error(message = timeoutException.localizedMessage)
                logAPICall(
                    url = url.toString(),
                    method = "POST",
                    duration = System.currentTimeMillis() - start,
                    error = error
                )

                // When we get a time out we want to retry the request
                continuation.resume(NativePaymentResponse.Retry(error))
            } catch (e: Exception) {
                val error = SwedbankPayAPIError.Error(message = e.localizedMessage)
                logAPICall(
                    url = url.toString(),
                    method = "POST",
                    duration = System.currentTimeMillis() - start,
                    error = error
                )

                continuation.resume(
                    NativePaymentResponse.Error(error)
                )
            }
        } ?: kotlin.run {
            val error = SwedbankPayAPIError.InvalidUrl
            logAPICall(
                url = "",
                method = "POST",
                duration = System.currentTimeMillis() - start,
                error = error
            )
            continuation.resume(NativePaymentResponse.Error(error))
        }

    }

    /**
     * We need to tell the api that we have taken care of the problem or
     * else it will not disappear from future responses during the payment session
     *
     * So if this request fails we need to call this again until it succeeds
     */
    fun postFailedAttemptRequest(problemDetailsWithOperation: ProblemDetails) {
        val start = System.currentTimeMillis()
        problemDetailsWithOperation.operations.href?.let {
            try {
                val url = URL(it)

                val connection = url.openConnection() as HttpsURLConnection

                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept", "application/json")
                connection.doInput = true
                connection.doOutput = true

                val responseCode = connection.responseCode
                if (responseCode == HttpsURLConnection.HTTP_NO_CONTENT) {
                    logAPICall(
                        url = it,
                        method = "POST",
                        duration = System.currentTimeMillis() - start,
                        responseStatusCode = responseCode,
                    )
                } else {
                    logAPICall(
                        url = it,
                        method = "POST",
                        duration = System.currentTimeMillis() - start,
                        responseStatusCode = responseCode,
                    )
                }
            } catch (e: MalformedURLException) {
                logAPICall(
                    url = it,
                    method = "POST",
                    duration = System.currentTimeMillis() - start,
                    error = SwedbankPayAPIError.Error(message = e.localizedMessage)
                )
            } catch (e: Exception) {
                logAPICall(
                    url = it,
                    method = "POST",
                    duration = System.currentTimeMillis() - start,
                    error = SwedbankPayAPIError.Error(message = e.localizedMessage)
                )
            }
        }
    }

    private fun logAPICall(
        url: String,
        method: String,
        duration: Long,
        responseStatusCode: Int? = null,
        error: SwedbankPayAPIError? = null
    ) {
        BeaconService.logEvent(
            EventAction.HttpRequest(
                http = HttpModel(
                    requestUrl = url,
                    method = method,
                    responseStatusCode = responseStatusCode,
                ),
                duration = duration.toInt(),
                extensions = error?.toExtensionsModel()
            )
        )
    }

    companion object {
        private const val REQUEST_TIMEOUT_IN_MS = 10 * 1000
    }


}