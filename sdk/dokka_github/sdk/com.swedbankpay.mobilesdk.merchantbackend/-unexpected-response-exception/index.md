[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [UnexpectedResponseException](./index.md)

# UnexpectedResponseException

`class UnexpectedResponseException : `[`IOException`](https://docs.oracle.com/javase/6/docs/api/java/io/IOException.html)

The server returned a response that [MerchantBackendConfiguration](../-merchant-backend-configuration/index.md)
was not prepared for.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | The server returned a response that [MerchantBackendConfiguration](../-merchant-backend-configuration/index.md) was not prepared for.`UnexpectedResponseException(status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, contentType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, cause: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`?)` |

### Properties

| Name | Summary |
|---|---|
| [body](body.md) | The response body`val body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [contentType](content-type.md) | The Content-Type of `body`, if available`val contentType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [status](status.md) | The http status code`val status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
