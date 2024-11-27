package com.swedbankpay.mobilesdk.paymentsession.api.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Problem details returned with [SessionProblemOccurred]
 */
@Keep
data class ProblemDetails(
    @SerializedName("type")
    val type: String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("detail")
    val detail: String?,
    @SerializedName("originalDetail")
    val originalDetail: String?,
    @SerializedName("operation")
    internal val operation: OperationOutputModel?
)