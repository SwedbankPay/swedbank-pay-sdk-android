package com.swedbankpay.mobilesdk.paymentsession.api.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UrlsModel(
    @SerializedName("completeUrl")
    val completeUrl: String? = null,
    @SerializedName("cancelUrl")
    val cancelUrl: String? = null,
    @SerializedName("paymentUrl")
    val paymentUrl: String? = null,
    @SerializedName("hostUrls")
    val hostUrls: List<String>? = null,
    @SerializedName("termsOfServiceUrl")
    val termsOfServiceUrl: String? = null
)