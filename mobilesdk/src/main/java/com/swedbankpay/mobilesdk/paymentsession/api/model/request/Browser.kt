package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Browser(
    @SerializedName("acceptHeader")
    val acceptHeader: String?,
    @SerializedName("languageHeader")
    val languageHeader: String?,
    @SerializedName("timeZoneOffset")
    val timeZoneOffset: String?,
    @SerializedName("javascriptEnabled")
    val javascriptEnabled: Boolean?,
)