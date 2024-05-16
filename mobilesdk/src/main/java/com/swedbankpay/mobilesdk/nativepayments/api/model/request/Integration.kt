package com.swedbankpay.mobilesdk.nativepayments.api.model.request

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * A class which contains the data needed for 'prepare-payment' request call
 */
@Keep
internal data class Integration(
    @SerializedName("integration")
    val integration: String,
    @SerializedName("deviceAcceptedWallets")
    val deviceAcceptedWallets: String,
    @SerializedName("client")
    val client: Client,
    @SerializedName("browser")
    val browser: Browser,
    @SerializedName("service")
    val service: Service,
    )

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

@Keep
data class Service(
    @SerializedName("name")
    val name: String?,
    @SerializedName("version")
    val version: String?,
)

