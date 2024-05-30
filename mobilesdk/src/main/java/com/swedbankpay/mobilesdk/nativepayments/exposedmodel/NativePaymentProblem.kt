package com.swedbankpay.mobilesdk.nativepayments.exposedmodel

import com.swedbankpay.mobilesdk.nativepayments.api.model.SwedbankPayAPIError

/**
 * Native payment problem returned with [SdkProblemOccurred]
 */
sealed class NativePaymentProblem {
    object PaymentSessionEndReached : NativePaymentProblem()
    data class PaymentSessionAPIRequestFailed(
        val error: SwedbankPayAPIError,
        val retry: () -> Unit
    ) :
        NativePaymentProblem()

    object ClientAppLaunchFailed : NativePaymentProblem()

    object InternalInconsistencyError: NativePaymentProblem()
}