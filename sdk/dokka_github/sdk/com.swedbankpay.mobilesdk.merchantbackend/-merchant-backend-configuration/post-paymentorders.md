[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [MerchantBackendConfiguration](index.md) / [postPaymentorders](./post-paymentorders.md)

# postPaymentorders

`suspend fun postPaymentorders(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, paymentOrder: `[`PaymentOrder`](../../com.swedbankpay.mobilesdk/-payment-order/index.md)`?, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, consumerProfileRef: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`ViewPaymentOrderInfo`](../../com.swedbankpay.mobilesdk/-view-payment-order-info/index.md)

Called by [PaymentFragment](../../com.swedbankpay.mobilesdk/-payment-fragment/index.md) when it needs to create a payment order.
Your implementation must ultimately make the call to Swedbank Pay API
and return a [ViewPaymentOrderInfo](../../com.swedbankpay.mobilesdk/-view-payment-order-info/index.md) describing the result.

### Parameters

`context` - an application context

`paymentOrder` - the [PaymentOrder](../../com.swedbankpay.mobilesdk/-payment-order/index.md) object set as the PaymentFragment argument

`userData` - the user data object set as the PaymentFragment argument

`consumerProfileRef` - if a checkin was performed first, the `consumerProfileRef` from checkin

**Return**
ViewPaymentOrderInfo describing the payment order

