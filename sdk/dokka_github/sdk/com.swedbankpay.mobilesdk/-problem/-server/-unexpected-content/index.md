[sdk](../../../../index.md) / [com.swedbankpay.mobilesdk](../../../index.md) / [Problem](../../index.md) / [Server](../index.md) / [UnexpectedContent](./index.md)

# UnexpectedContent

`class UnexpectedContent : Server, `[`UnexpectedContentProblem`](../../../-unexpected-content-problem/index.md)

Pseudo-problem, not actually parsed from an application/problem+json response.
This problem is emitted if the server response is in an unexpected format and the
HTTP status is not in the Client Error range.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Pseudo-problem, not actually parsed from an application/problem+json response. This problem is emitted if the server response is in an unexpected format and the HTTP status is not in the Client Error range.`UnexpectedContent(status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, contentType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?)` |

### Properties

| Name | Summary |
|---|---|
| [body](body.md) | The entity body of the response, if it had one and it could be read into a String.`val body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [contentType](content-type.md) | The Content-Type of the response, if any`val contentType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [status](status.md) | The HTTP status.`val status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
