[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [Operation](./index.md)

# Operation

`data class Operation`

Swedbank Pay Operation. Operations are invoked by making an HTTP request.

Please refer to the
[Swedbank Pay documentation](https://developer.swedbankpay.com/checkout/other-features#operations).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Swedbank Pay Operation. Operations are invoked by making an HTTP request.`Operation(rel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, method: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, contentType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?)` |

### Properties

| Name | Summary |
|---|---|
| [contentType](content-type.md) | The Content-Type of the response`val contentType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [href](href.md) | The request URL`val href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [method](method.md) | The request method`val method: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [rel](rel.md) | The purpose of the operation. The exact meaning is dependent on the Operation context.`val rel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
