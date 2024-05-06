package com.swedbankpay.mobilesdk.nativepayments.api

import com.swedbankpay.mobilesdk.nativepayments.OperationStep
import com.swedbankpay.mobilesdk.nativepayments.model.response.NativePaymentResponse
import com.swedbankpay.mobilesdk.nativepayments.model.response.RequestMethod
import com.swedbankpay.mobilesdk.nativepayments.util.JsonUtil.toPaymentErrorModel
import com.swedbankpay.mobilesdk.nativepayments.util.JsonUtil.toSessionModel
import java.io.OutputStreamWriter
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
        }

        return suspendCoroutine {
            it.resume(paymentResponse)
        }
    }

    private suspend fun getRequest(
        url: URL?
    ): NativePaymentResponse = suspendCoroutine { continuation ->
        url?.let {
            val connection = url.openConnection() as HttpsURLConnection

            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            connection.doInput = true
            connection.doOutput = false

            val responseCode = connection.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                try {
                    val response = connection.inputStream.bufferedReader()
                        .use { it.readText() }

                    continuation.resume(
                        NativePaymentResponse.Success(session = response.toSessionModel())
                    )
                } catch (e: Exception) {
                    continuation.resume(
                        NativePaymentResponse.UnknownError(
                            e.message ?: "Unknown error"
                        )
                    )
                }
            } else {
                val errorResponse = connection.errorStream.bufferedReader()
                    .use { it.readText() }

                continuation.resume(
                    NativePaymentResponse.PaymentError(
                        paymentError = errorResponse.toPaymentErrorModel()
                    )
                )
            }
        } ?: continuation.resume(NativePaymentResponse.UnknownError("Url not found"))

    }

    private suspend fun postRequest(
        url: URL?,
        data: String
    ): NativePaymentResponse = suspendCoroutine { continuation ->
        url?.let {
            val connection = url.openConnection() as HttpsURLConnection

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
                try {
                    val response = connection.inputStream.bufferedReader()
                        .use { it.readText() }
                    continuation.resume(NativePaymentResponse.Success(response.toSessionModel()))
                } catch (e: Exception) {
                    continuation.resume(
                        NativePaymentResponse.UnknownError(
                            e.message ?: "Unknown error"
                        )
                    )
                }
            } else {
                val errorResponse = connection.errorStream.bufferedReader()
                    .use { it.readText() }
                continuation.resume(NativePaymentResponse.PaymentError(errorResponse.toPaymentErrorModel()))
            }
        }

    }

}