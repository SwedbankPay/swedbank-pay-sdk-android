package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class CustomizePayment(
    @SerializedName("paymentMethod")
    val paymentMethod: String?,
    @SerializedName("hideStoredPaymentOptions")
    val hideStoredPaymentOptions: Boolean?,
    @SerializedName("showConsentAffirmation")
    val showConsentAffirmation: Boolean?,
    @SerializedName("restrictToPaymentMethods")
    val restrictToPaymentMethods: List<String?>?
)