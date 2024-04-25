package com.swedbankpay.mobilesdk.nativepayments.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Client(
    @SerializedName("ipAddress")
    val ipAddress: String,
    @SerializedName("userAgent")
    val userAgent: String
)

@Keep
data class SwishClient(
    @SerializedName("ipAddress")
    val ipAddress: String,
)