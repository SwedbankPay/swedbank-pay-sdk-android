[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RiskIndicator](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`RiskIndicator(deliveryEmailAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, deliveryTimeFrameIndicator: `[`DeliveryTimeFrameIndicator`](../-delivery-time-frame-indicator/index.md)`? = null, preOrderDate: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, preOrderPurchaseIndicator: `[`PreOrderPurchaseIndicator`](../-pre-order-purchase-indicator/index.md)`? = null, shipIndicator: `[`ShipIndicator`](../-ship-indicator/index.md)`? = null, giftCardPurchase: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, reOrderPurchaseIndicator: `[`ReOrderPurchaseIndicator`](../-re-order-purchase-indicator/index.md)`? = null)`

Constructs a `RiskIndicator` with Kotlin-native value for [shipIndicator](-init-.md#com.swedbankpay.mobilesdk.RiskIndicator$<init>(kotlin.String, com.swedbankpay.mobilesdk.DeliveryTimeFrameIndicator, kotlin.String, com.swedbankpay.mobilesdk.PreOrderPurchaseIndicator, com.swedbankpay.mobilesdk.ShipIndicator, kotlin.Boolean, com.swedbankpay.mobilesdk.ReOrderPurchaseIndicator)/shipIndicator)

### Parameters

`deliveryEmailAddress` - For electronic delivery, the e-mail address where the merchanise is delivered

`deliveryTimeFrameIndicator` - Indicator of merchandise delivery timeframe

`preOrderDate` - If this is a pre-order, the expected date that the merchandise will be available on. Format is YYYYMMDD; use [formatPreOrderDate](format-pre-order-date.md) to format some usual types correctly.

`preOrderPurchaseIndicator` - Indicates whether this is a pre-order

`shipIndicator` - Indicates the shipping method for this order

`reOrderPurchaseIndicator` - Indicates whether this is a re-order of previously purchased merchandise`RiskIndicator(deliveryEmailAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, deliveryTimeFrameIndicator: `[`DeliveryTimeFrameIndicator`](../-delivery-time-frame-indicator/index.md)`?, preOrderDate: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, preOrderPurchaseIndicator: `[`PreOrderPurchaseIndicator`](../-pre-order-purchase-indicator/index.md)`?, shipIndicator: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, pickUpAddress: `[`PickUpAddress`](../-pick-up-address/index.md)`?, giftCardPurchase: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?, reOrderPurchaseIndicator: `[`ReOrderPurchaseIndicator`](../-re-order-purchase-indicator/index.md)`?)`

"Raw" constructor. You must manually ensure you set the values for [shipIndicator](ship-indicator.md)
and [pickUpAddress](pick-up-address.md) correctly. It is recommended to use the other constructor.

**Constructor**
"Raw" constructor. You must manually ensure you set the values for [shipIndicator](ship-indicator.md)
and [pickUpAddress](pick-up-address.md) correctly. It is recommended to use the other constructor.

