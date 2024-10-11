package com.swedbankpay.mobilesdk.paymentsession.api.model.request.util

import android.util.Base64
import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.AttemptPayload
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.CompleteAuthentication
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.CreateAuthentication
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.CreditCardAttempt
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.CustomizePayment
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.GooglePayAttempt
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.InstrumentView
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.Integration
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.ResetCustomizePayment
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.SwishAttempt
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel.ATTEMPT_PAYLOAD
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel.COMPLETE_AUTHENTICATION
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel.CREATE_AUTHENTICATION
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel.CUSTOMIZE_PAYMENT
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel.EXPAND_METHOD
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel.PREPARE_PAYMENT
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel.START_PAYMENT_ATTEMPT
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.paymentsession.googlepay.model.GooglePayResult

/**
 * [RequestUtil] will get request data for the different [OperationRel]:s
 */
internal object RequestUtil {

    private val gson = GsonBuilder().serializeNulls().create()

    fun OperationRel.getRequestDataIfAny(
        instrument: PaymentAttemptInstrument? = null,
        culture: String? = null,
        completionIndicator: String = "N",
        notificationUrl: String = "",
        cRes: String = "",
        showConsentAffirmation: Boolean = false,
        resetPaymentMethod: Boolean = false,
        googlePayResult: GooglePayResult? = null
    ) =
        when (this) {
            PREPARE_PAYMENT -> getIntegrationRequestData()
            START_PAYMENT_ATTEMPT -> getPaymentAttemptDataFor(instrument, culture)
            EXPAND_METHOD -> getInstrumentViewsData(instrument)
            CREATE_AUTHENTICATION -> getCreateAuthenticationData(
                completionIndicator,
                notificationUrl
            )

            COMPLETE_AUTHENTICATION -> getCompleteAuthenticationData(cRes)
            CUSTOMIZE_PAYMENT -> getCustomizePaymentData(
                instrument,
                showConsentAffirmation,
                resetPaymentMethod = resetPaymentMethod
            )

            ATTEMPT_PAYLOAD -> getAttemptPayloadData(googlePayResult)
            else -> null
        }

    private fun getIntegrationRequestData(): String {
        return Integration(
            integration = "HostedView",
            deviceAcceptedWallets = "GooglePay",
            browser = RequestDataUtil.getBrowser(),
            client = RequestDataUtil.getClient(),
            service = RequestDataUtil.getService(),
        ).toJsonString()
    }

    private fun getInstrumentViewsData(instrument: PaymentAttemptInstrument?): String {
        return InstrumentView(
            instrumentName = instrument?.identifier ?: ""
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

            is PaymentAttemptInstrument.GooglePay -> GooglePayAttempt(
                culture = culture,
                client = RequestDataUtil.getClient()
            ).toJsonString()

            else -> ""
        }
    }

    private fun getCreateAuthenticationData(
        completionIndicator: String,
        notificationUrl: String
    ): String {
        return CreateAuthentication(
            methodCompletionIndicator = completionIndicator,
            notificationUrl = notificationUrl,
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

    private fun getCustomizePaymentData(
        instrument: PaymentAttemptInstrument? = null,
        showConsentAffirmation: Boolean? = null,
        instrumentFilter: List<String>? = null,
        resetPaymentMethod: Boolean = false
    ): String {
        return if (resetPaymentMethod) {
            ResetCustomizePayment(null).toJsonString()
        } else {
            CustomizePayment(
                paymentMethod = instrument?.identifier ?: "Menu",
                hideStoredPaymentOptions = if (instrument != null) true else null,
                showConsentAffirmation = showConsentAffirmation,
                restrictToPaymentMethods = instrumentFilter
            ).toJsonString()
        }
    }

    private fun getAttemptPayloadData(googlePayResult: GooglePayResult?): String {
        return AttemptPayload(
            instrument = "GooglePay",
            paymentPayload = googlePayResult?.paymentMethodData?.tokenizationData?.token?.toBase64(),
        ).toJsonString()
    }

    private fun String.toBase64() = Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)

    private inline fun <reified T : Any> T.toJsonString(): String = gson.toJson(this, T::class.java)
}

