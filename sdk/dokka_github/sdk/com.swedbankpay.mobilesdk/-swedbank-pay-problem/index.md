[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [SwedbankPayProblem](./index.md)

# SwedbankPayProblem

`interface SwedbankPayProblem : `[`ProperProblem`](../-proper-problem/index.md)

A Problem defined by the Swedbank Pay backend.
[https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems](https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems)

### Properties

| Name | Summary |
|---|---|
| [action](action.md) | Suggested action to take to recover from the error.`abstract val action: `[`SwedbankPayAction`](../-swedbank-pay-action.md)`?` |
| [detail](detail.md) | Human-readable details about the problem`abstract val detail: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [instance](instance.md) | Swedbank Pay internal identifier of the problem. This may be useful in debugging the issue with Swedbank Pay support.`abstract val instance: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [problems](problems.md) | Array of problem detail objects`abstract val problems: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SwedbankPaySubproblem`](../-swedbank-pay-subproblem/index.md)`>` |
| [title](title.md) | Human-readable description of the problem`abstract val title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
