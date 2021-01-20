[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RiskIndicator](./index.md)

# RiskIndicator

`data class RiskIndicator : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)

Optional information to reduce the risk factor of a payment.

You should populate this data as completely as possible to decrease the likelihood of 3-D Secure
Strong Authentication.

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | `class Builder` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Constructs a `RiskIndicator` with Kotlin-native value for [shipIndicator](-init-.md#com.swedbankpay.mobilesdk.RiskIndicator$<init>(kotlin.String, com.swedbankpay.mobilesdk.DeliveryTimeFrameIndicator, kotlin.String, com.swedbankpay.mobilesdk.PreOrderPurchaseIndicator, com.swedbankpay.mobilesdk.ShipIndicator, kotlin.Boolean, com.swedbankpay.mobilesdk.ReOrderPurchaseIndicator)/shipIndicator)`RiskIndicator(deliveryEmailAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, deliveryTimeFrameIndicator: `[`DeliveryTimeFrameIndicator`](../-delivery-time-frame-indicator/index.md)`? = null, preOrderDate: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, preOrderPurchaseIndicator: `[`PreOrderPurchaseIndicator`](../-pre-order-purchase-indicator/index.md)`? = null, shipIndicator: `[`ShipIndicator`](../-ship-indicator/index.md)`? = null, giftCardPurchase: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, reOrderPurchaseIndicator: `[`ReOrderPurchaseIndicator`](../-re-order-purchase-indicator/index.md)`? = null)`<br>"Raw" constructor. You must manually ensure you set the values for [shipIndicator](ship-indicator.md) and [pickUpAddress](pick-up-address.md) correctly. It is recommended to use the other constructor.`RiskIndicator(deliveryEmailAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, deliveryTimeFrameIndicator: `[`DeliveryTimeFrameIndicator`](../-delivery-time-frame-indicator/index.md)`?, preOrderDate: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, preOrderPurchaseIndicator: `[`PreOrderPurchaseIndicator`](../-pre-order-purchase-indicator/index.md)`?, shipIndicator: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, pickUpAddress: `[`PickUpAddress`](../-pick-up-address/index.md)`?, giftCardPurchase: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?, reOrderPurchaseIndicator: `[`ReOrderPurchaseIndicator`](../-re-order-purchase-indicator/index.md)`?)` |

### Properties

| Name | Summary |
|---|---|
| [deliveryEmailAddress](delivery-email-address.md) | For electronic delivery, the e-mail address where the merchandise is delivered`val deliveryEmailAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [deliveryTimeFrameIndicator](delivery-time-frame-indicator.md) | Indicator of merchandise delivery timeframe. See [DeliveryTimeFrameIndicator](../-delivery-time-frame-indicator/index.md) for options.`val deliveryTimeFrameIndicator: `[`DeliveryTimeFrameIndicator`](../-delivery-time-frame-indicator/index.md)`?` |
| [giftCardPurchase](gift-card-purchase.md) | `true` if this is a purchase of a gift card`val giftCardPurchase: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` |
| [pickUpAddress](pick-up-address.md) | If [shipIndicator](ship-indicator.md) is "04", i.e. [ShipIndicator.PICK_UP_AT_STORE](../-ship-indicator/-p-i-c-k_-u-p_-a-t_-s-t-o-r-e.md), this field should be populated.`val pickUpAddress: `[`PickUpAddress`](../-pick-up-address/index.md)`?` |
| [preOrderDate](pre-order-date.md) | If this is a pre-order, the expected date that the merchandise will be available on.`val preOrderDate: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [preOrderPurchaseIndicator](pre-order-purchase-indicator.md) | Indicates whether this is a pre-order. See [PreOrderPurchaseIndicator](../-pre-order-purchase-indicator/index.md) for options.`val preOrderPurchaseIndicator: `[`PreOrderPurchaseIndicator`](../-pre-order-purchase-indicator/index.md)`?` |
| [reOrderPurchaseIndicator](re-order-purchase-indicator.md) | Indicates whether this is a re-order of previously purchased merchandise. See [ReOrderPurchaseIndicator](../-re-order-purchase-indicator/index.md) for options.`val reOrderPurchaseIndicator: `[`ReOrderPurchaseIndicator`](../-re-order-purchase-indicator/index.md)`?` |
| [shipIndicator](ship-indicator.md) | Indicates the shipping method for this order.`val shipIndicator: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](write-to-parcel.md) | `fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`RiskIndicator`](./index.md)`>` |

### Companion Object Functions

| Name | Summary |
|---|---|
| [formatPreOrderDate](format-pre-order-date.md) | Creates a `preOrderDate` from a [java.time.temporal.TemporalAccessor](https://developer.android.com/reference/java/time/temporal/TemporalAccessor.html), e.g. a [java.time.LocalDate](https://developer.android.com/reference/java/time/LocalDate.html).`fun formatPreOrderDate(date: `[`TemporalAccessor`](https://developer.android.com/reference/java/time/temporal/TemporalAccessor.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!`<br>Creates a `preOrderDate` from an `org.threeten.bp.temporal.TemporalAccessor`, e.g. a `org.threeten.bp.LocalDate`.`fun formatPreOrderDate(date: TemporalAccessor): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!`<br>Creates a `preOrderDate` from an `org.joda.time.ReadablePartial` e.g. a `org.joda.time.LocalDate`.`fun formatPreOrderDate(date: ReadablePartial): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!` |
