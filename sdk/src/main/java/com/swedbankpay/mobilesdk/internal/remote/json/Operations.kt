package com.swedbankpay.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName

internal class Operations : ArrayList<Operation>() {
    fun find(rel: String) = firstOrNull { it.rel == rel }
}

internal class Operation {
    @SerializedName("rel")
    var rel: String? = null
    @SerializedName("href")
    var href: String? = null
}