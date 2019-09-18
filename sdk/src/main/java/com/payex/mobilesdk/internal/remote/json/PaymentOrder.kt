package com.payex.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName
import com.payex.mobilesdk.internal.remote.json.annotations.Required

internal class PaymentOrder {
    @Required
    @SerializedName("url")
    lateinit var url: Link.PaymentOrder
        private set

    @SerializedName("state")
    var state: String? = null
        private set

    @SerializedName("failureReason")
    var failureReason: String? = null
        private set

    @SerializedName("operations")
    var operations = Operations()
        private set
}