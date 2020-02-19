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


data class PaymentOrder(
    @SerializedName("operation") val operation: PaymentOrderOperation = Defaults.operation,
    @SerializedName("currency") val currency: Currency,
    @SerializedName("amount") val amount: Long,
    @SerializedName("vatAmount") val vatAmount: Long,
    @SerializedName("description") val description: String,
    @SerializedName("userAgent") val userAgent: String = Defaults.userAgent,
    @SerializedName("language") val language: Language = Defaults.language,
    @SerializedName("generateRecurrenceToken") val generateRecurrenceToken: Boolean = Defaults.generateRecurrenceToken,
    @SerializedName("restrictedToInstruments") val restrictedToInstruments: List<String>? = Defaults.restrictedToInstruments,
    @SerializedName("urls") val urls: PaymentOrderUrls,
    @SerializedName("payeeInfo") val payeeInfo: PayeeInfo = Defaults.payeeInfo,
    @SerializedName("payer") val payer: PaymentOrderPayer? = Defaults.payer,
    @SerializedName("orderItems") val orderItems: List<OrderItem>? = Defaults.orderItems,
    @SerializedName("riskIndicator") val riskIndicator: RiskIndicator? = Defaults.riskIndicator,
    @SerializedName("disablePaymentMenu") val disablePaymentMenu: Boolean = Defaults.disablePaymentMenu,

    // /** @hide */
    @Transient override val extensionProperties: Bundle? = null
) : Parcelable,
    ExtensibleJsonObject {
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = makeCreator(::PaymentOrder)
    }

    @Suppress("unused")
    class Builder {
        private var operation = Defaults.operation
        private var currency: Currency? = null
        private var amount: Long? = null
        private var vatAmount: Long? = null
        private var description: String? = null
        private var userAgent = Defaults.userAgent
        private var language = Defaults.language
        private var generateRecurrenceToken = Defaults.generateRecurrenceToken
        private var restrictedToInstruments = Defaults.restrictedToInstruments
        private var urls: PaymentOrderUrls? = null
        private var payeeInfo = Defaults.payeeInfo
        private var payer = Defaults.payer
        private var orderItems = Defaults.orderItems
        private var riskIndicator = Defaults.riskIndicator
        private var extensionProperties: Bundle? = null
        private var disablePaymentMenu = Defaults.disablePaymentMenu

        fun operation(operation: PaymentOrderOperation) = apply { this.operation = operation }
        fun currency(currency: Currency) = apply { this.currency = currency }
        fun amount(amount: Long) = apply { this.amount = amount }
        fun vatAmount(vatAmount: Long) = apply { this.vatAmount = vatAmount }
        fun description(description: String) = apply { this.description = description }
        fun userAgent(userAgent: String) = apply { this.userAgent = userAgent }
        fun language(language: Language) = apply { this.language = language }
        fun generateRecurrenceToken(generateRecurrenceToken: Boolean) = apply { this.generateRecurrenceToken = generateRecurrenceToken }
        fun restrictedToInstruments(restrictedToInstruments: List<String>?) = apply { this.restrictedToInstruments = restrictedToInstruments }
        fun urls(urls: PaymentOrderUrls) = apply { this.urls = urls }
        fun payeeInfo(payeeInfo: PayeeInfo) = apply { this.payeeInfo = payeeInfo }
        fun payer(payer: PaymentOrderPayer?) = apply { this.payer = payer }
        fun orderItems(orderItems: List<OrderItem>?) = apply { this.orderItems = orderItems }
        fun riskIndicator(riskIndicator: RiskIndicator?) = apply { this.riskIndicator = riskIndicator }
        fun disablePaymentMenu(disablePaymentMenu: Boolean) = apply { this.disablePaymentMenu = disablePaymentMenu }

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
            generateRecurrenceToken = generateRecurrenceToken,
            restrictedToInstruments = restrictedToInstruments,
            urls = checkBuilderNotNull(urls, "urls"),
            payeeInfo = payeeInfo,
            payer = payer,
            orderItems = orderItems,
            riskIndicator = riskIndicator,
            disablePaymentMenu = disablePaymentMenu,

            extensionProperties = extensionProperties
        )
    }

    private object Defaults {
        val operation = PaymentOrderOperation.PURCHASE
        const val userAgent = "SwedbankPaySDK-Android/${BuildConfig.VERSION_NAME}"
        val language = Language.ENGLISH
        const val generateRecurrenceToken = false
        val restrictedToInstruments: List<String>? = null
        val payeeInfo = PayeeInfo()
        val payer: PaymentOrderPayer? = null
        val orderItems: List<OrderItem>? = null
        val riskIndicator: RiskIndicator? = null
        const val disablePaymentMenu = false
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
            writeStringList(restrictedToInstruments)
            writeParcelable(urls, flags)
            writeParcelable(payeeInfo, flags)
            writeParcelable(payer, flags)
            writeTypedList(orderItems)
            writeParcelable(riskIndicator, flags)
            writeBooleanCompat(disablePaymentMenu)

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
        restrictedToInstruments = parcel.createStringArrayList(),
        urls = checkNotNull(parcel.readParcelable()),
        payeeInfo = checkNotNull(parcel.readParcelable()),
        payer = parcel.readParcelable(),
        orderItems = parcel.createTypedArrayList(OrderItem.CREATOR),
        riskIndicator = parcel.readParcelable(),
        disablePaymentMenu = parcel.readBooleanCompat(),

        extensionProperties = parcel.readBundle(PaymentOrder::class.java.classLoader)
    )
}