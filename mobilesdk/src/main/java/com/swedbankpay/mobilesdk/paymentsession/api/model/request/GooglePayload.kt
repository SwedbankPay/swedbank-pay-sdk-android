package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import com.google.gson.annotations.SerializedName
import se.vettefors.googlepaytest.model.ShippingAddress

internal data class GooglePayload(
    @SerializedName("instrument")
    val instrument: String?,
    @SerializedName("cardNetwork")
    val cardNetwork: String?,
    @SerializedName("paymentPayload")
    val payLoad: String?,
    @SerializedName("cardDetails")
    val cardDetails: String?,
    @SerializedName("browser")
    val shippingAddress: ShippingAddress?,
)