[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [UserHeaders](index.md) / [addNonAscii](./add-non-ascii.md)

# addNonAscii

`fun addNonAscii(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`UserHeaders`](index.md)

Adds a header without validating the value.

The name still must not contain non-ASCII characters. However,
the value can contain arbitrary characters.

N.B! The header value will be encoded in UTF-8. Be sure
your backend expects this. Non-ASCII characters in headers are
NOT valid in HTTP/1.1.

### Parameters

`name` - the name of the header to add

`value` - the value of the header

### Exceptions

`IllegalArgumentException` - if name is invalid