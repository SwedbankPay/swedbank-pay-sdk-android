package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class CompleteAuthentication(
    @SerializedName("cRes")
    val cRes: String,
    @SerializedName("client")
    val client: ClientMinimum?,
)