package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class CreditCardAttempt(
    @SerializedName("culture")
    val culture: String? = null,
    @SerializedName("client")
    val client: Client,
    @SerializedName("paymentToken")
    val paymentToken: String?,
    @SerializedName("cardNumber")
    val cardNumber: String?,
    @SerializedName("cardExpiryMonth")
    val cardExpiryMonth: String?,
    @SerializedName("cardExpiryYear")
    val cardExpiryYear: String?,
)
