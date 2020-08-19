package com.swedbankpay.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName

internal class PaymentOrderIn {
    @SerializedName("operations")
    var operations = Operations()
}