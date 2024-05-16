package com.swedbankpay.mobilesdk.nativepayments.api.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class Session(
    @SerializedName("operations")
    val operations: List<Operation>,
    @SerializedName("problem")
    val problem: Problem?,
    @SerializedName("paymentSession")
    val paymentSession: PaymentSession
)