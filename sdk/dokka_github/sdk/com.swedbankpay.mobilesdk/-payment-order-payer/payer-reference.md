[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentOrderPayer](index.md) / [payerReference](./payer-reference.md)

# payerReference

`val payerReference: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

An opaque, unique reference to the payer. Alternative to the other fields.

Using `payerReference` is required when generating or using payment tokens (N.B! not
recurrence tokens).

If you use `payerReference`, you should not set the other fields.
The `payerReference` must be unique to a payer, and your backend must have access control
such that is ensures that the `payerReference` is owned by the authenticated user.
It is usually best to only populate this field in the backend.

