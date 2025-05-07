package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Browser(
    @SerializedName("acceptHeader")
    val acceptHeader: String?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("timeZoneOffset")
    val timeZoneOffset: Int?,
    @SerializedName("javascriptEnabled")
    val javascriptEnabled: Boolean?,
)