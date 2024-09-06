package com.swedbankpay.mobilesdk.paymentsession.api.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

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
    internal val operations: OperationOutputModel
)