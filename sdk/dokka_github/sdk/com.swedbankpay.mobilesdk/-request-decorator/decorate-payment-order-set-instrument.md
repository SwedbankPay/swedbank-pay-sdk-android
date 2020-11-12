[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RequestDecorator](index.md) / [decoratePaymentOrderSetInstrument](./decorate-payment-order-set-instrument.md)

# decoratePaymentOrderSetInstrument

`open suspend fun decoratePaymentOrderSetInstrument(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, instrument: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Override this method to add custom headers to the PATCH {setInstrument} request of a payment order.

The default implementation does nothing.

### Parameters

`userHeaders` - headers added to this will be sent with the request

`url` - the url of the request

`body` - the body of the request

`instrument` - the instrument used to create the request body