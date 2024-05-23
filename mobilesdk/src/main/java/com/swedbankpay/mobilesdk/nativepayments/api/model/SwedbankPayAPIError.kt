package com.swedbankpay.mobilesdk.nativepayments.api.model


sealed class SwedbankPayAPIError {
    object Unknown : SwedbankPayAPIError()
    data class Error(val message: String? = null, val responseCode: Int? = null) :
        SwedbankPayAPIError()

    object InvalidUrl : SwedbankPayAPIError()
}