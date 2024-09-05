package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
internal data class GooglePayAttempt(
    @SerializedName("culture")
    val culture: String? = null,
    @SerializedName("client")
    val client: Client
)
