package com.swedbankpay.mobilesdk.paymentsession.api.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class PaymentOutputModel(
    @SerializedName("operations")
    val operations: List<OperationOutputModel>,
    @SerializedName("problem")
    val problem: ProblemDetails?,
    @SerializedName("paymentSession")
    val paymentSession: PaymentSessionModel
)