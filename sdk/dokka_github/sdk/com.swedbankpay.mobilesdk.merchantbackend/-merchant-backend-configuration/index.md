[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [MerchantBackendConfiguration](./index.md)

# MerchantBackendConfiguration

`class MerchantBackendConfiguration : `[`Configuration`](../../com.swedbankpay.mobilesdk/-configuration/index.md)

A [Configuration](../../com.swedbankpay.mobilesdk/-configuration/index.md) class for the Merchant Backend API.

Get an instance using [Builder](-builder/index.md).

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | A builder object for [MerchantBackendConfiguration](./index.md).`class Builder` |

### Properties

| Name | Summary |
|---|---|
| [backendUrl](backend-url.md) | The URL of the Merchant Backend.`val backendUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [getErrorMessage](get-error-message.md) | Called by [PaymentFragment](../../com.swedbankpay.mobilesdk/-payment-fragment/index.md) when it needs to show an error message because an operation failed.`fun getErrorMessage(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, exception: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [postConsumers](post-consumers.md) | Called by [PaymentFragment](../../com.swedbankpay.mobilesdk/-payment-fragment/index.md) when it needs to start a consumer identification session. Your implementation must ultimately make the call to Swedbank Pay API and return a [ViewConsumerIdentificationInfo](../../com.swedbankpay.mobilesdk/-view-consumer-identification-info/index.md) describing the result.`suspend fun postConsumers(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, consumer: `[`Consumer`](../../com.swedbankpay.mobilesdk/-consumer/index.md)`?, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`ViewConsumerIdentificationInfo`](../../com.swedbankpay.mobilesdk/-view-consumer-identification-info/index.md) |
| [postPaymentorders](post-paymentorders.md) | Called by [PaymentFragment](../../com.swedbankpay.mobilesdk/-payment-fragment/index.md) when it needs to create a payment order. Your implementation must ultimately make the call to Swedbank Pay API and return a [ViewPaymentOrderInfo](../../com.swedbankpay.mobilesdk/-view-payment-order-info/index.md) describing the result.`suspend fun postPaymentorders(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, paymentOrder: `[`PaymentOrder`](../../com.swedbankpay.mobilesdk/-payment-order/index.md)`?, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, consumerProfileRef: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`ViewPaymentOrderInfo`](../../com.swedbankpay.mobilesdk/-view-payment-order-info/index.md) |
| [shouldRetryAfterPostConsumersException](should-retry-after-post-consumers-exception.md) | Called by [PaymentFragment](../../com.swedbankpay.mobilesdk/-payment-fragment/index.md) to determine whether it should fail or allow retry after it failed to start a consumer identification session.`suspend fun shouldRetryAfterPostConsumersException(exception: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [shouldRetryAfterPostPaymentordersException](should-retry-after-post-paymentorders-exception.md) | Called by [PaymentFragment](../../com.swedbankpay.mobilesdk/-payment-fragment/index.md) to determine whether it should fail or allow retry after it failed to create the payment order.`suspend fun shouldRetryAfterPostPaymentordersException(exception: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [updatePaymentOrder](update-payment-order.md) | Called by [PaymentFragment](../../com.swedbankpay.mobilesdk/-payment-fragment/index.md) when it needs to update a payment order.`suspend fun updatePaymentOrder(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, paymentOrder: `[`PaymentOrder`](../../com.swedbankpay.mobilesdk/-payment-order/index.md)`?, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, viewPaymentOrderInfo: `[`ViewPaymentOrderInfo`](../../com.swedbankpay.mobilesdk/-view-payment-order-info/index.md)`, updateInfo: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`ViewPaymentOrderInfo`](../../com.swedbankpay.mobilesdk/-view-payment-order-info/index.md) |
