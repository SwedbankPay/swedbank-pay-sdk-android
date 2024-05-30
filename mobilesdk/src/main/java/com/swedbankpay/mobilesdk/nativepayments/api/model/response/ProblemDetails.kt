package com.swedbankpay.mobilesdk.nativepayments.api.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * Problem details returned with [SessionProblemOccurred]
 */
@Keep
data class ProblemDetails(
    @SerializedName("title")
    val title: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("detail")
    val detail: String?,
    @SerializedName("operation")
    internal val operations: OperationOutputModel
)