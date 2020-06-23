[sdk](../../../../index.md) / [com.swedbankpay.mobilesdk](../../../index.md) / [Problem](../../index.md) / [Server](../index.md) / [MobileSDK](./index.md)

# MobileSDK

`sealed class MobileSDK : Server, `[`ProperProblem`](../../../-proper-problem/index.md)

Base class for [Server](../index.md) Problems defined by the example backend.

### Types

| Name | Summary |
|---|---|
| [BackendConnectionFailure](-backend-connection-failure/index.md) | The merchant backend failed to connect to the Swedbank Pay backend.`class BackendConnectionFailure : MobileSDK` |
| [BackendConnectionTimeout](-backend-connection-timeout/index.md) | The merchant backend timed out trying to connect to the Swedbank Pay backend.`class BackendConnectionTimeout : MobileSDK` |
| [InvalidBackendResponse](-invalid-backend-response/index.md) | The merchant backend received an invalid response from the Swedbank Pay backend.`class InvalidBackendResponse : MobileSDK` |

### Properties

| Name | Summary |
|---|---|
| [raw](raw.md) | The raw application/problem+json object.`open val raw: JsonObject` |
| [status](status.md) | The HTTP status.`open val status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
