package com.swedbankpay.mobilesdk.nativepayments.api.model.request.util

import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.CompleteAuthentication
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.CreateAuthentication
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.CreditCardAttempt
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.InstrumentView
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.Integration
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.SwishAttempt
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.OperationRel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.OperationRel.COMPLETE_AUTHENTICATION
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.OperationRel.CREATE_AUTHENTICATION
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.OperationRel.EXPAND_METHOD
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.OperationRel.PREPARE_PAYMENT
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.OperationRel.START_PAYMENT_ATTEMPT
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.PaymentAttemptInstrument

/**
 * [RequestUtil] will get request data for the different [OperationRel]:s
 */
internal object RequestUtil {

    private val gson = GsonBuilder().serializeNulls().create()

    const val NOTIFICATION_URL = "https://fake.payex.com/notification"

    fun OperationRel.getRequestDataIfAny(
        instrument: PaymentAttemptInstrument? = null,
        culture: String?,
        completionIndicator: String = "N",
        cRes: String = ""
    ) =
        when (this) {
            PREPARE_PAYMENT -> getIntegrationRequestData()
            START_PAYMENT_ATTEMPT -> getPaymentAttemptDataFor(instrument, culture)
            EXPAND_METHOD -> getInstrumentViewsData(instrument)
            CREATE_AUTHENTICATION -> getCreateAuthenticationData(completionIndicator)
            COMPLETE_AUTHENTICATION -> getCompleteAuthenticationData(cRes)
            else -> null
        }

    private fun getIntegrationRequestData(): String {
        return Integration(
            integration = "HostedView",
            deviceAcceptedWallets = "",
            browser = RequestDataUtil.getBrowser(),
            client = RequestDataUtil.getClient(),
            service = RequestDataUtil.getService(),
        ).toJsonString()
    }

    private fun getInstrumentViewsData(instrument: PaymentAttemptInstrument?): String {
        return InstrumentView(
            instrumentName = instrument?.name ?: ""
        ).toJsonString()

    }

    private fun getPaymentAttemptDataFor(
        instrument: PaymentAttemptInstrument?,
        culture: String?
    ): String {
        return when (instrument) {
            is PaymentAttemptInstrument.Swish -> SwishAttempt(
                culture = culture,
                client = RequestDataUtil.getClient(),
                msisdn = instrument.msisdn
            ).toJsonString()

            is PaymentAttemptInstrument.CreditCard -> CreditCardAttempt(
                culture = culture,
                client = RequestDataUtil.getClient(),
                paymentToken = instrument.prefill.paymentToken,
                cardNumber = instrument.prefill.maskedPan,
                cardExpiryMonth = instrument.prefill.expiryMonth,
                cardExpiryYear = instrument.prefill.expiryYear
            ).toJsonString()

            else -> ""
        }
    }

    private fun getCreateAuthenticationData(completionIndicator: String): String {
        return CreateAuthentication(
            methodCompletionIndicator = completionIndicator,
            notificationUrl = NOTIFICATION_URL,
            requestWindowSize = "FULLSCREEN",
            client = RequestDataUtil.getClient(),
            browser = RequestDataUtil.getBrowser()
        ).toJsonString()
    }

    private fun getCompleteAuthenticationData(cRes: String): String {
        return CompleteAuthentication(
            cRes = cRes,
            client = RequestDataUtil.getClient()
        ).toJsonString()
    }

    private inline fun <reified T : Any> T.toJsonString(): String = gson.toJson(this, T::class.java)
}

