package com.swedbankpay.mobilesdk.paymentsession.api.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants

internal enum class ApiErrorType {
    OPERATION_NOT_ALLOWED,
    GENERIC_OPERATION_ERROR,
    UNKNOWN
}

@Keep
internal data class ApiError(
    @SerializedName("type")
    val type: String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("detail")
    val detail: String?
)

internal fun ApiError.translatedType() =
    when (status) {
        409 -> {
            if (type == PaymentSessionAPIConstants.ABORT_PAYMENT_NOT_ALLOWED_TYPE) {
                ApiErrorType.OPERATION_NOT_ALLOWED
            } else {
                ApiErrorType.GENERIC_OPERATION_ERROR
            }
        }

        else -> ApiErrorType.UNKNOWN
    }
