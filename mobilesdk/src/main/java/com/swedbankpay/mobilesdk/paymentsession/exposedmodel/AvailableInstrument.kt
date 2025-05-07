package com.swedbankpay.mobilesdk.paymentsession.exposedmodel

import androidx.annotation.Keep
import java.util.*

/**
 * Available instruments for native payments
 */
@Keep
sealed class AvailableInstrument {

    abstract val paymentMethod: String

    /**
     * Swish native payment with a list of prefills
     */
    data class Swish(
        override val paymentMethod: String,
        val prefills: List<SwishPrefill> = listOf(),
    ) : AvailableInstrument()

    /**
     * Credit card native payment with a list of prefills
     */
    data class CreditCard(
        override val paymentMethod: String,
        val prefills: List<CreditCardPrefill> = listOf()
    ) : AvailableInstrument()

    /**
     * Google pay native payments
     * @param isReadyToPay Boolean indicating if users device has the ability to make Google pay payments
     * @param isReadyToPayWithExistingPaymentMethod  Boolean indicating if user has at least one card available for payment in current payment session
     */
    data class GooglePay(
        override val paymentMethod: String,
        val isReadyToPay: Boolean = false,
        val isReadyToPayWithExistingPaymentMethod: Boolean = false
    ) : AvailableInstrument()

    /**
     * Instrument telling merchants that new credit card can be used
     */
    data class NewCreditCard(
        override val paymentMethod: String
    ) : AvailableInstrument()

    /**
     * Instruments that can be used for web based payments
     */
    data class WebBased(
        override val paymentMethod: String
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