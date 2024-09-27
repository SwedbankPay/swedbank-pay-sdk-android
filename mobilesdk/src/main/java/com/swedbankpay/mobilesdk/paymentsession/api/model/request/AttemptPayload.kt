package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import com.google.gson.annotations.SerializedName

internal data class AttemptPayload(
    @SerializedName("instrument")
    val instrument: String?,
    @SerializedName("paymentPayload")
    val paymentPayload: String?,
)