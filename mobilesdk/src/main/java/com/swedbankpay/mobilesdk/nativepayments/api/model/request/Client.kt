package com.swedbankpay.mobilesdk.nativepayments.api.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class Client(
    @SerializedName("userAgent")
    val userAgent: String,
    @SerializedName("ipAddress")
    val ipAddress: String,
    @SerializedName("screenHeight")
    val screenHeight: String?,
    @SerializedName("screenWidth")
    val screenWidth: String?,
    @SerializedName("screenColorDepth")
    val screenColorDepth: Int?,
)

@Keep
internal data class Version(
    val major: Int,
    val minor: Int,
    val build: Int,
    val revision: Int,
    val majorRevision: Int,
    val minorRevision: Int,
)