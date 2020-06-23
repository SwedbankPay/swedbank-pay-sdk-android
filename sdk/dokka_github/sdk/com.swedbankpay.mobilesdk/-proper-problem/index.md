[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [ProperProblem](./index.md)

# ProperProblem

`interface ProperProblem`

A Problem parsed from an application/problem+json object.

### Properties

| Name | Summary |
|---|---|
| [raw](raw.md) | The raw application/problem+json object.`abstract val raw: JsonObject` |
| [status](status.md) | The HTTP status.`abstract val status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [SwedbankPayProblem](../-swedbank-pay-problem/index.md) | A Problem defined by the Swedbank Pay backend. [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems](https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems)`interface SwedbankPayProblem : `[`ProperProblem`](./index.md) |
| [UnknownProblem](../-unknown-problem/index.md) | A Problem whose [type](../-unknown-problem/type.md) was not recognized.`interface UnknownProblem : `[`ProperProblem`](./index.md) |
