package com.swedbankpay.mobilesdk.paymentsession.api.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

internal enum class FailPaymentAttemptProblemType(val identifier: String) {
    USER_CANCELLED("UserCancelled"),
    TECHNICAL_ERROR("TechnicalError")
}

@Keep
internal data class FailPaymentAttempt(
    @SerializedName("problemType")
    val problemType: String,

    @SerializedName("errorCode")
    val errorCode: String? = null
)
