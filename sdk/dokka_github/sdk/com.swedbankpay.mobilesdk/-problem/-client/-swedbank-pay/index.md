[sdk](../../../../index.md) / [com.swedbankpay.mobilesdk](../../../index.md) / [Problem](../../index.md) / [Client](../index.md) / [SwedbankPay](./index.md)

# SwedbankPay

`sealed class SwedbankPay : Client, `[`SwedbankPayProblem`](../../../-swedbank-pay-problem/index.md)

Base class for [Client](../index.md) problems defined by the Swedbank Pay backend.
[https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems](https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems)

### Types

| Name | Summary |
|---|---|
| [Forbidden](-forbidden/index.md) | The request was understood, but the service is refusing to fulfill it. You may not have access to the requested resource.`class Forbidden : SwedbankPay` |
| [InputError](-input-error/index.md) | The request could not be handled because the request was malformed somehow (e.g. an invalid field value)`class InputError : SwedbankPay` |
| [NotFound](-not-found/index.md) | The requested resource was not found.`class NotFound : SwedbankPay` |

### Properties

| Name | Summary |
|---|---|
| [action](action.md) | Suggested action to take to recover from the error.`open val action: `[`SwedbankPayAction`](../../../-swedbank-pay-action.md)`?` |
| [detail](detail.md) | Human-readable details about the problem`open val detail: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [instance](instance.md) | Swedbank Pay internal identifier of the problem. This may be useful in debugging the issue with Swedbank Pay support.`open val instance: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [problems](problems.md) | Array of problem detail objects`open val problems: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SwedbankPaySubproblem`](../../../-swedbank-pay-subproblem/index.md)`>` |
| [raw](raw.md) | The raw application/problem+json object.`open val raw: JsonObject` |
| [status](status.md) | The HTTP status.`open val status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [title](title.md) | Human-readable description of the problem`open val title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |