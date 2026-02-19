package com.swedbankpay.mobilesdk.paymentsession.util.configuration

import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.PaymentFragment
import com.swedbankpay.mobilesdk.ViewPaymentOrderInfo
import com.swedbankpay.mobilesdk.internal.getParcelableInternal

class AutomaticConfigurationPaymentFragment : PaymentFragment() {
    override fun getConfiguration(): Configuration {
        return arguments?.getParcelableInternal(ARG_USER_DATA, AutomaticConfiguration::class.java)
            ?: AutomaticConfiguration(
                ViewPaymentOrderInfo(
                    viewPaymentLink = "",
                    webViewBaseUrl = "",
                    completeUrl = "",
                    cancelUrl = "",
                    paymentUrl = "",
                    isV3 = true
                )
            )
    }
}