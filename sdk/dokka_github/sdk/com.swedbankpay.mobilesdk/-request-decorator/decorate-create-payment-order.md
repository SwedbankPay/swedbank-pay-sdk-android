[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RequestDecorator](index.md) / [decorateCreatePaymentOrder](./decorate-create-payment-order.md)

# decorateCreatePaymentOrder

`open suspend fun decorateCreatePaymentOrder(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, paymentOrder: `[`PaymentOrder`](../-payment-order/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Override this method to add custom headers to the POST {paymentorders} request.

The default implementation does nothing.

### Parameters

`userHeaders` - headers added to this will be sent with the request

`body` - the body of the request

`paymentOrder` - the payment order used to create the request body