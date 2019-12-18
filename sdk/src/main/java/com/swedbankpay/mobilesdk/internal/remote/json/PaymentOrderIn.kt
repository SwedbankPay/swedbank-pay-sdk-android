package com.swedbankpay.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.remote.annotations.Required

internal class PaymentOrderIn {
    @SerializedName("operations")
    var operations = Operations()
}