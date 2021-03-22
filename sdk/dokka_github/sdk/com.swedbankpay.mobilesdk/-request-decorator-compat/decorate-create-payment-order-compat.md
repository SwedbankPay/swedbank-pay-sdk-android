//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[RequestDecoratorCompat](index.md)/[decorateCreatePaymentOrderCompat](decorate-create-payment-order-compat.md)



# decorateCreatePaymentOrderCompat  
[androidJvm]  
Content  
open fun [decorateCreatePaymentOrderCompat](decorate-create-payment-order-compat.md)(userHeaders: [UserHeaders](../-user-headers/index.md), body: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), paymentOrder: [PaymentOrder](../-payment-order/index.md))  
More info  


Override this method to add custom headers to the POST {paymentorders} request.



The default implementation does nothing.



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateCreatePaymentOrderCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a>userHeaders| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateCreatePaymentOrderCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a><br><br>headers added to this will be sent with the request<br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateCreatePaymentOrderCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a>body| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateCreatePaymentOrderCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a><br><br>the body of the request<br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateCreatePaymentOrderCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a>paymentOrder| <a name="com.swedbankpay.mobilesdk/RequestDecoratorCompat/decorateCreatePaymentOrderCompat/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a><br><br>the payment order used to create the request body<br><br>|
  
  



