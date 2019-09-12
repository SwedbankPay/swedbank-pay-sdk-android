package com.payex.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName
import com.payex.mobilesdk.internal.remote.json.annotations.Required

internal class PaymentOrder {
    @SerializedName("url")
    var url: Link.PaymentOrder? = null
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