package com.swedbankpay.mobilesdk.paymentsession.api.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Settings(
    @SerializedName("allowCustomLogo")
    val allowCustomLogo: Boolean?,
    @SerializedName("allowCustomStyling")
    val allowCustomStyling: Boolean?,
    @SerializedName("integration")
    val integration: String?,
    @SerializedName("traceId")
    val traceId: String?,
    @SerializedName("enabledPaymentMethods")
    val enabledPaymentMethods: List<String>
)