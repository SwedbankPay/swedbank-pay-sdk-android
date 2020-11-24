[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [ConfigurationCompat](index.md) / [shouldRetryAfterPostPaymentordersExceptionCompat](./should-retry-after-post-paymentorders-exception-compat.md)

# shouldRetryAfterPostPaymentordersExceptionCompat

`@WorkerThread open fun shouldRetryAfterPostPaymentordersExceptionCompat(exception: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Called by [PaymentFragment](../-payment-fragment/index.md) to determine whether it should fail or allow
retry after it failed to create the payment order.

### Parameters

`exception` - the exception that caused the failure

**Return**
`true` if retry should be allowed, `false` otherwise
