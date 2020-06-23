[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RequestDecorator](index.md) / [decorateInitiateConsumerSession](./decorate-initiate-consumer-session.md)

# decorateInitiateConsumerSession

`open suspend fun decorateInitiateConsumerSession(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, consumer: `[`Consumer`](../-consumer/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Override this method to add custom headers to the POST {consumers} request.

The default implementation does nothing.

### Parameters

`userHeaders` - headers added to this will be sent with the request

`body` - the body of the request