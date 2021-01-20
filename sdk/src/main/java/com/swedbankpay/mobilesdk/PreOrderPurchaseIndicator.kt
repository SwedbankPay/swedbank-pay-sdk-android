package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

/**
 * Purchase indicator values for [RiskIndicator].
 */
@Suppress("unused")
enum class PreOrderPurchaseIndicator {
    /**
     * Merchandise available now
     */
    @SerializedName("01") MERCHANDISE_AVAILABLE,

    /**
     * Merchandise will be available in the future
     */
    @SerializedName("02") FUTURE_AVAILABILITY
}