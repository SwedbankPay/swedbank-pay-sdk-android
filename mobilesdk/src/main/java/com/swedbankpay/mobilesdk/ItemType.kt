package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

/**
 * The type of an [OrderItem].
 *
 * See [https://developer.swedbankpay.com/checkout/payment-menu#request].
 */
@Suppress("unused")
enum class ItemType {
    /**
     * A product delivered to the customer (physical or electronic)
     */
    @SerializedName("PRODUCT") PRODUCT,
    /**
     * A service rendered for the customer
     */
    @SerializedName("SERVICE") SERVICE,
    /**
     * A shipping fee
     */
    @SerializedName("SHIPPING_FEE") SHIPPING_FEE,
    /**
     * A fee for using a particular payment method
     */
    @SerializedName("PAYMENT_FEE") PAYMENT_FEE,
    /**
     * A discount
     */
    @SerializedName("DISCOUNT") DISCOUNT,
    /**
     * A value/campaign/coupon code
     */
    @SerializedName("VALUE_CODE") VALUE_CODE,
    /**
     * An item that does not fit in the above categories
     */
    @SerializedName("OTHER") OTHER
}