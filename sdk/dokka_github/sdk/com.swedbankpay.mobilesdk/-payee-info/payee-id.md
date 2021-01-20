[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PayeeInfo](index.md) / [payeeId](./payee-id.md)

# payeeId

`val payeeId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

The unique identifier of this payee set by Swedbank Pay.

This is usually the Merchant ID. However, usually best idea to set this value in your backend
instead. Thus, this property defaults to the empty string, but it is included in the data
model for completeness.

