package com.swedbankpay.mobilesdk.paymentsession.util.configuration

import android.content.Context
import android.os.Parcelable
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.Consumer
import com.swedbankpay.mobilesdk.PaymentOrder
import com.swedbankpay.mobilesdk.ViewConsumerIdentificationInfo
import com.swedbankpay.mobilesdk.ViewPaymentOrderInfo
import kotlinx.parcelize.Parcelize

@Parcelize
internal class AutomaticConfiguration(private val orderInfo: ViewPaymentOrderInfo) :
    Configuration(), Parcelable {

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