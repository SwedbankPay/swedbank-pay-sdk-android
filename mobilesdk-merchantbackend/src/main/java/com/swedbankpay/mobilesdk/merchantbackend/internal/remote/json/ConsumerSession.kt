package com.swedbankpay.mobilesdk.merchantbackend.internal.remote.json

import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.Operation

internal class ConsumerSession {
    @SerializedName("operations")
    var operations: List<Operation> = emptyList()
}
