package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

@Suppress("unused")
enum class PaymentOrderOperation {
    @SerializedName("Purchase") PURCHASE,
    @SerializedName("Verify") VERIFY
}