package com.swedbankpay.mobilesdk.nativepayments.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Task(
    @SerializedName("contentType")
    val contentType: String,
    @SerializedName("expects")
    val expects: List<Any>,
    @SerializedName("href")
    val href: String,
    @SerializedName("method")
    val method: RequestMethod,
    @SerializedName("rel")
    val rel: Rel
)