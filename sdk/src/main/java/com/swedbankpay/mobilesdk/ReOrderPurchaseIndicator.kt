package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

/**
 * Re-order purchase indicator values for [RiskIndicator].
 */
@Suppress("unused")
enum class ReOrderPurchaseIndicator {
    /**
     * First purchase of this merchandise
     */
    @SerializedName("01") FIRST_TIME_ORDERED,

    /**
     * Re-order of previously purchased merchandise
     */
    @SerializedName("02") REORDERED
}
