package com.swedbankpay.mobilesdk.nativepayments.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class PaymentSession(
    @SerializedName("culture")
    val culture: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("methods")
    val methods: List<MethodBaseModel?>,
    @SerializedName("payeeName")
    val payeeName: String,
    @SerializedName("settings")
    val settings: Settings
)