[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [Configuration](index.md) / [postPaymentorders](./post-paymentorders.md)

# postPaymentorders

`abstract suspend fun postPaymentorders(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, paymentOrder: `[`PaymentOrder`](../-payment-order/index.md)`?, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, consumerProfileRef: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`ViewPaymentOrderInfo`](../-view-payment-order-info/index.md)

Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to create a payment order.
Your implementation must ultimately make the call to Swedbank Pay API
and return a [ViewPaymentOrderInfo](../-view-payment-order-info/index.md) describing the result.

### Parameters

`context` - an application context

`paymentOrder` - the [PaymentOrder](../-payment-order/index.md) object set as the PaymentFragment argument

`userData` - the user data object set as the PaymentFragment argument

`consumerProfileRef` - if a checkin was performed first, the `consumerProfileRef` from checkin

**Return**
ViewPaymentOrderInfo describing the payment order

