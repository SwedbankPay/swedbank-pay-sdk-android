package com.swedbankpay.mobilesdk

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.*
import com.swedbankpay.mobilesdk.internal.checkBuilderNotNull
import com.swedbankpay.mobilesdk.internal.readEnum
import com.swedbankpay.mobilesdk.internal.remote.ExtensibleJsonObject
import com.swedbankpay.mobilesdk.internal.writeEnum
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
 *
 * Please refer to the Swedbank Pay documentation for the meaning of the payment order properties.
 */
data class PaymentOrder(
    @SerializedName("operation") val operation: PaymentOrderOperation = PaymentOrderOperation.PURCHASE,
    @SerializedName("currency") val currency: Currency,
    @SerializedName("amount") val amount: Long,
    @SerializedName("vatAmount") val vatAmount: Long,
    @SerializedName("description") val description: String,
    @SerializedName("userAgent") val userAgent: String = DEFAULT_USER_AGENT,
    @SerializedName("language") val language: Language = Language.ENGLISH,
    @SerializedName("instrument") val instrument: String? = null,
    @SerializedName("generateRecurrenceToken") val generateRecurrenceToken: Boolean = false,
    @SerializedName("generatePaymentToken") val generatePaymentToken: Boolean = false,
    @SerializedName("disableStoredPaymentDetails") val disableStoredPaymentDetails: Boolean = false,
    @SerializedName("restrictedToInstruments") val restrictedToInstruments: List<String>? = null,
    @SerializedName("urls") val urls: PaymentOrderUrls,
    @SerializedName("payeeInfo") val payeeInfo: PayeeInfo = PayeeInfo(),
    @SerializedName("payer") val payer: PaymentOrderPayer? = null,
    @SerializedName("orderItems") val orderItems: List<OrderItem>? = null,
    @SerializedName("riskIndicator") val riskIndicator: RiskIndicator? = null,
    @SerializedName("disablePaymentMenu") val disablePaymentMenu: Boolean = false,
    @SerializedName("recurrenceToken") val recurrenceToken: String? = null,
    @SerializedName("paymentToken") val paymentToken: String? = null,

    /** @hide */
    @Transient override val extensionProperties: Bundle? = null
) : Parcelable, ExtensibleJsonObject {
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = makeCreator(::PaymentOrder)

        /**
         * Default value for the [userAgent] property.
         *
         * Value is of the format "SwedbankPaySDK-Android/SDK_VERSION"
         */
        const val DEFAULT_USER_AGENT = "SwedbankPaySDK-Android/${BuildConfig.SDK_VERSION}"
    }

    @Suppress("unused")
    class Builder {
        private var operation = PaymentOrderOperation.PURCHASE
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
        private var extensionProperties: Bundle? = null
        private var disablePaymentMenu = false
        private var recurrenceToken: String? = null
        private var paymentToken: String? = null

        fun operation(operation: PaymentOrderOperation) = apply { this.operation = operation }
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
        fun recurrenceToken(recurrenceToken: String?) = apply { this.recurrenceToken = recurrenceToken }
        fun paymentToken(paymentToken: String?) = apply { this.paymentToken = paymentToken }

        /** @hide */
        fun extensionProperties(extensionProperties: Bundle?) = apply { this.extensionProperties = extensionProperties }

        fun build() = PaymentOrder(
            operation = operation,
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
            recurrenceToken = recurrenceToken,
            paymentToken = paymentToken,

            extensionProperties = extensionProperties
        )
    }

    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.apply {
            writeEnum(operation)
            writeString(currency.currencyCode)
            writeLong(amount)
            writeLong(vatAmount)
            writeString(description)
            writeString(userAgent)
            writeEnum(language)
            writeBooleanCompat(generateRecurrenceToken)
            writeBooleanCompat(generatePaymentToken)
            writeBooleanCompat(disableStoredPaymentDetails)
            writeStringList(restrictedToInstruments)
            writeParcelable(urls, flags)
            writeParcelable(payeeInfo, flags)
            writeParcelable(payer, flags)
            writeTypedList(orderItems)
            writeParcelable(riskIndicator, flags)
            writeBooleanCompat(disablePaymentMenu)
            writeString(recurrenceToken)
            writeString(paymentToken)

            writeBundle(extensionProperties)
        }
    }
    private constructor(parcel: Parcel) : this(
        operation = checkNotNull(parcel.readEnum<PaymentOrderOperation>()),
        currency = Currency.getInstance(checkNotNull(parcel.readString())),
        amount = parcel.readLong(),
        vatAmount = parcel.readLong(),
        description = checkNotNull(parcel.readString()),
        userAgent = checkNotNull(parcel.readString()),
        language = checkNotNull(parcel.readEnum<Language>()),
        generateRecurrenceToken = parcel.readBooleanCompat(),
        generatePaymentToken = parcel.readBooleanCompat(),
        disableStoredPaymentDetails = parcel.readBooleanCompat(),
        restrictedToInstruments = parcel.createStringArrayList(),
        urls = checkNotNull(parcel.readParcelable()),
        payeeInfo = checkNotNull(parcel.readParcelable()),
        payer = parcel.readParcelable(),
        orderItems = parcel.createTypedArrayList(OrderItem.CREATOR),
        riskIndicator = parcel.readParcelable(),
        disablePaymentMenu = parcel.readBooleanCompat(),
        recurrenceToken = parcel.readString(),
        paymentToken = parcel.readString(),

        extensionProperties = parcel.readBundle(PaymentOrder::class.java.classLoader)
    )
}