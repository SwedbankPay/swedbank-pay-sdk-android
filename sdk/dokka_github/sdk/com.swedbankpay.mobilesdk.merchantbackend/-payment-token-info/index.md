[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [PaymentTokenInfo](./index.md)

# PaymentTokenInfo

`data class PaymentTokenInfo`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PaymentTokenInfo(paymentToken: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, instrument: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, instrumentDisplayName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, instrumentParameters: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?, operations: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Operation`](../-operation/index.md)`>)` |

### Properties

| Name | Summary |
|---|---|
| [instrument](instrument.md) | Payment instrument type of this token`val instrument: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [instrumentDisplayName](instrument-display-name.md) | User-friendly name of the payment instrument`val instrumentDisplayName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [instrumentParameters](instrument-parameters.md) | Instrument-specific parameters.`val instrumentParameters: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` |
| [operations](operations.md) | Operations you can perform on this token. Note that you generally cannot call these from your mobile app.`val operations: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Operation`](../-operation/index.md)`>` |
| [paymentToken](payment-token.md) | The actual paymentToken`val paymentToken: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
