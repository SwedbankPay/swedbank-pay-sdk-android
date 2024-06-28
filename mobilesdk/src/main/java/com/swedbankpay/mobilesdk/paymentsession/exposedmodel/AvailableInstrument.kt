package com.swedbankpay.mobilesdk.paymentsession.exposedmodel

import androidx.annotation.Keep
import java.util.*

/**
 * Available instruments for native payments
 */
@Keep
sealed class AvailableInstrument {

    /**
     * Swish native payment with a list of prefills
     */
    data class Swish(
        val identifier: String,
        val prefills: List<SwishPrefill> = listOf(),
    ) : AvailableInstrument()

    /**
     * Credit card native payment with a list of prefills
     */
    data class CreditCard(
        val identifier: String,
        val prefills: List<CreditCardPrefill> = listOf()
    ) : AvailableInstrument()

    /**
     * Instruments that can be used for web based payments
     */
    data class WebBased(
        val identifier: String
    ) : AvailableInstrument()

}

/**
 * Prefill information for Swish payment
 */
@Keep
data class SwishPrefill(
    val rank: Int?,
    val msisdn: String?
)

/**
 * Prefill information for credit card payment
 */
@Keep
data class CreditCardPrefill(
    val rank: Int?,
    val paymentToken: String?,
    val cardBrand: String?,
    val maskedPan: String?,
    val expiryDate: Date?,
    val expiryMonth: String,
    val expiryYear: String,
    val expiryString: String
)