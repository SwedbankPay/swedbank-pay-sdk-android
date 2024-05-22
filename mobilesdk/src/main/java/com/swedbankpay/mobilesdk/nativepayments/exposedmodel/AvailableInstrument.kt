package com.swedbankpay.mobilesdk.nativepayments.exposedmodel

import androidx.annotation.Keep

@Keep
sealed class AvailableInstrument {

    data class Swish(
        val prefills: List<SwishPrefill> = listOf()
    ) : AvailableInstrument()

    data class CreditCard(
        val prefills: List<CreditCardPrefill> = listOf()
    ) : AvailableInstrument()

}

@Keep
data class SwishPrefill(
    val rank: Int?,
    val msisdn: String?
)

@Keep
data class CreditCardPrefill(
    val rank: Int?,
    val paymentToken: String?,
    val cardBrand: String?,
    val maskedPan: String?,
    val expiryDate: String?
)