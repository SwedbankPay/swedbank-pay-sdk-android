package com.swedbankpay.mobilesdk.nativepayments.model.response

internal sealed class NativePaymentResponse(
) {
    class Success(val session: Session) : NativePaymentResponse()
    class PaymentError(val paymentError: NativePaymentError) :
        NativePaymentResponse()

    class UnknownError(val message: String) : NativePaymentResponse()
}
