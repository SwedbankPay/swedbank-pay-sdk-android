package com.swedbankpay.mobilesdk.merchantbackend

/**
 * Reported as the [com.swedbankpay.mobilesdk.PaymentViewModel.RichState.updateException]
 * if the instrument was not valid for the payment order.
 */
class InvalidInstrumentException(
    val instrument: String,
    cause: Throwable?
) : Exception(cause)
