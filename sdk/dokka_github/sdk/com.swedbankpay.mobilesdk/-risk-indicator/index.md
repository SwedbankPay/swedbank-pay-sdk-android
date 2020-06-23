[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RiskIndicator](./index.md)

# RiskIndicator

`data class RiskIndicator : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | `class Builder` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RiskIndicator(deliveryEmailAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, deliveryTimeFrameIndicator: `[`DeliveryTimeFrameIndicator`](../-delivery-time-frame-indicator/index.md)`? = null, preOrderDate: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null, preOrderPurchaseIndicator: `[`PurchaseIndicator`](../-purchase-indicator/index.md)`? = null, shipIndicator: `[`ShipIndicator`](../-ship-indicator/index.md)`? = null, giftCardPurchase: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, reOrderPurchaseIndicator: `[`PurchaseIndicator`](../-purchase-indicator/index.md)`? = null)`<br>`RiskIndicator(deliveryEmailAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, deliveryTimeFrameIndicator: `[`DeliveryTimeFrameIndicator`](../-delivery-time-frame-indicator/index.md)`?, preOrderDate: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, preOrderPurchaseIndicator: `[`PurchaseIndicator`](../-purchase-indicator/index.md)`?, shipIndicator: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, pickUpAddress: `[`PickUpAddress`](../-pick-up-address/index.md)`?, giftCardPurchase: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?, reOrderPurchaseIndicator: `[`PurchaseIndicator`](../-purchase-indicator/index.md)`?)` |

### Properties

| Name | Summary |
|---|---|
| [deliveryEmailAddress](delivery-email-address.md) | `val deliveryEmailAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [deliveryTimeFrameIndicator](delivery-time-frame-indicator.md) | `val deliveryTimeFrameIndicator: `[`DeliveryTimeFrameIndicator`](../-delivery-time-frame-indicator/index.md)`?` |
| [giftCardPurchase](gift-card-purchase.md) | `val giftCardPurchase: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` |
| [pickUpAddress](pick-up-address.md) | `val pickUpAddress: `[`PickUpAddress`](../-pick-up-address/index.md)`?` |
| [preOrderDate](pre-order-date.md) | `val preOrderDate: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [preOrderPurchaseIndicator](pre-order-purchase-indicator.md) | `val preOrderPurchaseIndicator: `[`PurchaseIndicator`](../-purchase-indicator/index.md)`?` |
| [reOrderPurchaseIndicator](re-order-purchase-indicator.md) | `val reOrderPurchaseIndicator: `[`PurchaseIndicator`](../-purchase-indicator/index.md)`?` |
| [shipIndicator](ship-indicator.md) | `val shipIndicator: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](write-to-parcel.md) | `fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`RiskIndicator`](./index.md)`>` |
