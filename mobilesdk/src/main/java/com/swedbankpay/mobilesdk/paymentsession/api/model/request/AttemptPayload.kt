package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import com.google.gson.annotations.SerializedName

internal data class AttemptPayload(
    @SerializedName("paymentMethod")
    val paymentMethod: String?,
    @SerializedName("paymentPayload")
    val paymentPayload: String?,
)