package com.swedbankpay.mobilesdk

import android.annotation.TargetApi
import android.os.Build
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.RiskIndicator.Companion.formatPreOrderDate
import kotlinx.parcelize.Parcelize
import org.joda.time.ReadablePartial
import org.joda.time.format.DateTimeFormat
import java.util.*

/**
 * Optional information to reduce the risk factor of a payment.
 *
 * You should populate this data as completely as possible to decrease the likelihood of 3-D Secure
 * Strong Authentication.
 *
 * @constructor "Raw" constructor. You must manually ensure you set the values for [shipIndicator]
 * and [pickUpAddress] correctly. It is recommended to use the other constructor.
 */
@Parcelize
data class RiskIndicator(
    /**
     * For electronic delivery, the e-mail address where the merchandise is delivered
     */
    @SerializedName("deliveryEmailAddress") val deliveryEmailAddress: String?,
    /**
     * Indicator of merchandise delivery timeframe. See [DeliveryTimeFrameIndicator] for options.
     */
    @SerializedName("deliveryTimeFrameIndicator") val deliveryTimeFrameIndicator: DeliveryTimeFrameIndicator?,
    /**
     * If this is a pre-order, the expected date that the merchandise will be available on.
     *
     * Format is `YYYYMMDD`. You can use [formatPreOrderDate] to format some common local-date
     * types correctly.
     */
    @SerializedName("preOrderDate") val preOrderDate: String?,
    /**
     * Indicates whether this is a pre-order. See [PreOrderPurchaseIndicator] for options.
     */
    @SerializedName("preOrderPurchaseIndicator") val preOrderPurchaseIndicator: PreOrderPurchaseIndicator?,
    /**
     * Indicates the shipping method for this order.
     *
     * Values are according to the
     * [Swedbank Pay documentation](https://developer.swedbankpay.com/checkout/payment-menu#request).
     * However, you should usually use the constructor that takes a [ShipIndicator] argument, which
     * models the different options in a Kotlin-native way.
     */
    @SerializedName("shipIndicator") val shipIndicator: String?,
    /**
     * If [shipIndicator] is "04", i.e. [ShipIndicator.PICK_UP_AT_STORE], this field should be
     * populated.
     *
     * You should use the constructor that takes a [ShipIndicator] argument for `shipIndicator`,
     * which also set sets this field properly.
     */
    @SerializedName("pickUpAddress") val pickUpAddress: PickUpAddress?,
    /**
     * `true` if this is a purchase of a gift card
     */
    @SerializedName("giftCardPurchase") val giftCardPurchase: Boolean?,
    /**
     * Indicates whether this is a re-order of previously purchased merchandise.
     * See [ReOrderPurchaseIndicator] for options.
     */
    @SerializedName("reOrderPurchaseIndicator") val reOrderPurchaseIndicator: ReOrderPurchaseIndicator?
) : Parcelable {
    companion object {
        /**
         * Creates a `preOrderDate` from a [java.time.temporal.TemporalAccessor],
         * e.g. a [java.time.LocalDate].
         *
         * Note that `java.time` is also available on API < 26 through
         * [API desugaring](https://developer.android.com/studio/write/java8-support#library-desugaring).
         */
        @TargetApi(Build.VERSION_CODES.O)
        fun formatPreOrderDate(date: java.time.temporal.TemporalAccessor) =
            java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US).format(date)

        /**
         * Creates a `preOrderDate` from an `org.threeten.bp.temporal.TemporalAccessor`,
         * e.g. a `org.threeten.bp.LocalDate`.
         */
        fun formatPreOrderDate(date: org.threeten.bp.temporal.TemporalAccessor) =
            org.threeten.bp.format.DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US).format(date)

        /**
         * Creates a `preOrderDate` from an `org.joda.time.ReadablePartial`
         * e.g. a `org.joda.time.LocalDate`.
         */
        fun formatPreOrderDate(date: ReadablePartial) =
            DateTimeFormat.forPattern("yyyyMMdd").withLocale(Locale.US).print(date)
    }

    /**
     * Constructs a `RiskIndicator` with Kotlin-native value for [shipIndicator]
     *
     * @param deliveryEmailAddress For electronic delivery, the e-mail address where the merchanise is delivered
     * @param deliveryTimeFrameIndicator Indicator of merchandise delivery timeframe
     * @param preOrderDate If this is a pre-order, the expected date that the merchandise will be available on. Format is YYYYMMDD; use [formatPreOrderDate] to format some usual types correctly.
     * @param preOrderPurchaseIndicator Indicates whether this is a pre-order
     * @param shipIndicator Indicates the shipping method for this order
     * @param reOrderPurchaseIndicator Indicates whether this is a re-order of previously purchased merchandise
     */
    constructor(
        deliveryEmailAddress: String? = null,
        deliveryTimeFrameIndicator: DeliveryTimeFrameIndicator? = null,
        preOrderDate: String? = null,
        preOrderPurchaseIndicator: PreOrderPurchaseIndicator? = null,
        shipIndicator: ShipIndicator? = null,
        giftCardPurchase: Boolean? = null,
        reOrderPurchaseIndicator: ReOrderPurchaseIndicator? = null
    ) : this(
        deliveryEmailAddress = deliveryEmailAddress,
        deliveryTimeFrameIndicator = deliveryTimeFrameIndicator,
        preOrderDate = preOrderDate,
        preOrderPurchaseIndicator = preOrderPurchaseIndicator,
        shipIndicator = shipIndicator?.serializedName,
        pickUpAddress = shipIndicator?.pickUpAddress,
        giftCardPurchase = giftCardPurchase,
        reOrderPurchaseIndicator = reOrderPurchaseIndicator
    )

    @Suppress("unused")
    class Builder {
        private var deliveryEmailAddress: String? = null
        private var deliveryTimeFrameIndicator: DeliveryTimeFrameIndicator? = null
        private var preOrderDate: String? = null
        private var preOrderPurchaseIndicator: PreOrderPurchaseIndicator? = null
        private var shipIndicator: ShipIndicator? = null
        private var giftCardPurchase: Boolean? = null
        private var reOrderPurchaseIndicator: ReOrderPurchaseIndicator? = null

        fun deliveryEmailAddress(address: String) = apply {
            deliveryEmailAddress = address
        }
        fun deliveryTimeFrameIndicator(indicator: DeliveryTimeFrameIndicator) = apply {
            deliveryTimeFrameIndicator = indicator
        }
        fun preOrderDate(date: String) = apply {
            preOrderDate = date
        }
        fun preOrderPurchaseIndicator(indicator: PreOrderPurchaseIndicator) = apply {
            preOrderPurchaseIndicator = indicator
        }
        fun shipIndicator(indicator: ShipIndicator) = apply {
            shipIndicator = indicator
        }
        fun giftCardPurchase(isGiftCardPurchase: Boolean) = apply {
            giftCardPurchase = isGiftCardPurchase
        }
        fun reOrderPurchaseIndicator(indicator: ReOrderPurchaseIndicator) = apply {
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
}

