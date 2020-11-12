[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [Configuration](index.md) / [updatePaymentOrder](./update-payment-order.md)

# updatePaymentOrder

`open suspend fun updatePaymentOrder(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, paymentOrder: `[`PaymentOrder`](../-payment-order/index.md)`?, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, viewPaymentOrderInfo: `[`ViewPaymentOrderInfo`](../-view-payment-order-info/index.md)`, updateInfo: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`ViewPaymentOrderInfo`](../-view-payment-order-info/index.md)

Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to update a payment order.

If you do not update payment orders after they have been created,
you do not need to override this method.

### Parameters

`context` - an application context

`paymentOrder` - the [PaymentOrder](../-payment-order/index.md) object set as the PaymentFragment argument

`userData` - the user data object set as the PaymentFragment argument

`viewPaymentOrderInfo` - the current [ViewPaymentOrderInfo](../-view-payment-order-info/index.md) as returned from a call to this or [postPaymentorders](post-paymentorders.md)

`updateInfo` - the updateInfo value from the [PaymentViewModel.updatePaymentOrder](../-payment-view-model/update-payment-order.md) call

**Return**
ViewPaymentOrderInfo describing the payment order with the changed instrument

