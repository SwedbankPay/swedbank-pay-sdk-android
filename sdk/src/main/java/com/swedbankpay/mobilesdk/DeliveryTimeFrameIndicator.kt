package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

/**
 * Product delivery timeframe for a [RiskIndicator].
 *
 * See [https://developer.swedbankpay.com/checkout/payment-menu#request]
 */
@Suppress("unused")
enum class DeliveryTimeFrameIndicator {
    /**
     * Product is delivered electronically; no physical shipping.
     */
    @SerializedName("01") ELECTRONIC_DELIVERY,
    /**
     * Product is delivered on the same day.
     */
    @SerializedName("02") SAME_DAY_SHIPPING,
    /**
     * Product is delivered on the next day.
     */
    @SerializedName("03") OVERNIGHT_SHIPPING,
    /**
     * Product is delivered in two days or later.
     */
    @SerializedName("04") TWO_DAY_OR_MORE_SHIPPING
}