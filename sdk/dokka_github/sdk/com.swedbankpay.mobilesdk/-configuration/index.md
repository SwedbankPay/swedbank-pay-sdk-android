[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [Configuration](./index.md)

# Configuration

`abstract class Configuration`

The Swedbank Pay configuration for your application.

You need a Configuration to use [PaymentFragment](../-payment-fragment/index.md).
If you want to use a custom way of communicating with your services,
you can create a subclass of Configuration.
If you wish to use the specified Merchant Backend API,
create a
[MerchantBackendConfiguration](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-configuration/index.md)
using
[MerchantBackendConfiguration.Builder](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-configuration/-builder/index.md).

In most cases, it is enough to set a
[default Configuration](../-payment-fragment/default-configuration.md).
However, for more advanced situations, you may override [PaymentFragment.getConfiguration](../-payment-fragment/get-configuration.md)
to provide a Configuration dynamically.

N.B! Configuration is specified as `suspend` functions, i.e. Kotlin coroutines.
As Java does not support these, a [compatibility class](../-configuration-compat/index.md)
is provided.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | The Swedbank Pay configuration for your application.`Configuration()` |

### Functions

| Name | Summary |
|---|---|
| [getErrorMessage](get-error-message.md) | Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to show an error message because an operation failed.`open fun getErrorMessage(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, exception: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [postConsumers](post-consumers.md) | Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to start a consumer identification session. Your implementation must ultimately make the call to Swedbank Pay API and return a [ViewConsumerIdentificationInfo](../-view-consumer-identification-info/index.md) describing the result.`abstract suspend fun postConsumers(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, consumer: `[`Consumer`](../-consumer/index.md)`?, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`ViewConsumerIdentificationInfo`](../-view-consumer-identification-info/index.md) |
| [postPaymentorders](post-paymentorders.md) | Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to create a payment order. Your implementation must ultimately make the call to Swedbank Pay API and return a [ViewPaymentOrderInfo](../-view-payment-order-info/index.md) describing the result.`abstract suspend fun postPaymentorders(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, paymentOrder: `[`PaymentOrder`](../-payment-order/index.md)`?, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, consumerProfileRef: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`ViewPaymentOrderInfo`](../-view-payment-order-info/index.md) |
| [shouldRetryAfterPostConsumersException](should-retry-after-post-consumers-exception.md) | Called by [PaymentFragment](../-payment-fragment/index.md) to determine whether it should fail or allow retry after it failed to start a consumer identification session.`open suspend fun shouldRetryAfterPostConsumersException(exception: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [shouldRetryAfterPostPaymentordersException](should-retry-after-post-paymentorders-exception.md) | Called by [PaymentFragment](../-payment-fragment/index.md) to determine whether it should fail or allow retry after it failed to create the payment order.`open suspend fun shouldRetryAfterPostPaymentordersException(exception: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [updatePaymentOrder](update-payment-order.md) | Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to update a payment order.`open suspend fun updatePaymentOrder(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, paymentOrder: `[`PaymentOrder`](../-payment-order/index.md)`?, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, viewPaymentOrderInfo: `[`ViewPaymentOrderInfo`](../-view-payment-order-info/index.md)`, updateInfo: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`ViewPaymentOrderInfo`](../-view-payment-order-info/index.md) |

### Inheritors

| Name | Summary |
|---|---|
| [ConfigurationCompat](../-configuration-compat/index.md) | Java compatibility wrapper for [Configuration](./index.md).`abstract class ConfigurationCompat : `[`Configuration`](./index.md) |
| [MerchantBackendConfiguration](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-configuration/index.md) | A [Configuration](./index.md) class for the Merchant Backend API.`class MerchantBackendConfiguration : `[`Configuration`](./index.md) |
