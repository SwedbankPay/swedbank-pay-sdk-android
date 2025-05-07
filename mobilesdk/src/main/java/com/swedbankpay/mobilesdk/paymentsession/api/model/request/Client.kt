package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class Client(
    @SerializedName("userAgent")
    val userAgent: String,
    @SerializedName("screenHeight")
    val screenHeight: Int?,
    @SerializedName("screenWidth")
    val screenWidth: Int?,
    @SerializedName("screenColorDepth")
    val screenColorDepth: Int?,
)

@Keep
internal data class ClientWithType(
    @SerializedName("userAgent")
    val userAgent: String,
    @SerializedName("screenHeight")
    val screenHeight: Int?,
    @SerializedName("screenWidth")
    val screenWidth: Int?,
    @SerializedName("screenColorDepth")
    val screenColorDepth: Int?,
    @SerializedName("clientType")
    val clientType: String
)

@Keep
internal data class ClientMinimum(
    @SerializedName("userAgent")
    val userAgent: String,
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

