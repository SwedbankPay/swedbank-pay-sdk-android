package com.swedbankpay.mobilesdk.nativepayments.api.model.response

internal sealed class NativePaymentResponse(
) {
    class Success(val paymentOutputModel: PaymentOutputModel) : NativePaymentResponse()
    class PaymentError(val paymentError: NativePaymentError) :
        NativePaymentResponse()

    class UnknownError(val message: String) : NativePaymentResponse()

    object Retry : NativePaymentResponse()

}

