package com.swedbankpay.mobilesdk.nativepayments.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class Client(
    @SerializedName("ipAddress")
    val ipAddress: String,
    @SerializedName("userAgent")
    val userAgent: String
)

@Keep
internal data class SwishClient(
    @SerializedName("ipAddress")
    val ipAddress: String,
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