package com.swedbankpay.mobilesdk.paymentsession.api.model

import androidx.annotation.Keep


@Keep
sealed class SwedbankPayAPIError {
    object Unknown : SwedbankPayAPIError()
    data class Error(val message: String? = null, val responseCode: Int? = null) :
        SwedbankPayAPIError()

    object InvalidUrl : SwedbankPayAPIError()
}