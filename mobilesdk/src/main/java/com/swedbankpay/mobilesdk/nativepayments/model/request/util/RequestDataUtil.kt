package com.swedbankpay.mobilesdk.nativepayments.model.request.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.nativepayments.model.request.Browser
import com.swedbankpay.mobilesdk.nativepayments.model.request.Client
import com.swedbankpay.mobilesdk.nativepayments.model.request.InstrumentView
import com.swedbankpay.mobilesdk.nativepayments.model.request.Integration
import com.swedbankpay.mobilesdk.nativepayments.model.request.SwishAttempt
import com.swedbankpay.mobilesdk.nativepayments.model.request.SwishClient
import com.swedbankpay.mobilesdk.nativepayments.model.response.Instrument
import com.swedbankpay.mobilesdk.nativepayments.model.response.Rel


object RequestDataUtil {

    private val gson = GsonBuilder().serializeNulls().create()

    fun Rel.getRequestDataIfAny(instrument: Instrument? = null) =
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
                colorDepth = null,
                languageHeader = null,
                screenHeight = null,
                screenWidth = null,
                timeZoneOffset = null
            ),
            client = Client(
                ipAddress = "192.168.0.1",
                userAgent = "mobile-sdk"
            ),
            deviceAcceptedWallets = "GooglePay",
            initiatingSystem = "mobile-sdk",
            initiatingSystemVersion = "0.1",
            integration = "HostedView"

        ).toJsonString()
    }

    private fun getInstrumentViewsData(instrument: Instrument?): String {
        return when (instrument) {
            Instrument.SWISH -> InstrumentView(
                instrumentName = instrument.name
            ).toJsonString()

            else -> ""
        }

    }

    private fun getPaymentAttemptDataFor(instrument: Instrument?): String {
        return when (instrument) {
            Instrument.SWISH -> SwishAttempt(
                culture = "sv-SE",
                client = SwishClient(
                    ipAddress = "192.168.0.1",
                )
            ).toJsonString()

            else -> ""
        }
    }

    private inline fun <reified T : Any> T.toJsonString(): String = gson.toJson(this, T::class.java)
}

