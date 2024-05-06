package com.swedbankpay.mobilesdk.nativepayments.model.request

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * A class which contains the data needed for 'prepare-payment' request call
 */
@Keep
internal data class Integration(
    @SerializedName("browser")
    val browser: Browser,
    @SerializedName("client")
    val client: Client,
    @SerializedName("deviceAcceptedWallets")
    val deviceAcceptedWallets: String,
    @SerializedName("initiatingSystem")
    val initiatingSystem: String,
    @SerializedName("initiatingSystemVersion")
    val initiatingSystemVersion: String,
    @SerializedName("integration")
    val integration: String
)

@Keep
data class Browser(
    @SerializedName("acceptHeader")
    val acceptHeader: String?,
    @SerializedName("colorDepth")
    val colorDepth: String?,
    @SerializedName("languageHeader")
    val languageHeader: String?,
    @SerializedName("screenHeight")
    val screenHeight: String?,
    @SerializedName("screenWidth")
    val screenWidth: String?,
    @SerializedName("timeZoneOffset")
    val timeZoneOffset: String?
)


