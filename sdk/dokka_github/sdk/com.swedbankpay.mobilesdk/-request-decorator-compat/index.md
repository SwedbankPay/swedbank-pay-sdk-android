[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RequestDecoratorCompat](./index.md)

# RequestDecoratorCompat

`@WorkerThread open class RequestDecoratorCompat : `[`RequestDecorator`](../-request-decorator/index.md)

Java compatibility wrapper for [RequestDecorator](../-request-decorator/index.md).

For each callback defined in [RequestDecorator](../-request-decorator/index.md), this class
contains a corresponding callback but without the suspend modifier.
The suspending methods of [RequestDecorator](../-request-decorator/index.md) invoke the corresponding
regular methods using the
[IO Dispatcher](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-i-o.html).
This means your callbacks run in a background thread, so be careful with synchronization.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Java compatibility wrapper for [RequestDecorator](../-request-decorator/index.md).`RequestDecoratorCompat()` |

### Functions

| Name | Summary |
|---|---|
| [decorateAnyRequest](decorate-any-request.md) | Override this method to add custom headers to all backend requests.`suspend fun decorateAnyRequest(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, method: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateAnyRequestCompat](decorate-any-request-compat.md) | Override this method to add custom headers to all backend requests.`open fun decorateAnyRequestCompat(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, method: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateCreatePaymentOrder](decorate-create-payment-order.md) | Override this method to add custom headers to the POST {paymentorders} request.`suspend fun decorateCreatePaymentOrder(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, paymentOrder: `[`PaymentOrder`](../-payment-order/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateCreatePaymentOrderCompat](decorate-create-payment-order-compat.md) | Override this method to add custom headers to the POST {paymentorders} request.`open fun decorateCreatePaymentOrderCompat(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, paymentOrder: `[`PaymentOrder`](../-payment-order/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateGetPaymentOrder](decorate-get-payment-order.md) | Override this method to add custom headers to the GET {paymentorder} request.`suspend fun decorateGetPaymentOrder(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateGetPaymentOrderCompat](decorate-get-payment-order-compat.md) | Override this method to add custom headers to the GET {paymentorder} request.`open fun decorateGetPaymentOrderCompat(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateGetTopLevelResources](decorate-get-top-level-resources.md) | Override this method to add custom headers to the backend entry point request.`suspend fun decorateGetTopLevelResources(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateGetTopLevelResourcesCompat](decorate-get-top-level-resources-compat.md) | Override this method to add custom headers to the backend entry point request.`open fun decorateGetTopLevelResourcesCompat(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateInitiateConsumerSession](decorate-initiate-consumer-session.md) | Override this method to add custom headers to the POST {consumers} request.`suspend fun decorateInitiateConsumerSession(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, consumer: `[`Consumer`](../-consumer/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateInitiateConsumerSessionCompat](decorate-initiate-consumer-session-compat.md) | Override this method to add custom headers to the POST {consumers} request.`open fun decorateInitiateConsumerSessionCompat(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, consumer: `[`Consumer`](../-consumer/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
