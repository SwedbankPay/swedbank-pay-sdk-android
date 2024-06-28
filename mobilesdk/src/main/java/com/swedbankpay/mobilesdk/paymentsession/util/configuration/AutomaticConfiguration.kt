package com.swedbankpay.mobilesdk.paymentsession.util.configuration

import android.content.Context
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.Consumer
import com.swedbankpay.mobilesdk.PaymentOrder
import com.swedbankpay.mobilesdk.ViewConsumerIdentificationInfo
import com.swedbankpay.mobilesdk.ViewPaymentOrderInfo

internal class AutomaticConfiguration(private val orderInfo: ViewPaymentOrderInfo) : Configuration() {
    override suspend fun postConsumers(
        context: Context,
        consumer: Consumer?,
        userData: Any?
    ): ViewConsumerIdentificationInfo {
        throw Exception()
    }

    override suspend fun postPaymentorders(
        context: Context,
        paymentOrder: PaymentOrder?,
        userData: Any?,
        consumerProfileRef: String?
    ): ViewPaymentOrderInfo = orderInfo

}