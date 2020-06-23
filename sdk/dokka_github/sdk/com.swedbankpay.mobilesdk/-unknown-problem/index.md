[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [UnknownProblem](./index.md)

# UnknownProblem

`interface UnknownProblem : `[`ProperProblem`](../-proper-problem/index.md)

A Problem whose [type](type.md) was not recognized.

Any custom errors you add to your merchant backend will be reported
as these. Examine the [type](type.md) to identify them. You can access the [raw](../-proper-problem/raw.md) json
to read any custom fields.

See [https://tools.ietf.org/html/rfc7807#section-3.1](https://tools.ietf.org/html/rfc7807#section-3.1)

### Properties

| Name | Summary |
|---|---|
| [detail](detail.md) | A detailed, human-readable description`abstract val detail: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [instance](instance.md) | Optional unique identifier for this specific problem instance. This is also an URI which may or may not be dereferencable.`abstract val instance: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [title](title.md) | A short, human-readable description of the problem.`abstract val title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [type](type.md) | The type of the problem. This is an URI which may or may not be dereferencable. Defaults to `"about:blank"` if the json contained no explicit type.`abstract val type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
