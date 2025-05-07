package com.swedbankpay.mobilesdk.paymentsession.api

import com.swedbankpay.mobilesdk.logging.BeaconService
import com.swedbankpay.mobilesdk.logging.model.EventAction
import com.swedbankpay.mobilesdk.logging.model.HttpModel
import com.swedbankpay.mobilesdk.paymentsession.OperationStep
import com.swedbankpay.mobilesdk.paymentsession.StepInstruction
import com.swedbankpay.mobilesdk.paymentsession.api.model.SwedbankPayAPIError
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.util.TimeOutUtil
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ApiError
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PaymentOutputModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PaymentSessionResponse
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ProblemDetails
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.RequestMethod
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.toInstrument
import com.swedbankpay.mobilesdk.paymentsession.util.JsonUtil.toApiError
import com.swedbankpay.mobilesdk.paymentsession.util.JsonUtil.toPaymentOutputModel
import com.swedbankpay.mobilesdk.paymentsession.util.toExtensionsModel
import java.io.OutputStreamWriter
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException
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

                val logInfo = ApiCallLogInfo(
                    url = requestUrl.toString(),
                    method = "GET",
                    duration = System.currentTimeMillis() - start,
                    responseStatusCode = responseCode
                )

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader()
                        .use { it.readText() }

                    continuation.resume(
                        handleSuccess(
                            paymentOutputModel = response.toPaymentOutputModel(),
                            logInfo = logInfo
                        )
                    )
                } else if ((500.until(600)).contains(responseCode)) {
                    // When we get a server error we want to retry the request
                    continuation.resume(
                        handleRetry(
                            logInfo = logInfo.copy(
                                error = SwedbankPayAPIError.Error(
                                    message = "Internal server error",
                                    responseCode = responseCode
                                )
                            )
                        )
                    )
                } else {
                    val response = connection.errorStream.bufferedReader()
                        .use { it.readText() }

                    try {
                        val apiError = response.toApiError()

                        continuation.resume(
                            handleOperationError(
                                apiError = apiError,
                                logInfo = logInfo.copy(
                                    error = SwedbankPayAPIError.Error(
                                        message = apiError.detail,
                                        responseCode = responseCode
                                    )
                                )
                            )
                        )
                    } catch (e: Exception) {
                        continuation.resume(
                            handleError(
                                logInfo = logInfo.copy(
                                    error = SwedbankPayAPIError.Error(
                                        message = e.localizedMessage,
                                        responseCode = responseCode
                                    )
                                )
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                val error = SwedbankPayAPIError.Error(message = e.localizedMessage)

                val logInfo = ApiCallLogInfo(
                    url = url.toString(),
                    method = "GET",
                    duration = System.currentTimeMillis() - start,
                    error = error
                )

                val response = when (e) {
                    // When not connected to internet we can get these exceptions.
                    // If so retry the call
                    is UnknownHostException,
                    is SocketTimeoutException,
                    is ConnectException,
                    is SocketException -> {
                        handleRetry(logInfo.copy(error = error))
                    }

                    else -> {
                        handleError(logInfo.copy(error = error))
                    }
                }

                continuation.resume(response)
            }
        } ?: kotlin.run {
            val error = SwedbankPayAPIError.InvalidUrl

            continuation.resume(
                handleError(
                    ApiCallLogInfo(
                        url = "",
                        method = "GET",
                        duration = System.currentTimeMillis() - start,
                        error = error
                    )
                )
            )
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

                val logInfo = ApiCallLogInfo(
                    url = requestUrl.toString(),
                    method = "POST",
                    duration = System.currentTimeMillis() - start,
                    responseStatusCode = responseCode
                )

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader()
                        .use { it.readText() }

                    continuation.resume(
                        handleSuccess(
                            paymentOutputModel = response.toPaymentOutputModel(),
                            logInfo = logInfo
                        )
                    )
                } else if ((500.until(600)).contains(responseCode)) {
                    // When we get a server error we want to retry the request
                    continuation.resume(
                        handleRetry(
                            logInfo = logInfo.copy(
                                error = SwedbankPayAPIError.Error(
                                    message = "Internal server error",
                                    responseCode = responseCode
                                )
                            )
                        )
                    )
                } else {
                    val response = connection.errorStream.bufferedReader()
                        .use { it.readText() }

                    try {
                        val apiError = response.toApiError()

                        continuation.resume(
                            handleOperationError(
                                apiError = apiError, logInfo.copy(
                                    error = SwedbankPayAPIError.Error(
                                        message = apiError.detail,
                                        responseCode = responseCode
                                    )
                                )
                            )
                        )
                    } catch (e: Exception) {
                        continuation.resume(
                            handleError(
                                logInfo.copy(
                                    error = SwedbankPayAPIError.Error(
                                        message = e.localizedMessage,
                                        responseCode = responseCode
                                    )
                                )
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                val error = SwedbankPayAPIError.Error(message = e.localizedMessage)
                val logInfo = ApiCallLogInfo(
                    url = url.toString(),
                    method = "POST",
                    duration = System.currentTimeMillis() - start,
                    error = error
                )
                val response = when (e) {
                    // When not connected to internet we can get these exceptions.
                    // If so retry the call
                    is UnknownHostException,
                    is SocketTimeoutException,
                    is ConnectException,
                    is SocketException -> {
                        handleRetry(logInfo.copy(error = error))
                    }

                    else -> {
                        handleError(logInfo.copy(error = error))
                    }
                }

                continuation.resume(response)
            }
        } ?: kotlin.run {
            val error = SwedbankPayAPIError.InvalidUrl

            continuation.resume(
                handleError(
                    ApiCallLogInfo(
                        url = "",
                        method = "POST",
                        duration = System.currentTimeMillis() - start,
                        error = error
                    )
                )
            )
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
                val logInfo = ApiCallLogInfo(
                    url = it,
                    method = "POST",
                    duration = System.currentTimeMillis() - start,
                    responseStatusCode = responseCode,
                )

                logAPICall(logInfo)

            } catch (e: Exception) {
                logAPICall(
                    ApiCallLogInfo(
                        url = it,
                        method = "POST",
                        duration = System.currentTimeMillis() - start,
                        error = SwedbankPayAPIError.Error(message = e.localizedMessage)
                    )
                )
            }
        }
    }

    private fun handleSuccess(
        paymentOutputModel: PaymentOutputModel,
        logInfo: ApiCallLogInfo
    ): PaymentSessionResponse {
        logAPICall(logInfo)
        return PaymentSessionResponse.Success(paymentOutputModel = paymentOutputModel)
    }

    private fun handleRetry(
        logInfo: ApiCallLogInfo
    ): PaymentSessionResponse {
        logAPICall(logInfo)
        return PaymentSessionResponse.Retry(
            SwedbankPayAPIError.Error(
                message = (logInfo.error as? SwedbankPayAPIError.Error)?.message,
                responseCode = logInfo.responseStatusCode
            )
        )
    }

    private fun handleOperationError(
        apiError: ApiError,
        logInfo: ApiCallLogInfo
    ): PaymentSessionResponse {
        logAPICall(logInfo)
        return PaymentSessionResponse.OperationError(apiError)
    }

    private fun handleError(
        logInfo: ApiCallLogInfo
    ): PaymentSessionResponse {
        logAPICall(logInfo)
        return PaymentSessionResponse.Error(logInfo.error ?: SwedbankPayAPIError.Unknown)
    }

    private fun logAPICall(
        logInfo: ApiCallLogInfo
    ) {
        BeaconService.logEvent(
            EventAction.HttpRequest(
                http = HttpModel(
                    requestUrl = logInfo.url,
                    method = logInfo.method,
                    responseStatusCode = logInfo.responseStatusCode,
                ),
                duration = logInfo.duration.toInt(),
                extensions = logInfo.error?.toExtensionsModel()
            )
        )
    }

    private data class ApiCallLogInfo(
        val url: String,
        val method: String,
        val duration: Long,
        val responseStatusCode: Int? = null,
        val error: SwedbankPayAPIError? = null
    )

}