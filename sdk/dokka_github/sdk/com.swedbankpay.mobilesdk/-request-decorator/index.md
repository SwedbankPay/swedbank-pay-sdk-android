[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RequestDecorator](./index.md)

# RequestDecorator

`abstract class RequestDecorator`

Callback for adding custom headers to backend requests.

All requests made to the merchant backend will call back to
the [decorateAnyRequest](decorate-any-request.md) method. This is a good place to add
API keys and session tokens and the like. Afterwards each request
will call back to its specific decoration method, where you can
add request-specific headers if such is relevant to your use-case.

The sequence of operations is this:

* SDK prepares a request
* decorateAnyRequest is called
* decorateAnyRequest returns
* decorate is called
* decorate returns
* the request is executed

Note that the methods in this class are Kotlin coroutines
(suspending functions). This way you can include long-running
tasks (e.g. network I/O) in your custom header creation. You must
not return from the callback until you have set all your headers;
indeed you must not call any methods on the passed UserHeaders object
after returning from the method.

There is a [Java compatibility class](../-request-decorator-compat/index.md) where the callbacks
are regular methods running in a background thread.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Callback for adding custom headers to backend requests.`RequestDecorator()` |

### Functions

| Name | Summary |
|---|---|
| [decorateAnyRequest](decorate-any-request.md) | Override this method to add custom headers to all backend requests.`open suspend fun decorateAnyRequest(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, method: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateCreatePaymentOrder](decorate-create-payment-order.md) | Override this method to add custom headers to the POST {paymentorders} request.`open suspend fun decorateCreatePaymentOrder(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, paymentOrder: `[`PaymentOrder`](../-payment-order/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateGetPaymentOrder](decorate-get-payment-order.md) | Override this method to add custom headers to the GET {paymentorder} request.`open suspend fun decorateGetPaymentOrder(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateGetTopLevelResources](decorate-get-top-level-resources.md) | Override this method to add custom headers to the backend entry point request.`open suspend fun decorateGetTopLevelResources(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [decorateInitiateConsumerSession](decorate-initiate-consumer-session.md) | Override this method to add custom headers to the POST {consumers} request.`open suspend fun decorateInitiateConsumerSession(userHeaders: `[`UserHeaders`](../-user-headers/index.md)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, consumer: `[`Consumer`](../-consumer/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [RequestDecoratorCompat](../-request-decorator-compat/index.md) | Java compatibility wrapper for [RequestDecorator](./index.md).`open class RequestDecoratorCompat : `[`RequestDecorator`](./index.md) |
