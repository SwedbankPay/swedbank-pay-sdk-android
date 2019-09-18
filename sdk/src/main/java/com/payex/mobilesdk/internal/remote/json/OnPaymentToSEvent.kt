package com.payex.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName

internal class OnPaymentToSEvent {
    @SerializedName("openUrl")
    var openUrl: String? = null
}