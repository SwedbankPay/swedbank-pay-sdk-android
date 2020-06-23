[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RequestDecoratorCompat](index.md) / [decorateInitiateConsumerSessionCompat](./decorate-initiate-consumer-session-compat.md)

# decorateInitiateConsumerSessionCompat

`open fun decorateInitiateConsumerSessionCompat(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, consumer: `[`Consumer`](../-consumer/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Override this method to add custom headers to the POST {consumers} request.

The default implementation does nothing.

### Parameters

`userHeaders` - headers added to this will be sent with the request

`body` - the body of the request