[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [ConfigurationCompat](index.md) / [postConsumers](./post-consumers.md)

# postConsumers

`suspend fun postConsumers(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, consumer: `[`Consumer`](../-consumer/index.md)`?, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`ViewConsumerIdentificationInfo`](../-view-consumer-identification-info/index.md)

Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to start a consumer identification
session. Your implementation must ultimately make the call to Swedbank Pay API
and return a [ViewConsumerIdentificationInfo](../-view-consumer-identification-info/index.md) describing the result.

### Parameters

`context` - an application context

`consumer` - the [Consumer](../-consumer/index.md) object set as the PaymentFragment argument

`userData` - the user data object set as the PaymentFragment argument

**Return**
ViewConsumerIdentificationInfo describing the consumer identification session

