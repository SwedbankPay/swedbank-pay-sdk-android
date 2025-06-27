package com.swedbankpay.mobilesdk

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ExpectationModel

@Keep
data class LaunchNativeGooglePayEvent(
    @SerializedName("event")
    val event: String,
    @SerializedName("paymentOrder")
    val paymentOrder: Id,
    @SerializedName("initParams")
    val initParams: List<ExpectationModel>? = null
)

@Keep
data class Id(
    @SerializedName("id")
    val id: String
)