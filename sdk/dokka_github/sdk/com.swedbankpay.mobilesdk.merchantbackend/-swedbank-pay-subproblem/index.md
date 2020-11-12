[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [SwedbankPaySubproblem](./index.md)

# SwedbankPaySubproblem

`class SwedbankPaySubproblem : `[`Serializable`](https://docs.oracle.com/javase/6/docs/api/java/io/Serializable.html)

Object detailing the reason for a [SwedbankPayProblem](../-swedbank-pay-problem/index.md).

See [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems](https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Object detailing the reason for a [SwedbankPayProblem](../-swedbank-pay-problem/index.md).`SwedbankPaySubproblem(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?)` |

### Properties

| Name | Summary |
|---|---|
| [description](description.md) | A description of what was wrong`val description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [name](name.md) | Name of the erroneous part of the request`val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
