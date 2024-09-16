package com.swedbankpay.mobilesdk.paymentsession.googlepay.util

import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ProblemDetails

internal object GooglePayErrorUtil {
    internal val cancelGooglePayProblem = ProblemDetails(
        type = "https://api.payex.com/psp/errordetail/usercancelled",
        title = "User cancelled",
        status = 403,
        detail = null,
        originalDetail = "The payer cancelled the payment in Google Pay.",
        operation = null
    )
}

