package com.swedbankpay.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.remote.json.annotations.Required

internal class TopLevelResources {
    @Required
    @SerializedName("consumers")
    lateinit var consumers: Link.Consumers
        private set

    @Required
    @SerializedName("paymentorders")
    lateinit var paymentorders: Link.PaymentOrders
        private set
}