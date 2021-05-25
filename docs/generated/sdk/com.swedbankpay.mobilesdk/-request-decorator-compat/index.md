//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[RequestDecoratorCompat](index.md)



# RequestDecoratorCompat  
 [androidJvm] @[WorkerThread](https://developer.android.com/reference/kotlin/androidx/annotation/WorkerThread.html)()  
  
open class [RequestDecoratorCompat](index.md) : [RequestDecorator](../-request-decorator/index.md)

Java compatibility wrapper for [RequestDecorator](../-request-decorator/index.md).



For each callback defined in [RequestDecorator](../-request-decorator/index.md), this class contains a corresponding callback but without the suspend modifier. The suspending methods of [RequestDecorator](../-request-decorator/index.md) invoke the corresponding regular methods using the [IO Dispatcher](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-i-o.html). This means your callbacks run in a background thread, so be careful with synchronization.

   


## Constructors  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/RequestDecoratorCompat/#/PointingToDeclaration/"></a>[RequestDecoratorCompat](-request-decorator-compat.md)| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/RequestDecoratorCompat/#/PointingToDeclaration/"></a> [androidJvm] fun [RequestDecoratorCompat](-request-decorator-compat.md)()   <br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateAnyRequest/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String?/PointingToDeclaration/"></a>[decorateAnyRequest](decorate-any-request.md)| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateAnyRequest/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>suspend override fun [decorateAnyRequest](decorate-any-request.md)(userHeaders: [UserHeaders](../-user-headers/index.md), method: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), body: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?)  <br>More info  <br>Override this method to add custom headers to all backend requests.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateAnyRequestCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String?/PointingToDeclaration/"></a>[decorateAnyRequestCompat](decorate-any-request-compat.md)| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateAnyRequestCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open fun [decorateAnyRequestCompat](decorate-any-request-compat.md)(userHeaders: [UserHeaders](../-user-headers/index.md), method: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), body: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?)  <br>More info  <br>Override this method to add custom headers to all backend requests.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateCreatePaymentOrder/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a>[decorateCreatePaymentOrder](decorate-create-payment-order.md)| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateCreatePaymentOrder/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>suspend override fun [decorateCreatePaymentOrder](decorate-create-payment-order.md)(userHeaders: [UserHeaders](../-user-headers/index.md), body: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), paymentOrder: [PaymentOrder](../-payment-order/index.md))  <br>More info  <br>Override this method to add custom headers to the POST {paymentorders} request.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateCreatePaymentOrderCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a>[decorateCreatePaymentOrderCompat](decorate-create-payment-order-compat.md)| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateCreatePaymentOrderCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open fun [decorateCreatePaymentOrderCompat](decorate-create-payment-order-compat.md)(userHeaders: [UserHeaders](../-user-headers/index.md), body: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), paymentOrder: [PaymentOrder](../-payment-order/index.md))  <br>More info  <br>Override this method to add custom headers to the POST {paymentorders} request.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateGetTopLevelResources/#com.swedbankpay.mobilesdk.UserHeaders/PointingToDeclaration/"></a>[decorateGetTopLevelResources](decorate-get-top-level-resources.md)| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateGetTopLevelResources/#com.swedbankpay.mobilesdk.UserHeaders/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>suspend override fun [decorateGetTopLevelResources](decorate-get-top-level-resources.md)(userHeaders: [UserHeaders](../-user-headers/index.md))  <br>More info  <br>Override this method to add custom headers to the backend entry point request.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateGetTopLevelResourcesCompat/#com.swedbankpay.mobilesdk.UserHeaders/PointingToDeclaration/"></a>[decorateGetTopLevelResourcesCompat](decorate-get-top-level-resources-compat.md)| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateGetTopLevelResourcesCompat/#com.swedbankpay.mobilesdk.UserHeaders/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open fun [decorateGetTopLevelResourcesCompat](decorate-get-top-level-resources-compat.md)(userHeaders: [UserHeaders](../-user-headers/index.md))  <br>More info  <br>Override this method to add custom headers to the backend entry point request.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateInitiateConsumerSession/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.Consumer/PointingToDeclaration/"></a>[decorateInitiateConsumerSession](decorate-initiate-consumer-session.md)| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateInitiateConsumerSession/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.Consumer/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>suspend override fun [decorateInitiateConsumerSession](decorate-initiate-consumer-session.md)(userHeaders: [UserHeaders](../-user-headers/index.md), body: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), consumer: [Consumer](../-consumer/index.md))  <br>More info  <br>Override this method to add custom headers to the POST {consumers} request.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateInitiateConsumerSessionCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.Consumer/PointingToDeclaration/"></a>[decorateInitiateConsumerSessionCompat](decorate-initiate-consumer-session-compat.md)| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateInitiateConsumerSessionCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.Consumer/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open fun [decorateInitiateConsumerSessionCompat](decorate-initiate-consumer-session-compat.md)(userHeaders: [UserHeaders](../-user-headers/index.md), body: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), consumer: [Consumer](../-consumer/index.md))  <br>More info  <br>Override this method to add custom headers to the POST {consumers} request.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decoratePaymentOrderSetInstrument/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String/PointingToDeclaration/"></a>[decoratePaymentOrderSetInstrument](decorate-payment-order-set-instrument.md)| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decoratePaymentOrderSetInstrument/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>suspend override fun [decoratePaymentOrderSetInstrument](decorate-payment-order-set-instrument.md)(userHeaders: [UserHeaders](../-user-headers/index.md), url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), body: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), instrument: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))  <br>More info  <br>Override this method to add custom headers to the PATCH {setInstrument} request of a payment order.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decoratePaymentOrderSetInstrumentCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String/PointingToDeclaration/"></a>[decoratePaymentOrderSetInstrumentCompat](decorate-payment-order-set-instrument-compat.md)| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decoratePaymentOrderSetInstrumentCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open fun [decoratePaymentOrderSetInstrumentCompat](decorate-payment-order-set-instrument-compat.md)(userHeaders: [UserHeaders](../-user-headers/index.md), url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), body: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), instrument: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))  <br>More info  <br>Override this method to add custom headers to the PATCH {setInstrument} request of a payment order.  <br><br><br>|
