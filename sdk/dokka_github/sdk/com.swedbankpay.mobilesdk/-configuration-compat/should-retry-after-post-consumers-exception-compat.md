[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [ConfigurationCompat](index.md) / [shouldRetryAfterPostConsumersExceptionCompat](./should-retry-after-post-consumers-exception-compat.md)

# shouldRetryAfterPostConsumersExceptionCompat

`@WorkerThread open fun shouldRetryAfterPostConsumersExceptionCompat(exception: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Called by [PaymentFragment](../-payment-fragment/index.md) to determine whether it should fail or allow
retry after it failed to start a consumer identification session.

### Parameters

`exception` - the exception that caused the failure

**Return**
`true` if retry should be allowed, `false` otherwise

