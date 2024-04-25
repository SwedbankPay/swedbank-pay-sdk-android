package com.swedbankpay.mobilesdk.nativepayments.model.request


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class SwishAttempt(
    @SerializedName("culture")
    val culture: String,
    @SerializedName("msisdn")
    val msisdn: String? = null,
    @SerializedName("client")
    val client: SwishClient
)
