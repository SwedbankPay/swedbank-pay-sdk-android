package com.swedbankpay.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.remote.json.annotations.Required

internal class PaymentOrder {
    @Required
    @SerializedName("url")
    lateinit var url: Link.PaymentOrder

    @SerializedName("state")
    var state: String? = null

    @SerializedName("failureReason")
    var failureReason: String? = null

    @SerializedName("operations")
    var operations = Operations()
}