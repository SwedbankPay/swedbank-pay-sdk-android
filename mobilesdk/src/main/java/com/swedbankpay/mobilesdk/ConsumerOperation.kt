package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

/**
 * Operations that can be performed with a [Consumer].
 *
 * Currently limited to starting an identification session.
 *
 * See [https://developer.swedbankpay.com/checkout/checkin#checkin-back-end].
 */
enum class ConsumerOperation {
    /**
     * Operation: Start a consumer identification session
     */
    @SerializedName("initiate-consumer-session") INITIATE_CONSUMER_SESSION
}