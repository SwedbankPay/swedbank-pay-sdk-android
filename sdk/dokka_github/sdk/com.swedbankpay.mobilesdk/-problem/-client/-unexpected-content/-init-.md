[sdk](../../../../index.md) / [com.swedbankpay.mobilesdk](../../../index.md) / [Problem](../../index.md) / [Client](../index.md) / [UnexpectedContent](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`UnexpectedContent(status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, contentType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?)`

Pseudo-problem, not actually parsed from an application/problem+json response.
This problem is emitted if the server response is in an unexpected format and the
HTTP status is in the Client Error range (400-499).

