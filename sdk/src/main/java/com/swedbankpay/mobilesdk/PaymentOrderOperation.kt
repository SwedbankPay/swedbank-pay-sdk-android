package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

enum class PaymentOrderOperation {
    @SerializedName("Purchase") PURCHASE,
    @SerializedName("Verify") VERIFY
}