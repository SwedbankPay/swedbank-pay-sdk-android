package com.swedbankpay.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName

internal class OnExternalRedirectEvent {
    @SerializedName("urlTemplate")
    var urlTemplate: String? = null
}