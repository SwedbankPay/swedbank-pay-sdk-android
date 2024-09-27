package com.swedbankpay.mobilesdk.paymentsession.api.model.request.util

import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants.EXTENDED_REQUEST_TIME_OUT_IN_MS
import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants.EXTENDED_SESSION_TIME_OUT_IN_MS
import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants.REQUEST_TIME_OUT_IN_MS
import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants.SESSION_TIME_OUT_IN_MS
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.Instrument
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel

internal object TimeOutUtil {

    fun getRequestTimeout(operationRel: OperationRel?, instrument: Instrument?) =
        if ((operationRel == OperationRel.START_PAYMENT_ATTEMPT && instrument is Instrument.CreditCard)
            || operationRel == OperationRel.CREATE_AUTHENTICATION || operationRel == OperationRel.COMPLETE_AUTHENTICATION
            || operationRel == OperationRel.ATTEMPT_PAYLOAD
        ) {
            EXTENDED_REQUEST_TIME_OUT_IN_MS
        } else {
            REQUEST_TIME_OUT_IN_MS
        }

    fun getSessionTimeout(operationRel: OperationRel?, instrument: Instrument?) =
        if ((operationRel == OperationRel.START_PAYMENT_ATTEMPT && instrument is Instrument.CreditCard)
            || operationRel == OperationRel.CREATE_AUTHENTICATION || operationRel == OperationRel.COMPLETE_AUTHENTICATION
            || operationRel == OperationRel.ATTEMPT_PAYLOAD
        ) {
            EXTENDED_SESSION_TIME_OUT_IN_MS
        } else {
            SESSION_TIME_OUT_IN_MS
        }
}