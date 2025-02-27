package com.swedbankpay.mobilesdk.paymentsession.api

import com.swedbankpay.mobilesdk.logging.BeaconService
import com.swedbankpay.mobilesdk.logging.model.EventAction
import com.swedbankpay.mobilesdk.logging.model.HttpModel
import com.swedbankpay.mobilesdk.paymentsession.OperationStep
import com.swedbankpay.mobilesdk.paymentsession.StepInstruction
import com.swedbankpay.mobilesdk.paymentsession.api.model.SwedbankPayAPIError
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.util.TimeOutUtil
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PaymentSessionResponse
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ProblemDetails
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.RequestMethod
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.toInstrument
import com.swedbankpay.mobilesdk.paymentsession.util.JsonUtil.toApiError
import com.swedbankpay.mobilesdk.paymentsession.util.JsonUtil.toPaymentOutputModel
import com.swedbankpay.mobilesdk.paymentsession.util.toExtensionsModel
import java.io.OutputStreamWriter
import java.net.MalformedURLException
import java.net.SocketTimeoutException
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal open class PaymentSessionAPIClient {

    suspend fun executeNextRequest(
        operation: OperationStep,
        paymentAttemptInstrument: PaymentAttemptInstrument?
    ): PaymentSessionResponse {
        val overrideApiCall =
            operation.instructions.firstOrNull { it is StepInstruction.OverrideApiCall }

        val paymentResponse = when {
            overrideApiCall != null && overrideApiCall is StepInstruction.OverrideApiCall -> {
                PaymentSessionResponse.Success(overrideApiCall.paymentOutputModel)
            }

            operation.requestMethod == RequestMethod.GET -> {
                getRequest(
                    operation.url,
                    timeout = TimeOutUtil.getRequestTimeout(
                        operation.operationRel,
                        paymentAttemptInstrument?.toInstrument()
                    )
                )
            }

            operation.requestMethod == RequestMethod.POST -> {
                postRequest(
                    operation.url,
                    operation.data ?: "",
                    timeout = TimeOutUtil.getRequestTimeout(
                        operation.operationRel,
                        paymentAttemptInstrument?.toInstrument()
                    )
                )
            }

            else -> PaymentSessionResponse.Error(SwedbankPayAPIError.Unknown)
        }

        return suspendCoroutine {
            it.resume(paymentResponse)
        }
    }

    private suspend fun getRequest(
        url: URL?,
        timeout: Int
    ): PaymentSessionResponse = suspendCoroutine { continuation ->
        val start = System.currentTimeMillis()

        url?.let { requestUrl ->
            try {
                val connection = requestUrl.openConnection() as HttpsURLConnection

                connection.connectTimeout = timeout
                connection.readTimeout = timeout
                connection.requestMethod = "GET"

                PaymentSessionAPIConstants.commonHeaders.forEach {
                    connection.setRequestProperty(it.key, it.value)
                }

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
                        PaymentSessionResponse.Success(paymentOutputModel = response.toPaymentOutputModel())
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
                        PaymentSessionResponse.Retry(
                            SwedbankPayAPIError.Error(
                                message = "Internal server error",
                                responseCode = responseCode
                            )
                        )
                    )
                } else {
                    val response = connection.errorStream.bufferedReader()
                        .use { it.readText() }

                    try {
                        val apiError = response.toApiError()

                        val error = SwedbankPayAPIError.Error(
                            message = apiError.detail,
                            responseCode = responseCode
                        )

                        logAPICall(
                            url = requestUrl.toString(),
                            method = "GET",
                            duration = System.currentTimeMillis() - start,
                            error = error
                        )

                        continuation.resume(
                            PaymentSessionResponse.Error(error)
                        )
                    } catch (e: Exception) {
                        logAPICall(
                            url = requestUrl.toString(),
                            method = "GET",
                            duration = System.currentTimeMillis() - start,
                            responseStatusCode = responseCode
                        )

                        continuation.resume(
                            PaymentSessionResponse.Error(
                                SwedbankPayAPIError.Error(
                                    message = connection.responseMessage,
                                    responseCode = responseCode
                                )
                            )
                        )
                    }
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
                continuation.resume(PaymentSessionResponse.Retry(error))
            } catch (e: Exception) {
                val error = SwedbankPayAPIError.Error(message = e.localizedMessage)
                logAPICall(
                    url = url.toString(),
                    method = "GET",
                    duration = System.currentTimeMillis() - start,
                    error = error
                )

                continuation.resume(
                    PaymentSessionResponse.Error(error)
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
            continuation.resume(PaymentSessionResponse.Error(error))
        }

    }

    private suspend fun postRequest(
        url: URL?,
        data: String,
        timeout: Int
    ): PaymentSessionResponse = suspendCoroutine { continuation ->
        val start = System.currentTimeMillis()
        url?.let { requestUrl ->
            try {
                val connection = requestUrl.openConnection() as HttpsURLConnection

                connection.connectTimeout = timeout
                connection.readTimeout = timeout
                connection.requestMethod = "POST"

                PaymentSessionAPIConstants.commonHeaders.forEach {
                    connection.setRequestProperty(it.key, it.value)
                }

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

                    continuation.resume(PaymentSessionResponse.Success(response.toPaymentOutputModel()))
                } else if ((500.until(600)).contains(responseCode)) {
                    logAPICall(
                        url = requestUrl.toString(),
                        method = "POST",
                        duration = System.currentTimeMillis() - start,
                        responseStatusCode = responseCode,
                    )

                    // When we get a server error we want to retry the request
                    continuation.resume(
                        PaymentSessionResponse.Retry(
                            SwedbankPayAPIError.Error(
                                message = "Internal server error",
                                responseCode = responseCode
                            )
                        )
                    )
                } else {
                    val response = connection.errorStream.bufferedReader()
                        .use { it.readText() }

                    try {
                        val apiError = response.toApiError()

                        val error = SwedbankPayAPIError.Error(
                            message = apiError.detail,
                            responseCode = responseCode
                        )

                        logAPICall(
                            url = requestUrl.toString(),
                            method = "POST",
                            duration = System.currentTimeMillis() - start,
                            error = error
                        )

                        continuation.resume(
                            PaymentSessionResponse.Error(error)
                        )
                    } catch (e: Exception) {
                        logAPICall(
                            url = requestUrl.toString(),
                            method = "POST",
                            duration = System.currentTimeMillis() - start,
                            responseStatusCode = responseCode
                        )

                        continuation.resume(
                            PaymentSessionResponse.Error(
                                SwedbankPayAPIError.Error(
                                    message = connection.responseMessage,
                                    responseCode = responseCode
                                )
                            )
                        )
                    }
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
                continuation.resume(PaymentSessionResponse.Retry(error))
            } catch (e: Exception) {
                val error = SwedbankPayAPIError.Error(message = e.localizedMessage)
                logAPICall(
                    url = url.toString(),
                    method = "POST",
                    duration = System.currentTimeMillis() - start,
                    error = error
                )

                continuation.resume(
                    PaymentSessionResponse.Error(error)
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
            continuation.resume(PaymentSessionResponse.Error(error))
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
        problemDetailsWithOperation.operation?.href?.let {
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

}