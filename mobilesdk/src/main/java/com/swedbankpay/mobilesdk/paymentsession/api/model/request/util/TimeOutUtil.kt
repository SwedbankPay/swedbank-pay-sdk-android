package com.swedbankpay.mobilesdk.paymentsession.api.model.request.util

import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants.REQUEST_TIME_OUT_IN_MS
import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants.REQUEST_TIME_OUT_IN_MS_FOR_CREDIT_CARD
import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants.SESSION_TIME_OUT_IN_MS
import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants.SESSION_TIME_OUT_IN_MS_FOR_CREDIT_CARD
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.Instrument
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel

internal object TimeOutUtil {

    fun getRequestTimeout(operationRel: OperationRel?, instrument: Instrument?) =
        if ((operationRel == OperationRel.START_PAYMENT_ATTEMPT && instrument is Instrument.CreditCard)
            || operationRel == OperationRel.CREATE_AUTHENTICATION || operationRel == OperationRel.COMPLETE_AUTHENTICATION
        ) {
            REQUEST_TIME_OUT_IN_MS_FOR_CREDIT_CARD
        } else {
            REQUEST_TIME_OUT_IN_MS
        }

    fun getSessionTimeout(operationRel: OperationRel?, instrument: Instrument?) =
        if ((operationRel == OperationRel.START_PAYMENT_ATTEMPT && instrument is Instrument.CreditCard)
            || operationRel == OperationRel.CREATE_AUTHENTICATION || operationRel == OperationRel.COMPLETE_AUTHENTICATION
        ) {
            SESSION_TIME_OUT_IN_MS_FOR_CREDIT_CARD
        } else {
            SESSION_TIME_OUT_IN_MS
        }
}