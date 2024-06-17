package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class CreateAuthentication(
    @SerializedName("methodCompletionIndicator")
    val methodCompletionIndicator: String?,
    @SerializedName("notificationUrl")
    val notificationUrl: String?,
    @SerializedName("requestWindowSize")
    val requestWindowSize: String?,
    @SerializedName("client")
    val client: Client?,
    @SerializedName("browser")
    val browser: Browser?,
)
