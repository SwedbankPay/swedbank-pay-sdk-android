package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

@Suppress("unused")
enum class ItemType {
    @SerializedName("PRODUCT") PRODUCT,
    @SerializedName("SERVICE") SERVICE,
    @SerializedName("SHIPPING_FEE") SHIPPING_FEE,
    @SerializedName("PAYMENT_FEE") PAYMENT_FEE,
    @SerializedName("DISCOUNT") DISCOUNT,
    @SerializedName("VALUE_CODE") VALUE_CODE,
    @SerializedName("OTHER") OTHER
}