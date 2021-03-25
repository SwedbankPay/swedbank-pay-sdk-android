package com.swedbankpay.mobilesdk

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.checkBuilderNotNull
import kotlinx.parcelize.Parcelize

/**
 * An item being paid for, part of a [PaymentOrder].
 *
 * OrderItems are an optional, but recommended, part of PaymentOrders.
 * To use them, create an OrderItem for each distinct item the paymentorder
 * is for: e.g. if the consumer is paying for one Thingamajig and two
 * Watchamacallits, which will be shipped to the consumer's address,
 * you would create three OrderItems: one for the lone Thingamajig,
 * one for the two Watchamacallits, and one for the shipping fee.
 *
 * When using OrderItems, make sure that the sum of the OrderItems'
 * amount and vatAmount are equal to the PaymentOrder's amount
 * and vatAmount properties, respectively.
 */
@Parcelize
data class OrderItem(
    /**
     * A reference that identifies the item in your own systems.
     */
    @SerializedName("reference") val reference: String,
    /**
     * Name of the item
     */
    @SerializedName("name") val name: String,
    /**
     * Type of the item
     */
    @SerializedName("type") val type: ItemType,
    /**
     * A classification of the item. Must not contain spaces.
     *
     * Can be used for assigning the order item to a specific product category,
     * such as <code>"MobilePhone"</code>.
     *
     * Swedbank Pay may use this field for statistics.
     */
    @SerializedName("class") @get:JvmName("getItemClass") val `class`: String,
    /**
     * URL of a web page that contains information about the item
     */
    @SerializedName("itemUrl") val itemUrl: String? = null,
    /**
     * URL to an image of the item
     */
    @SerializedName("imageUrl") val imageUrl: String? = null,
    /**
     * Human-friendly description of the item
     */
    @SerializedName("description") val description: String? = null,
    /**
     * Human-friendly description of the discount on the item, if applicable
     */
    @SerializedName("discountDescription") val discountDescription: String? = null,
    /**
     * Quantity of the item being purchased
     */
    @SerializedName("quantity") val quantity: Int,
    /**
     * Unit of the quantity
     *
     * E.g. <code>"pcs"</code>, <code>"grams"</code>
     */
    @SerializedName("quantityUnit") val quantityUnit: String,
    /**
     * Price of a single unit, including VAT.
     */
    @SerializedName("unitPrice") val unitPrice: Long,
    /**
     * The discounted price of the item, if applicable
     */
    @SerializedName("discountPrice") val discountPrice: Long? = null,
    /**
     * The VAT percent value, multiplied by 100.
     *
     * E.g. 25% would be represented as <code>2500</code>.
     */
    @SerializedName("vatPercent") val vatPercent: Int,
    /**
     * The total amount, including VAT, paid for the specified quantity of the item.
     *
     * Denoted in the smallest monetary unit applicable, typically 1/100.
     * E.g. 50.00 SEK would be represented as <code>5000</code>.
     */
    @SerializedName("amount") val amount: Long,
    /**
     * The total amount of VAT paid for the specified quantity of the item.
     *
     * Denoted in the smallest monetary unit applicable, typically 1/100.
     * E.g. 50.00 SEK would be represented as <code>5000</code>.
     */
    @SerializedName("vatAmount") val vatAmount: Long
) : Parcelable {
    @Suppress("unused")
    class Builder {
        private var reference: String? = null
        private var name: String? = null
        private var type: ItemType? = null
        private var `class`: String? = null
        private var itemUrl: String? = null
        private var imageUrl: String? = null
        private var description: String? = null
        private var discountDescription: String? = null
        private var quantity: Int? = null
        private var quantityUnit: String? = null
        private var unitPrice: Long? = null
        private var discountPrice: Long? = null
        private var vatPercent: Int? = null
        private var amount: Long? = null
        private var vatAmount: Long? = null

        fun reference(reference: String) = apply { this.reference = reference }
        fun name(name: String) = apply { this.name = name }
        fun type(type: ItemType) = apply { this.type = type }
        @JvmName("itemClass") fun `class`(`class`: String) = apply { this.`class` = `class` }
        fun itemUrl(itemUrl: String?) = apply { this.itemUrl = itemUrl }
        fun imageUrl(imageUrl: String?) = apply { this.imageUrl = imageUrl }
        fun description(description: String?) = apply { this.description = description }
        fun discountDescription(discountDescription: String?) = apply { this.discountDescription = discountDescription }
        fun quantity(quantity: Int) = apply { this.quantity = quantity }
        fun quantityUnit(quantityUnit: String) = apply { this.quantityUnit = quantityUnit }
        fun unitPrice(unitPrice: Long) = apply { this.unitPrice = unitPrice }
        fun discountPrice(discountPrice: Long?) = apply { this.discountPrice = discountPrice }
        fun vatPercent(vatPercent: Int) = apply { this.vatPercent = vatPercent }
        fun amount(amount: Long) = apply { this.amount = amount }
        fun vatAmount(vatAmount: Long) = apply { this.vatAmount = vatAmount }

        fun build() = OrderItem(
            reference = checkBuilderNotNull(reference, "reference"),
            name = checkBuilderNotNull(name, "name"),
            type = checkBuilderNotNull(type, "type"),
            `class` = checkBuilderNotNull(`class`, "class"),
            itemUrl = itemUrl,
            imageUrl = imageUrl,
            description = description,
            discountDescription = discountDescription,
            quantity = checkBuilderNotNull(quantity, "quantity"),
            quantityUnit = checkBuilderNotNull(quantityUnit, "quantityUnit"),
            unitPrice = checkBuilderNotNull(unitPrice, "unitPrice"),
            discountPrice = discountPrice,
            vatPercent = checkBuilderNotNull(vatPercent, "vatPercent"),
            amount = checkBuilderNotNull(amount, "amount"),
            vatAmount = checkBuilderNotNull(vatAmount, "vatAmount")
        )
    }
}