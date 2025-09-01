package com.swedbankpay.mobilesdk

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NativeGooglePayAttemptPayload(
    @SerializedName("paymentOrderId")
    val paymentOrderId: String,
    @SerializedName("paymentMethod")
    val paymentMethod: String = "GooglePay",
    @SerializedName("paymentAttemptPayload")
    val paymentAttemptPayload: String?
)
