[sdk](../../../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../../../index.md) / [MerchantBackendProblem](../../index.md) / [Client](../index.md) / [SwedbankPay](./index.md)

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
| [problems](problems.md) | Array of problem detail objects`open val problems: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SwedbankPaySubproblem`](../../../-swedbank-pay-subproblem/index.md)`>` |
