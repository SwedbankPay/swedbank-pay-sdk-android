[sdk](../../../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../../../index.md) / [MerchantBackendProblem](../../index.md) / [Server](../index.md) / [SwedbankPay](./index.md)

# SwedbankPay

`sealed class SwedbankPay : Server, `[`SwedbankPayProblem`](../../../-swedbank-pay-problem/index.md)

Base class for [Server](../index.md) problems defined by the Swedbank Pay backend.
[https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems](https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems)

### Types

| Name | Summary |
|---|---|
| [ConfigurationError](-configuration-error/index.md) | There is a problem with your merchant configuration.`class ConfigurationError : SwedbankPay` |
| [SystemError](-system-error/index.md) | General internal error in Swedbank Pay systems.`class SystemError : SwedbankPay` |

### Properties

| Name | Summary |
|---|---|
| [action](action.md) | Suggested action to take to recover from the error.`open val action: `[`SwedbankPayAction`](../../../-swedbank-pay-action.md)`?` |
| [problems](problems.md) | Array of problem detail objects`open val problems: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SwedbankPaySubproblem`](../../../-swedbank-pay-subproblem/index.md)`>` |
