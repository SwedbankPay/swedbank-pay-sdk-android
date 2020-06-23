[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [UserHeaders](index.md) / [add](./add.md)

# add

`fun add(headerLine: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`UserHeaders`](index.md)

Adds a header line to the request.

The header line must be a valid http header line, i.e. it must
have the format "Name:Value", and it must not contain non-ASCII
characters.

### Parameters

`headerLine` - the header line to add to the request

### Exceptions

`IllegalArgumentException` - if headerLine is invalid`fun add(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`UserHeaders`](index.md)

Adds a header to the request.

The name and value must not contain non-ASCII characters.

### Parameters

`name` - the name of the header to add

`value` - the value of the header

### Exceptions

`IllegalArgumentException` - if name or value is invalid`fun add(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`Date`](https://docs.oracle.com/javase/6/docs/api/java/util/Date.html)`): `[`UserHeaders`](index.md)

Adds a header to the request.

The name must not contain non-ASCII characters.
The value will be formatted as a http date &lt;&gt;

### Parameters

`name` - the name of the header to add

`value` - the value of the header

### Exceptions

`IllegalArgumentException` - if name or value is invalid