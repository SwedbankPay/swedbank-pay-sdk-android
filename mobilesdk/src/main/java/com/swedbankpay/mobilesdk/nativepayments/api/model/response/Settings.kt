package com.swedbankpay.mobilesdk.nativepayments.api.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Settings(
    @SerializedName("allowCustomLogo")
    val allowCustomLogo: Boolean?,
    @SerializedName("allowCustomStyling")
    val allowCustomStyling: Boolean?,
    @SerializedName("integration")
    val integration: String?,
    @SerializedName("traceId")
    val traceId: String?
)