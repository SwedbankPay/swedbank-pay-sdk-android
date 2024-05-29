package com.swedbankpay.mobilesdk.nativepayments.api.model.request.util

import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.BuildConfig
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.Browser
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.Client
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.InstrumentView
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.Integration
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.Service
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.SwishAttempt
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.OperationRel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.OperationRel.*

/**
 * [RequestUtil] will get request data for the different [OperationRel]:s
 */
internal object RequestUtil {

    private val gson = GsonBuilder().serializeNulls().create()

    fun OperationRel.getRequestDataIfAny(instrument: PaymentAttemptInstrument? = null) =
        when (this) {
            PREPARE_PAYMENT -> getIntegrationRequestData()
            START_PAYMENT_ATTEMPT -> getPaymentAttemptDataFor(instrument)
            EXPAND_METHOD -> getInstrumentViewsData(instrument)
            else -> null
        }

    private fun getIntegrationRequestData(): String {
        return Integration(
            integration = "HostedView",
            deviceAcceptedWallets = "",
            browser = Browser(
                acceptHeader = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
                languageHeader = RequestDataUtil.getLanguages(),
                timeZoneOffset = RequestDataUtil.getTimeZoneOffset(),
                javascriptEnabled = true
            ),
            client = RequestDataUtil.getClient(),
            service = RequestDataUtil.getService(),
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
                client = RequestDataUtil.getClient(),
                msisdn = instrument.msisdn
            ).toJsonString()

            else -> ""
        }
    }

    private inline fun <reified T : Any> T.toJsonString(): String = gson.toJson(this, T::class.java)
}

