[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RequestDecoratorCompat](index.md) / [decorateGetPaymentOrderCompat](./decorate-get-payment-order-compat.md)

# decorateGetPaymentOrderCompat

`open fun decorateGetPaymentOrderCompat(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Override this method to add custom headers to the GET {paymentorder} request.

The default implementation does nothing.

### Parameters

`userHeaders` - headers added to this will be sent with the request

`url` - the URL being requested