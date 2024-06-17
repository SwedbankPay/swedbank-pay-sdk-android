package com.swedbankpay.mobilesdk.paymentsession.api.model.response

import com.swedbankpay.mobilesdk.paymentsession.api.model.SwedbankPayAPIError

internal sealed class PaymentSessionResponse(
) {
    class Success(val paymentOutputModel: PaymentOutputModel) : PaymentSessionResponse()

    data class Error(val error: SwedbankPayAPIError) : PaymentSessionResponse()

    data class Retry(val error: SwedbankPayAPIError) : PaymentSessionResponse()

}

