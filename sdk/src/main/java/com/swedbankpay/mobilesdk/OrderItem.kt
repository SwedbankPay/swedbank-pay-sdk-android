package com.swedbankpay.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.*
import com.swedbankpay.mobilesdk.internal.checkBuilderNotNull
import com.swedbankpay.mobilesdk.internal.makeCreator
import com.swedbankpay.mobilesdk.internal.readEnum
import com.swedbankpay.mobilesdk.internal.writeEnum

@Suppress("unused")
data class OrderItem(
    @SerializedName("reference") val reference: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: ItemType,
    @SerializedName("class") @get:JvmName("getItemClass") val `class`: String,
    @SerializedName("itemUrl") val itemUrl: String? = null,
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("discountDescription") val discountDescription: String? = null,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("quantityUnit") val quantityUnit: String,
    @SerializedName("unitPrice") val unitPrice: Long,
    @SerializedName("discountPrice") val discountPrice: Long? = null,
    @SerializedName("vatPercent") val vatPercent: Int,
    @SerializedName("amount") val amount: Long,
    @SerializedName("vatAmount") val vatAmount: Long
) : Parcelable {
    companion object {
        @JvmField val CREATOR = makeCreator(::OrderItem)
    }

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

    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.apply {
            writeString(reference)
            writeString(name)
            writeEnum(type)
            writeString(`class`)
            writeString(itemUrl)
            writeString(imageUrl)
            writeString(description)
            writeString(discountDescription)
            writeInt(quantity)
            writeString(quantityUnit)
            writeLong(unitPrice)
            writeOptionalLong(discountPrice)
            writeInt(vatPercent)
            writeLong(amount)
            writeLong(vatAmount)
        }
    }
    private constructor(parcel: Parcel) : this(
        reference = checkNotNull(parcel.readString()),
        name = checkNotNull(parcel.readString()),
        type = checkNotNull(parcel.readEnum<ItemType>()),
        `class` = checkNotNull(parcel.readString()),
        itemUrl = parcel.readString(),
        imageUrl = parcel.readString(),
        description = parcel.readString(),
        discountDescription = parcel.readString(),
        quantity = parcel.readInt(),
        quantityUnit = checkNotNull(parcel.readString()),
        unitPrice = parcel.readLong(),
        discountPrice = parcel.readOptionalLong(),
        vatPercent = parcel.readInt(),
        amount = parcel.readLong(),
        vatAmount = parcel.readLong()
    )
}