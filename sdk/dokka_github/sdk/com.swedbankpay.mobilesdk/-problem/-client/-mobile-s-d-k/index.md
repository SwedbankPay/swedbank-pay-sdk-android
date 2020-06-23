[sdk](../../../../index.md) / [com.swedbankpay.mobilesdk](../../../index.md) / [Problem](../../index.md) / [Client](../index.md) / [MobileSDK](./index.md)

# MobileSDK

`sealed class MobileSDK : Client, `[`ProperProblem`](../../../-proper-problem/index.md)

Base class for [Client](../index.md) Problems defined by the example backend.

### Types

| Name | Summary |
|---|---|
| [InvalidRequest](-invalid-request/index.md) | The merchant backend did not understand the request.`class InvalidRequest : MobileSDK` |
| [Unauthorized](-unauthorized/index.md) | The merchant backend rejected the request because its authentication headers were invalid.`class Unauthorized : MobileSDK` |

### Properties

| Name | Summary |
|---|---|
| [raw](raw.md) | The raw application/problem+json object.`open val raw: JsonObject` |
| [status](status.md) | The HTTP status.`open val status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
