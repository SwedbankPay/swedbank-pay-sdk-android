//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[Configuration](index.md)



# Configuration  
 [androidJvm] abstract class [Configuration](index.md)

The Swedbank Pay configuration for your application.



You need a Configuration to use [PaymentFragment](../-payment-fragment/index.md). If you want to use a custom way of communicating with your services, you can create a subclass of Configuration. If you wish to use the specified Merchant Backend API, create a [MerchantBackendConfiguration](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-configuration/index.md) using [MerchantBackendConfiguration.Builder](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-configuration/-builder/index.md).



In most cases, it is enough to set a [default Configuration](../-payment-fragment/-companion/default-configuration.md). However, for more advanced situations, you may override [PaymentFragment.getConfiguration](../-payment-fragment/get-configuration.md) to provide a Configuration dynamically.



N.B! Configuration is specified as suspend functions, i.e. Kotlin coroutines. As Java does not support these, a [compatibility class](../-configuration-compat/index.md) is provided.

   


## Constructors  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/Configuration/Configuration/#/PointingToDeclaration/"></a>[Configuration](-configuration.md)| <a name="com.swedbankpay.mobilesdk/Configuration/Configuration/#/PointingToDeclaration/"></a> [androidJvm] fun [Configuration](-configuration.md)()   <br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/Configuration/getErrorMessage/#android.content.Context#java.lang.Exception/PointingToDeclaration/"></a>[getErrorMessage](get-error-message.md)| <a name="com.swedbankpay.mobilesdk/Configuration/getErrorMessage/#android.content.Context#java.lang.Exception/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open fun [getErrorMessage](get-error-message.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), exception: [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?  <br>More info  <br>Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to show an error message because an operation failed.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Configuration/postConsumers/#android.content.Context#com.swedbankpay.mobilesdk.Consumer?#kotlin.Any?/PointingToDeclaration/"></a>[postConsumers](post-consumers.md)| <a name="com.swedbankpay.mobilesdk/Configuration/postConsumers/#android.content.Context#com.swedbankpay.mobilesdk.Consumer?#kotlin.Any?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>abstract suspend fun [postConsumers](post-consumers.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), consumer: [Consumer](../-consumer/index.md)?, userData: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [ViewConsumerIdentificationInfo](../-view-consumer-identification-info/index.md)  <br>More info  <br>Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to start a consumer identification session.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Configuration/postPaymentorders/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#kotlin.String?/PointingToDeclaration/"></a>[postPaymentorders](post-paymentorders.md)| <a name="com.swedbankpay.mobilesdk/Configuration/postPaymentorders/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#kotlin.String?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>abstract suspend fun [postPaymentorders](post-paymentorders.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), paymentOrder: [PaymentOrder](../-payment-order/index.md)?, userData: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, consumerProfileRef: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): [ViewPaymentOrderInfo](../-view-payment-order-info/index.md)  <br>More info  <br>Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to create a payment order.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Configuration/shouldRetryAfterPostConsumersException/#java.lang.Exception/PointingToDeclaration/"></a>[shouldRetryAfterPostConsumersException](should-retry-after-post-consumers-exception.md)| <a name="com.swedbankpay.mobilesdk/Configuration/shouldRetryAfterPostConsumersException/#java.lang.Exception/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open suspend fun [shouldRetryAfterPostConsumersException](should-retry-after-post-consumers-exception.md)(exception: [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br>More info  <br>Called by [PaymentFragment](../-payment-fragment/index.md) to determine whether it should fail or allow retry after it failed to start a consumer identification session.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Configuration/shouldRetryAfterPostPaymentordersException/#java.lang.Exception/PointingToDeclaration/"></a>[shouldRetryAfterPostPaymentordersException](should-retry-after-post-paymentorders-exception.md)| <a name="com.swedbankpay.mobilesdk/Configuration/shouldRetryAfterPostPaymentordersException/#java.lang.Exception/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open suspend fun [shouldRetryAfterPostPaymentordersException](should-retry-after-post-paymentorders-exception.md)(exception: [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br>More info  <br>Called by [PaymentFragment](../-payment-fragment/index.md) to determine whether it should fail or allow retry after it failed to create the payment order.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Configuration/updatePaymentOrder/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#com.swedbankpay.mobilesdk.ViewPaymentOrderInfo#kotlin.Any?/PointingToDeclaration/"></a>[updatePaymentOrder](update-payment-order.md)| <a name="com.swedbankpay.mobilesdk/Configuration/updatePaymentOrder/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#com.swedbankpay.mobilesdk.ViewPaymentOrderInfo#kotlin.Any?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open suspend fun [updatePaymentOrder](update-payment-order.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), paymentOrder: [PaymentOrder](../-payment-order/index.md)?, userData: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, viewPaymentOrderInfo: [ViewPaymentOrderInfo](../-view-payment-order-info/index.md), updateInfo: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [ViewPaymentOrderInfo](../-view-payment-order-info/index.md)  <br>More info  <br>Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to update a payment order.  <br><br><br>|


## Inheritors  
  
|  Name | 
|---|
| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat///PointingToDeclaration/"></a>[ConfigurationCompat](../-configuration-compat/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration///PointingToDeclaration/"></a>[MerchantBackendConfiguration](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-configuration/index.md)|

