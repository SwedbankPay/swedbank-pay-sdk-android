//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[RequestDecoratorCompat](index.md)/[decoratePaymentOrderSetInstrumentCompat](decorate-payment-order-set-instrument-compat.md)



# decoratePaymentOrderSetInstrumentCompat  
[androidJvm]  
Content  
open fun [decoratePaymentOrderSetInstrumentCompat](decorate-payment-order-set-instrument-compat.md)(userHeaders: [UserHeaders](../-user-headers/index.md), url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), body: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), instrument: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))  
More info  


Override this method to add custom headers to the PATCH {setInstrument} request of a payment order.



The default implementation does nothing.



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decoratePaymentOrderSetInstrumentCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String/PointingToDeclaration/"></a>userHeaders| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decoratePaymentOrderSetInstrumentCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String/PointingToDeclaration/"></a><br><br>headers added to this will be sent with the request<br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decoratePaymentOrderSetInstrumentCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String/PointingToDeclaration/"></a>url| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decoratePaymentOrderSetInstrumentCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String/PointingToDeclaration/"></a><br><br>the url of the request<br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decoratePaymentOrderSetInstrumentCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String/PointingToDeclaration/"></a>body| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decoratePaymentOrderSetInstrumentCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String/PointingToDeclaration/"></a><br><br>the body of the request<br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decoratePaymentOrderSetInstrumentCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String/PointingToDeclaration/"></a>instrument| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decoratePaymentOrderSetInstrumentCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#kotlin.String#kotlin.String/PointingToDeclaration/"></a><br><br>the instrument used to create the request body<br><br>|
  
  



