[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [UnexpectedContentProblem](./index.md)

# UnexpectedContentProblem

`interface UnexpectedContentProblem`

A Pseudo-Problem where the server response was entirely unrecognized.

### Properties

| Name | Summary |
|---|---|
| [body](body.md) | The entity body of the response, if it had one and it could be read into a String.`abstract val body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [contentType](content-type.md) | The Content-Type of the response, if any`abstract val contentType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [status](status.md) | The HTTP status.`abstract val status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
