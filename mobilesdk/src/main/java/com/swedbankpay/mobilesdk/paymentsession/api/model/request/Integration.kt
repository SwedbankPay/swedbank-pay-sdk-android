package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

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
    @SerializedName("presentationSdk")
    val presentationSdk: PresentationSdk
)

@Keep
data class Service(
    @SerializedName("name")
    val name: String?,
    @SerializedName("version")
    val version: String?,
)

@Keep
data class PresentationSdk(
    @SerializedName("name")
    val name: String?,
    @SerializedName("version")
    val version: String?,
)

