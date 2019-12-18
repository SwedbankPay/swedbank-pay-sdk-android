package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

@Suppress("unused")
enum class PurchaseIndicator {
    @SerializedName("01") MERCHANDISE_AVAILABLE,
    @SerializedName("02") FUTURE_AVAILABILITY
}