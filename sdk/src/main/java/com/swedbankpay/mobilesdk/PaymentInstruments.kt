package com.swedbankpay.mobilesdk

/**
 * Constant values for common payment instruments
 */
@Suppress("unused")
object PaymentInstruments {
    /**
     * Credit or Debit Card
     */
    const val CREDIT_CARD = "CreditCard"

    /**
     * Swish
     */
    const val SWISH = "Swish"

    /**
     * Vipps
     */
    const val VIPPS = "Vipps"

    /**
     * Swedbank Pay Invoice (Sweden)
     */
    const val INVOICE_SE = "Invoice-PayExFinancingSe"

    /**
     * Swedbank Pay Invoice (Norway)
     */
    const val INVOICE_NO = "Invoice-PayExFinancingNo"

    /**
     * Swedbank Pay Monthly Invoice (Sweden)
     */
    const val MONTHLY_INVOICE_SE = "Invoice-PayExMonthlyInvoiceSe"

    /**
     * Volvofinans CarPay
     */
    const val CAR_PAY = "CarPay"

    /**
     * Credit Account
     */
    const val CREDIT_ACCOUNT = "CreditAccount"
}

/**
 * Old name of INVOICE_SE
 */
@Suppress("unused")
@Deprecated(
    "Use PaymentInstruments.INVOICE_SE instead",
    ReplaceWith("INVOICE_SE")
)
val PaymentInstruments.INVOICE get() = INVOICE_SE
