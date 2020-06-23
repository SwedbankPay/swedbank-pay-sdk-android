[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RequestDecorator](index.md) / [decorateAnyRequest](./decorate-any-request.md)

# decorateAnyRequest

`open suspend fun decorateAnyRequest(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, method: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Override this method to add custom headers to all backend requests.

### Parameters

`userHeaders` - headers added to this will be sent with the request

`method` - the HTTP method of the request

`url` - the URL of the request

`body` - the body of the request, if any