[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [MerchantBackendConfiguration](index.md) / [postConsumers](./post-consumers.md)

# postConsumers

`suspend fun postConsumers(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, consumer: `[`Consumer`](../../com.swedbankpay.mobilesdk/-consumer/index.md)`?, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`ViewConsumerIdentificationInfo`](../../com.swedbankpay.mobilesdk/-view-consumer-identification-info/index.md)

Called by [PaymentFragment](../../com.swedbankpay.mobilesdk/-payment-fragment/index.md) when it needs to start a consumer identification
session. Your implementation must ultimately make the call to Swedbank Pay API
and return a [ViewConsumerIdentificationInfo](../../com.swedbankpay.mobilesdk/-view-consumer-identification-info/index.md) describing the result.

### Parameters

`context` - an application context

`consumer` - the [Consumer](../../com.swedbankpay.mobilesdk/-consumer/index.md) object set as the PaymentFragment argument

`userData` - the user data object set as the PaymentFragment argument

**Return**
ViewConsumerIdentificationInfo describing the consumer identification session
