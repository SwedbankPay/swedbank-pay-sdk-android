package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class CreditCardCustomizePayment(
    @SerializedName("paymentMethod")
    val paymentMethod: String?,
    @SerializedName("hideStoredPaymentOptions")
    val hideStoredPaymentOptions: Boolean?,
    @SerializedName("showConsentAffirmation")
    val showConsentAffirmation: Boolean?,
    @SerializedName("restrictToPaymentMethods")
    val restrictToPaymentMethods: List<String?>?
)

@Keep
internal data class CustomizePayment(
    @SerializedName("paymentMethod")
    val paymentMethod: String?
)