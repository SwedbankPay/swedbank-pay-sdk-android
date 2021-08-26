package com.swedbankpay.mobilesdk.merchantbackend.internal.remote.json

import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.merchantbackend.internal.remote.annotations.Required

internal class TopLevelResources {
    @Required
    @SerializedName("consumers")
    lateinit var consumers: Link.Consumers

    @Required
    @SerializedName("paymentorders")
    lateinit var paymentorders: Link.PaymentOrders
}