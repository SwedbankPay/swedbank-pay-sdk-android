package com.swedbankpay.mobilesdk.nativepayments.exposedmodel

import androidx.annotation.Keep

/**
 * Available instruments for native payments
 */
@Keep
sealed class AvailableInstrument {

    /**
     * Swish native payment with a list of prefills
     */
    data class Swish(
        val prefills: List<SwishPrefill> = listOf()
    ) : AvailableInstrument()

    /**
     * Credit card native payment with a list of prefills
     */
    data class CreditCard(
        val prefills: List<CreditCardPrefill> = listOf()
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
    val expiryDate: String?
)