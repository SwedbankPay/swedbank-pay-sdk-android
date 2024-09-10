package com.swedbankpay.mobilesdk.paymentsession.api.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class PaymentOutputModel(
    @SerializedName("operations")
    val operations: List<OperationOutputModel?>,
    @SerializedName("problem")
    val problem: ProblemDetails?,
    @SerializedName("paymentSession")
    val paymentSession: PaymentSessionModel
)