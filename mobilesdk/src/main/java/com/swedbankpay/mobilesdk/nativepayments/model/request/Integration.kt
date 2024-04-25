package com.swedbankpay.mobilesdk.nativepayments.model.request

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * A class which contains the data needed for 'prepare-payment' request call
 */
@Keep
data class Integration(
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
    val acceptHeader: Any?,
    @SerializedName("colorDepth")
    val colorDepth: Any?,
    @SerializedName("languageHeader")
    val languageHeader: Any?,
    @SerializedName("screenHeight")
    val screenHeight: Any?,
    @SerializedName("screenWidth")
    val screenWidth: Any?,
    @SerializedName("timeZoneOffset")
    val timeZoneOffset: Any?
)


