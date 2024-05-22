package com.swedbankpay.mobilesdk.nativepayments.api.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ProblemDetailsWithOperation(
    @SerializedName("type")
    val type: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("detail")
    val detail: String?,
    @SerializedName("instance")
    val instance: String?,
    @SerializedName("operation")
    val operations: OperationOutputModel
)