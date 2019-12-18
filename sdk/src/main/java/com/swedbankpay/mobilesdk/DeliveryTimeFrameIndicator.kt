package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

@Suppress("unused")
enum class DeliveryTimeFrameIndicator {
    @SerializedName("01") ELECTRONIC_DELIVERY,
    @SerializedName("02") SAME_DAY_SHIPPING,
    @SerializedName("03") OVERNIGHT_SHIPPING,
    @SerializedName("04") TWO_DAY_OR_MORE_SHIPPING
}