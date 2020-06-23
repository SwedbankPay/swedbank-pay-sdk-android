[sdk](../../../../../index.md) / [com.swedbankpay.mobilesdk](../../../../index.md) / [Problem](../../../index.md) / [Server](../../index.md) / [MobileSDK](../index.md) / [InvalidBackendResponse](./index.md)

# InvalidBackendResponse

`class InvalidBackendResponse : MobileSDK`

The merchant backend received an invalid response from the Swedbank Pay backend.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | The merchant backend received an invalid response from the Swedbank Pay backend.`InvalidBackendResponse(raw: JsonObject, status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, backendStatus: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?)` |

### Properties

| Name | Summary |
|---|---|
| [backendStatus](backend-status.md) | The HTTP status code received from the Swedback Pay backend`val backendStatus: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [body](body.md) | The unrecognized body received from the Swedback Pay backend`val body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
