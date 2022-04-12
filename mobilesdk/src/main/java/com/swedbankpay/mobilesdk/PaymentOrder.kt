package com.swedbankpay.mobilesdk

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.checkBuilderNotNull
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Description a payment order.
 *
 * This class mirrors the body the Swedbank Pay
 * [POST /psp/paymentorders](https://developer.swedbankpay.com/checkout/other-features#creating-a-payment-order)
 * endpoint, and is designed to work together with
 * [MerchantBackendConfiguration][com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration]
 * and a server implementing the
 * [Merchant Backend API](https://https://developer.swedbankpay.com/modules-sdks/mobile-sdk/merchant-backend),
 * but you can also use it with your custom [Configuration].
 */
@Parcelize
data class PaymentOrder(
    /**
     * The operation to perform
     */
    @SerializedName("operation") val operation: PaymentOrderOperation = PaymentOrderOperation.PURCHASE,
    
    // To use v3 instead of v2, it must contain "Checkout3"
    @SerializedName("productName") var productName: String? = null,

    /**
     * Currency to use
     */
    @SerializedName("currency") val currency: Currency,

    /**
     * Payment amount, including VAT
     *
     * Denoted in the smallest monetary unit applicable, typically 1/100.
     * E.g. 50.00 SEK would be represented as `5000`.
     */
    @SerializedName("amount") val amount: Long,

    /**
     * Amount of VAT included in the payment
     *
     * Denoted in the smallest monetary unit applicable, typically 1/100.
     * E.g. 50.00 SEK would be represented as `5000`.
     */
    @SerializedName("vatAmount") val vatAmount: Long,

    /**
     * A description of the payment order
     */
    @SerializedName("description") val description: String,

    /**
     * User-agent of the payer.
     *
     * Defaults to `"SwedbankPaySDK-Android/{version}"`.
     */
    @SerializedName("userAgent") val userAgent: String = DEFAULT_USER_AGENT,

    /**
     * Language to use in the payment menu
     */
    @SerializedName("language") val language: Language = Language.ENGLISH,

    /**
     * The payment instrument to use in instrument mode.
     */
    @SerializedName("instrument") val instrument: String? = null,

    /**
     * If `true`, the a recurrence token will be created from this payment order
     *
     * The recurrence token should be retrieved by your server from Swedbank Pay.
     * Your server can then use the token for recurring server-to-server payments.
     */
    @SerializedName("generateRecurrenceToken") val generateRecurrenceToken: Boolean = false,
    /**
     * If `true`, the a unscheduled token will be created from this payment order. Unscheduled tokens 
     * differ from recurrence tokens as they are not meant to be recurring, but occur as singular transactions. 
     *
     * The unscheduled token should be retrieved by your server from Swedbank Pay.
     * Your server can then use the token for recurring server-to-server payments.
     */
    @SerializedName("generateUnscheduledToken") val generateUnscheduledToken: Boolean = false,
    
    /**
     * If `true`, a payment token will be created from this payment order
     *
     * You must also set [PaymentOrderPayer.payerReference] to generate a payment token.
     * The payment token can be used later to reuse the same payment details;
     * see [paymentToken].
     */
    @SerializedName("generatePaymentToken") val generatePaymentToken: Boolean = false,

    /**
     * If `true`, the payment menu will not show any stored payment details.
     *
     * This is useful mainly if you are implementing a custom UI for stored
     * payment details.
     */
    @SerializedName("disableStoredPaymentDetails") val disableStoredPaymentDetails: Boolean = false,

    /**
     * If set, only shows the specified payment instruments in the payment menu
     */
    @SerializedName("restrictedToInstruments") val restrictedToInstruments: List<String>? = null,

    /**
     * A set of URLs related to the payment.
     *
     * See [PaymentOrderUrls] for details.
     */
    @SerializedName("urls") val urls: PaymentOrderUrls,

    /**
     * Information about the payee (recipient)
     */
    @SerializedName("payeeInfo") val payeeInfo: PayeeInfo = PayeeInfo(),

    /**
     *  Information about the payer
     */
    @SerializedName("payer") val payer: PaymentOrderPayer? = null,

    /**
     * A list of items that are being paid for by this payment order.
     *
     * If used, the sum of the [OrderItem.amount] and [OrderItem.vatAmount] should match
     * [amount]` and [vatAmount] of this payment order.
     */
    @SerializedName("orderItems") val orderItems: List<OrderItem>? = null,

    /**
     * A collection of additional data to minimize the risk of 3-D Secure strong authentication.
     *
     * For best user experience, you should fill this field as completely as possible.
     */
    @SerializedName("riskIndicator") val riskIndicator: RiskIndicator? = null,
    @SerializedName("disablePaymentMenu") val disablePaymentMenu: Boolean = false,

    /**
     * A payment token to use for this payment.
     *
     * You must also set [PaymentOrderPayer.payerReference] to use a payment token;
     * the `payerReference` must match the one used when the payment token
     * was generated.
     */
    @SerializedName("paymentToken") val paymentToken: String? = null,
    @SerializedName("initiatingSystemUserAgent") val initiatingSystemUserAgent: String? = null,
) : Parcelable {
    companion object {
        /**
         * Default value for the [userAgent] property.
         *
         * Value is of the format "SwedbankPaySDK-Android/SDK_VERSION"
         */
        const val DEFAULT_USER_AGENT = "SwedbankPaySDK-Android/${BuildConfig.SDK_VERSION}"
        
        // Constant for the productName when using version 3
        const val CHECKOUT_3 = "Checkout3"
    }
    
    var isV3 
        get() = productName == CHECKOUT_3
        set(value) {
            productName = if (value) CHECKOUT_3 else null
        } 

    @Suppress("unused")
    class Builder {
        private var operation = PaymentOrderOperation.PURCHASE
        private var productName: String? = null
        private var currency: Currency? = null
        private var amount: Long? = null
        private var vatAmount: Long? = null
        private var description: String? = null
        private var userAgent = DEFAULT_USER_AGENT
        private var language = Language.ENGLISH
        private var instrument: String? = null
        private var generateRecurrenceToken = false
        private var generatePaymentToken = false
        private var disableStoredPaymentDetails = false
        private var restrictedToInstruments: List<String>? = null
        private var urls: PaymentOrderUrls? = null
        private var payeeInfo = PayeeInfo()
        private var payer: PaymentOrderPayer? = null
        private var orderItems: List<OrderItem>? = null
        private var riskIndicator: RiskIndicator? = null
        private var disablePaymentMenu = false
        private var paymentToken: String? = null
        private var initiatingSystemUserAgent: String? = null

        fun operation(operation: PaymentOrderOperation) = apply { this.operation = operation }
        fun productName(productName: String?) = apply { this.productName = productName }
        fun currency(currency: Currency) = apply { this.currency = currency }
        fun amount(amount: Long) = apply { this.amount = amount }
        fun vatAmount(vatAmount: Long) = apply { this.vatAmount = vatAmount }
        fun description(description: String) = apply { this.description = description }
        fun userAgent(userAgent: String) = apply { this.userAgent = userAgent }
        fun language(language: Language) = apply { this.language = language }
        fun instrument(instrument: String?) = apply { this.instrument = instrument }
        fun generateRecurrenceToken(generateRecurrenceToken: Boolean) = apply { this.generateRecurrenceToken = generateRecurrenceToken }
        fun generatePaymentToken(generatePaymentToken: Boolean) = apply { this.generatePaymentToken = generatePaymentToken }
        fun disableStoredPaymentDetails(disableStoredPaymentDetails: Boolean) = apply { this.disableStoredPaymentDetails = disableStoredPaymentDetails }
        fun restrictedToInstruments(restrictedToInstruments: List<String>?) = apply { this.restrictedToInstruments = restrictedToInstruments }
        fun urls(urls: PaymentOrderUrls) = apply { this.urls = urls }
        fun payeeInfo(payeeInfo: PayeeInfo) = apply { this.payeeInfo = payeeInfo }
        fun payer(payer: PaymentOrderPayer?) = apply { this.payer = payer }
        fun orderItems(orderItems: List<OrderItem>?) = apply { this.orderItems = orderItems }
        fun riskIndicator(riskIndicator: RiskIndicator?) = apply { this.riskIndicator = riskIndicator }
        fun disablePaymentMenu(disablePaymentMenu: Boolean) = apply { this.disablePaymentMenu = disablePaymentMenu }
        fun paymentToken(paymentToken: String?) = apply { this.paymentToken = paymentToken }
        fun initiatingSystemUserAgent(initiatingSystemUserAgent: String?) = apply { this.initiatingSystemUserAgent = initiatingSystemUserAgent }

        fun build() = PaymentOrder(
            operation = operation,
            productName = productName,
            currency = checkBuilderNotNull(currency, "currency"),
            amount = checkBuilderNotNull(amount, "amount"),
            vatAmount = checkBuilderNotNull(vatAmount, "vatAmount"),
            description = checkBuilderNotNull(description, "description"),
            userAgent = userAgent,
            language = language,
            instrument = instrument,
            generateRecurrenceToken = generateRecurrenceToken,
            generatePaymentToken = generatePaymentToken,
            disableStoredPaymentDetails = disableStoredPaymentDetails,
            restrictedToInstruments = restrictedToInstruments,
            urls = checkBuilderNotNull(urls, "urls"),
            payeeInfo = payeeInfo,
            payer = payer,
            orderItems = orderItems,
            riskIndicator = riskIndicator,
            disablePaymentMenu = disablePaymentMenu,
            paymentToken = paymentToken,
            initiatingSystemUserAgent = initiatingSystemUserAgent,
        )
    }
}