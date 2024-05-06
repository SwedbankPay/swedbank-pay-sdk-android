package com.swedbankpay.mobilesdk.nativepayments.exposedmodel

sealed class AvailableInstrument {

    data class Swish(
        val prefill: List<SwishPrefill> = listOf()
    ) : AvailableInstrument()

    data class CreditCard(
        val prefill: List<CreditCardPrefill> = listOf()
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
    val maskedPen: String?,
    val expiryDate: String?
)