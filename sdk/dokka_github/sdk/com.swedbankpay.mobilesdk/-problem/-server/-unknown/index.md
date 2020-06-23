[sdk](../../../../index.md) / [com.swedbankpay.mobilesdk](../../../index.md) / [Problem](../../index.md) / [Server](../index.md) / [Unknown](./index.md)

# Unknown

`class Unknown : Server, `[`UnknownProblem`](../../../-unknown-problem/index.md)

[Server](../index.md) problem with an unrecognized type.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | [Server](../index.md) problem with an unrecognized type.`Unknown(raw: JsonObject, type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, detail: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, instance: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?)` |

### Properties

| Name | Summary |
|---|---|
| [detail](detail.md) | A detailed, human-readable description`val detail: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [instance](instance.md) | Optional unique identifier for this specific problem instance. This is also an URI which may or may not be dereferencable.`val instance: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [raw](raw.md) | The raw application/problem+json object.`val raw: JsonObject` |
| [status](status.md) | The HTTP status.`val status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [title](title.md) | A short, human-readable description of the problem.`val title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [type](type.md) | The type of the problem. This is an URI which may or may not be dereferencable. Defaults to `"about:blank"` if the json contained no explicit type.`val type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
