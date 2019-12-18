package com.swedbankpay.mobilesdk

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.*
import com.swedbankpay.mobilesdk.internal.readEnum
import com.swedbankpay.mobilesdk.internal.readOptionalBoolean
import com.swedbankpay.mobilesdk.internal.writeEnum
import com.swedbankpay.mobilesdk.internal.writeOptionalBoolean
import org.joda.time.ReadablePartial
import org.joda.time.format.DateTimeFormat
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

@Suppress("unused")
data class RiskIndicator(
    @SerializedName("deliveryEmailAddress") val deliveryEmailAddress: String?,
    @SerializedName("deliveryTimeFrameIndicator") val deliveryTimeFrameIndicator: DeliveryTimeFrameIndicator?,
    @SerializedName("preOrderDate") val preOrderDate: String?,
    @SerializedName("preOrderPurchaseIndicator") val preOrderPurchaseIndicator: PurchaseIndicator?,
    @SerializedName("shipIndicator") val shipIndicator: String?,
    @SerializedName("pickUpAddress") val pickUpAddress: PickUpAddress?,
    @SerializedName("giftCardPurchase") val giftCardPurchase: Boolean?,
    @SerializedName("reOrderPurchaseIndicator") val reOrderPurchaseIndicator: PurchaseIndicator?
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR = makeCreator(::RiskIndicator)

        private fun formatPreOrderDate(date: Any): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && date is java.time.temporal.TemporalAccessor) {
                formatJavaPreOrderDate(date)
            } else when (date) {
                is Long, is Calendar, is Date -> formatJavaPreOrderDate(date)
                is org.threeten.bp.temporal.TemporalAccessor -> formatThreeTenBpPreOrderDate(date)
                is ReadablePartial -> formatJodaPreOrderDate(date)
                is String -> date
                else -> throw IllegalArgumentException("Unexpected type: ${date.javaClass} (expected java.time.temporal.TemporalAccessor, Long, Calendar, Date, org.threeten.bp.temporal.TemporalAccessor, org.joda.time.ReadablePartial, String; Recommend using java.time.LocalDate or org.threeten.bp.LocalDate)")
            }
        }

        private fun formatJavaPreOrderDate(date: Any) =
            String.format(Locale.US, "%1\$tY%1\$tm%1\$td", date)

        private fun formatThreeTenBpPreOrderDate(date: org.threeten.bp.temporal.TemporalAccessor) =
            DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US).format(date)

        private fun formatJodaPreOrderDate(date: ReadablePartial) =
            DateTimeFormat.forPattern("yyyyMMdd").withLocale(Locale.US).print(date)
    }

    constructor(
        deliveryEmailAddress: String? = null,
        deliveryTimeFrameIndicator: DeliveryTimeFrameIndicator? = null,
        preOrderDate: Any? = null,
        preOrderPurchaseIndicator: PurchaseIndicator? = null,
        shipIndicator: ShipIndicator? = null,
        giftCardPurchase: Boolean? = null,
        reOrderPurchaseIndicator: PurchaseIndicator? = null
    ) : this(
        deliveryEmailAddress = deliveryEmailAddress,
        deliveryTimeFrameIndicator = deliveryTimeFrameIndicator,
        preOrderDate = preOrderDate?.let(::formatPreOrderDate),
        preOrderPurchaseIndicator = preOrderPurchaseIndicator,
        shipIndicator = shipIndicator?.serializedName,
        pickUpAddress = shipIndicator?.pickUpAddress,
        giftCardPurchase = giftCardPurchase,
        reOrderPurchaseIndicator = reOrderPurchaseIndicator
    )

    class Builder {
        private var deliveryEmailAddress: String? = null
        private var deliveryTimeFrameIndicator: DeliveryTimeFrameIndicator? = null
        private var preOrderDate: String? = null
        private var preOrderPurchaseIndicator: PurchaseIndicator? = null
        private var shipIndicator: ShipIndicator? = null
        private var giftCardPurchase: Boolean? = null
        private var reOrderPurchaseIndicator: PurchaseIndicator? = null

        fun deliveryEmailAddress(address: String) = apply {
            deliveryEmailAddress = address
        }
        fun deliveryTimeFrameIndicator(indicator: DeliveryTimeFrameIndicator) = apply {
            deliveryTimeFrameIndicator = indicator
        }
        fun preOrderDate(date: Any) = apply {
            preOrderDate = formatPreOrderDate(date)
        }
        fun preOrderPurchaseIndicator(indicator: PurchaseIndicator) = apply {
            preOrderPurchaseIndicator = indicator
        }
        fun shipIndicator(indicator: ShipIndicator) = apply {
            shipIndicator = indicator
        }
        fun giftCardPurchase(isGiftCardPurchase: Boolean) = apply {
            giftCardPurchase = isGiftCardPurchase
        }
        fun reOrderPurchaseIndicator(indicator: PurchaseIndicator) = apply {
            reOrderPurchaseIndicator = indicator
        }

        fun build() = RiskIndicator(
            deliveryEmailAddress = deliveryEmailAddress,
            deliveryTimeFrameIndicator = deliveryTimeFrameIndicator,
            preOrderDate = preOrderDate,
            preOrderPurchaseIndicator = preOrderPurchaseIndicator,
            shipIndicator = shipIndicator,
            giftCardPurchase = giftCardPurchase,
            reOrderPurchaseIndicator = reOrderPurchaseIndicator
        )
    }

    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.apply {
            writeString(deliveryEmailAddress)
            writeEnum(deliveryTimeFrameIndicator)
            writeString(preOrderDate)
            writeEnum(preOrderPurchaseIndicator)
            writeString(shipIndicator)
            writeParcelable(pickUpAddress, flags)
            writeOptionalBoolean(giftCardPurchase)
            writeEnum(reOrderPurchaseIndicator)
        }
    }
    private constructor(parcel: Parcel) : this(
        deliveryEmailAddress = parcel.readString(),
        deliveryTimeFrameIndicator = parcel.readEnum<DeliveryTimeFrameIndicator>(),
        preOrderDate = parcel.readString(),
        preOrderPurchaseIndicator = parcel.readEnum<PurchaseIndicator>(),
        shipIndicator = parcel.readString(),
        pickUpAddress = parcel.readParcelable(),
        giftCardPurchase = parcel.readOptionalBoolean(),
        reOrderPurchaseIndicator = parcel.readEnum<PurchaseIndicator>()
    )
}

