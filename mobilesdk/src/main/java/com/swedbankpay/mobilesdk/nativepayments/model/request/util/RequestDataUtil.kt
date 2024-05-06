package com.swedbankpay.mobilesdk.nativepayments.model.request.util

import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.nativepayments.model.request.Browser
import com.swedbankpay.mobilesdk.nativepayments.model.request.Client
import com.swedbankpay.mobilesdk.nativepayments.model.request.InstrumentView
import com.swedbankpay.mobilesdk.nativepayments.model.request.Integration
import com.swedbankpay.mobilesdk.nativepayments.model.request.SwishAttempt
import com.swedbankpay.mobilesdk.nativepayments.model.request.SwishClient
import com.swedbankpay.mobilesdk.nativepayments.model.response.Rel

internal object RequestDataUtil {

    private val gson = GsonBuilder().serializeNulls().create()

    fun Rel.getRequestDataIfAny(instrument: PaymentAttemptInstrument? = null) =
        when (this) {
            Rel.PREPARE_PAYMENT -> getIntegrationRequestData()
            Rel.START_PAYMENT_ATTEMPT -> getPaymentAttemptDataFor(instrument)
            Rel.EXPAND_METHOD -> getInstrumentViewsData(instrument)
            else -> null
        }

    private fun getIntegrationRequestData(): String {
        return Integration(
            browser = Browser(
                acceptHeader = null,
                colorDepth = "24",
                languageHeader = null,
                screenHeight = IntegrationRequestDataUtil.getPhoneSize().heightPixels.toString(),
                screenWidth = IntegrationRequestDataUtil.getPhoneSize().widthPixels.toString(),
                timeZoneOffset = IntegrationRequestDataUtil.getTimeZoneOffset()
            ),
            client = Client(
                ipAddress = IntegrationRequestDataUtil.getLocalIpAddress() ?: "" ,
                userAgent = "swedbank-pay-sdk-android"
            ),
            deviceAcceptedWallets = "",
            initiatingSystem = "swedbank-pay-sdk-android",
            initiatingSystemVersion = IntegrationRequestDataUtil.getVersion(),
            integration = "HostedView"

        ).toJsonString()
    }

    private fun getInstrumentViewsData(instrument: PaymentAttemptInstrument?): String {
        return when (instrument) {
            is PaymentAttemptInstrument.Swish -> InstrumentView(
                instrumentName = "Swish"
            ).toJsonString()

            else -> ""
        }

    }

    private fun getPaymentAttemptDataFor(instrument: PaymentAttemptInstrument?): String {
        return when (instrument) {
            is PaymentAttemptInstrument.Swish -> SwishAttempt(
                culture = "sv-SE",
                client = SwishClient(
                    ipAddress = IntegrationRequestDataUtil.getLocalIpAddress() ?: "",
                ),
                msisdn = instrument.msisdn
            ).toJsonString()

            else -> ""
        }
    }

    private inline fun <reified T : Any> T.toJsonString(): String = gson.toJson(this, T::class.java)
}

