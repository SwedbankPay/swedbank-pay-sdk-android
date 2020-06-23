[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [UserHeaders](./index.md)

# UserHeaders

`class UserHeaders`

Builder for custom headers.

To add headers to a request, override the desired method in [RequestDecorator](../-request-decorator/index.md)
and call userHeaders.[add](add.md), eg:

```
    override fun decorateCreatePaymentOrder(userHeaders: UserHeaders, body: String, consumerProfileRef: String?, merchantData: String?) {
        userHeaders.add("api-key", "secret-api-key")
        userHeaders.add("hmac", getHmac(body))
    }
```

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | Adds a header line to the request.`fun add(headerLine: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`UserHeaders`](./index.md)<br>Adds a header to the request.`fun add(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`UserHeaders`](./index.md)<br>`fun add(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`Date`](https://docs.oracle.com/javase/6/docs/api/java/util/Date.html)`): `[`UserHeaders`](./index.md) |
| [addNonAscii](add-non-ascii.md) | Adds a header without validating the value.`fun addNonAscii(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`UserHeaders`](./index.md) |
| [set](set.md) | Sets a header in the request.`fun set(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`UserHeaders`](./index.md)<br>`fun set(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`Date`](https://docs.oracle.com/javase/6/docs/api/java/util/Date.html)`): `[`UserHeaders`](./index.md) |
