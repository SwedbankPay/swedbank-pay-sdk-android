package com.swedbankpay.mobilesdk.nativepayments.api.model.request.util

import com.swedbankpay.mobilesdk.nativepayments.api.model.response.Instrument
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.OperationRel

internal object TimeOutUtil {

    private const val REQUEST_TIME_OUT_IN_MS = 10 * 1000
    private const val REQUEST_TIME_OUT_IN_MS_FOR_CREDIT_CARD = 30 * 1000

    private const val SESSION_TIME_OUT_IN_MS = 20 * 1000
    private const val SESSION_TIME_OUT_IN_MS_FOR_CREDIT_CARD = 30 * 1000

    fun getRequestTimeout(operationRel: OperationRel?, instrument: Instrument?) =
        if ((operationRel == OperationRel.START_PAYMENT_ATTEMPT && instrument == Instrument.CREDIT_CARD)
            || operationRel == OperationRel.CREATE_AUTHENTICATION || operationRel == OperationRel.COMPLETE_AUTHENTICATION
        ) {
            REQUEST_TIME_OUT_IN_MS_FOR_CREDIT_CARD
        } else {
            REQUEST_TIME_OUT_IN_MS
        }

    fun getSessionTimeout(operationRel: OperationRel?, instrument: Instrument?) =
        if ((operationRel == OperationRel.START_PAYMENT_ATTEMPT && instrument == Instrument.CREDIT_CARD)
            || operationRel == OperationRel.CREATE_AUTHENTICATION || operationRel == OperationRel.COMPLETE_AUTHENTICATION
        ) {
            SESSION_TIME_OUT_IN_MS_FOR_CREDIT_CARD
        } else {
            SESSION_TIME_OUT_IN_MS
        }
}