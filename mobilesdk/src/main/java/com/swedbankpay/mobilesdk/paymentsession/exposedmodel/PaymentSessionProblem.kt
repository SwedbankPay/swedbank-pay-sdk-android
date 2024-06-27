package com.swedbankpay.mobilesdk.paymentsession.exposedmodel

import com.swedbankpay.mobilesdk.paymentsession.api.model.SwedbankPayAPIError

/**
 * Payment session problem returned with [SdkProblemOccurred]
 */
sealed class PaymentSessionProblem {
    object PaymentSessionEndReached : PaymentSessionProblem()
    data class PaymentSessionAPIRequestFailed(
        val error: SwedbankPayAPIError,
        val retry: () -> Unit
    ) : PaymentSessionProblem()

    data class PaymentSession3DSecureFragmentLoadFailed(
        val error: SwedbankPayAPIError.Error,
        val retry: () -> Unit
    ) : PaymentSessionProblem()

    object ClientAppLaunchFailed : PaymentSessionProblem()

    object InternalInconsistencyError : PaymentSessionProblem()

    object AutomaticConfigurationFailed : PaymentSessionProblem()
}