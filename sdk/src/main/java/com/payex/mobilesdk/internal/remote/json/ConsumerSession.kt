package com.payex.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName

internal class ConsumerSession {
    @SerializedName("operations")
    var operations = Operations()
        private set
}
