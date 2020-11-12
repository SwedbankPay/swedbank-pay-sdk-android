[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RequestDecorator](index.md) / [withHeaders](./with-headers.md)

# withHeaders

`@JvmStatic fun withHeaders(vararg namesAndValues: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`RequestDecorator`](index.md)

Create a RequestDecorator that attaches the specified headers to all SDK requests.

### Parameters

`namesAndValues` - the header names and values, alternating`@JvmStatic fun withHeaders(headers: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`RequestDecorator`](index.md)

Create a RequestDecorator that attaches the specified headers to all SDK requests.

### Parameters

`headers` - map of header names to corresponding values