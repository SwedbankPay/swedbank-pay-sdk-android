package com.swedbankpay.mobilesdk.nativepayments.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class NativePaymentError(
    @SerializedName("detail")
    val detail: String,
    @SerializedName("instance")
    val instance: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String
)