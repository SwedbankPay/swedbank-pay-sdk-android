package com.swedbankpay.mobilesdk.nativepayments.api.model.response

import com.swedbankpay.mobilesdk.nativepayments.api.model.SwedbankPayAPIError

internal sealed class NativePaymentResponse(
) {
    class Success(val paymentOutputModel: PaymentOutputModel) : NativePaymentResponse()

    data class Error(val error: SwedbankPayAPIError) : NativePaymentResponse()

    data class Retry(val error: SwedbankPayAPIError) : NativePaymentResponse()

}

