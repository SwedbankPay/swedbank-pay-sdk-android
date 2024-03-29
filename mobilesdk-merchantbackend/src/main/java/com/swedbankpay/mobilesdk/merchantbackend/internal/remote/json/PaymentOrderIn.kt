package com.swedbankpay.mobilesdk.merchantbackend.internal.remote.json

import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.Operation

internal class PaymentOrderIn {
    @SerializedName("paymentOrder")
    var paymentOrder: PaymentOrder? = null

    @SerializedName("operations")
    var operations: List<Operation> = emptyList()

    @SerializedName("mobileSDK")
    var mobileSDK: MobileSDK? = null

    class PaymentOrder {
        @SerializedName("id")
        var id: String? = null
        @SerializedName("instrument")
        var instrument: String? = null
        @SerializedName("availableInstruments")
        var availableInstruments: List<String>? = null
    }

    class MobileSDK {
        @SerializedName("setInstrument")
        var setInstrument: Link.PaymentOrderSetInstrument? = null
    }
}