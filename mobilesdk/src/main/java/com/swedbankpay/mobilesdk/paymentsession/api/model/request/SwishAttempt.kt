package com.swedbankpay.mobilesdk.paymentsession.api.model.request


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class SwishAttempt(
    @SerializedName("culture")
    val culture: String? = null,
    @SerializedName("msisdn")
    val msisdn: String? = null,
    @SerializedName("client")
    val client: Client
)
