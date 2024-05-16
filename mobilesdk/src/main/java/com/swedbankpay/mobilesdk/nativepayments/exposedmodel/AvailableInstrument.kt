package com.swedbankpay.mobilesdk.nativepayments.exposedmodel

sealed class AvailableInstrument {

    data class Swish(
        val prefills: List<SwishPrefill> = listOf()
    ) : AvailableInstrument()

    data class CreditCard(
        val prefills: List<CreditCardPrefill> = listOf()
    ) : AvailableInstrument()

}

data class SwishPrefill(
    val rank: Int?,
    val msisdn: String?
)

data class CreditCardPrefill(
    val rank: Int?,
    val paymentToken: String?,
    val cardBrand: String?,
    val maskedPan: String?,
    val expiryDate: String?
)