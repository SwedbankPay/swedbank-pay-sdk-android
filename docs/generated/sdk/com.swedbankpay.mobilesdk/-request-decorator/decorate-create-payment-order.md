//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[RequestDecorator](index.md)/[decorateCreatePaymentOrder](decorate-create-payment-order.md)



# decorateCreatePaymentOrder  
[androidJvm]  
Content  
open suspend fun [decorateCreatePaymentOrder](decorate-create-payment-order.md)(userHeaders: [UserHeaders](../-user-headers/index.md), body: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), paymentOrder: [PaymentOrder](../-payment-order/index.md))  
More info  


Override this method to add custom headers to the POST {paymentorders} request.



The default implementation does nothing.



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/RequestDecorator/decorateCreatePaymentOrder/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a>userHeaders| <a name="com.swedbankpay.mobilesdk/RequestDecorator/decorateCreatePaymentOrder/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a><br><br>headers added to this will be sent with the request<br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecorator/decorateCreatePaymentOrder/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a>body| <a name="com.swedbankpay.mobilesdk/RequestDecorator/decorateCreatePaymentOrder/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a><br><br>the body of the request<br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecorator/decorateCreatePaymentOrder/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a>paymentOrder| <a name="com.swedbankpay.mobilesdk/RequestDecorator/decorateCreatePaymentOrder/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.PaymentOrder/PointingToDeclaration/"></a><br><br>the payment order used to create the request body<br><br>|
  
  



