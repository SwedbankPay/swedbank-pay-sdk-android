//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[RequestDecorator](index.md)/[decorateInitiateConsumerSession](decorate-initiate-consumer-session.md)



# decorateInitiateConsumerSession  
[androidJvm]  
Content  
open suspend fun [decorateInitiateConsumerSession](decorate-initiate-consumer-session.md)(userHeaders: [UserHeaders](../-user-headers/index.md), body: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), consumer: [Consumer](../-consumer/index.md))  
More info  


Override this method to add custom headers to the POST {consumers} request.



The default implementation does nothing.



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/RequestDecorator/decorateInitiateConsumerSession/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.Consumer/PointingToDeclaration/"></a>userHeaders| <a name="com.swedbankpay.mobilesdk/RequestDecorator/decorateInitiateConsumerSession/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.Consumer/PointingToDeclaration/"></a><br><br>headers added to this will be sent with the request<br><br>|
| <a name="com.swedbankpay.mobilesdk/RequestDecorator/decorateInitiateConsumerSession/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.Consumer/PointingToDeclaration/"></a>body| <a name="com.swedbankpay.mobilesdk/RequestDecorator/decorateInitiateConsumerSession/#com.swedbankpay.mobilesdk.UserHeaders#kotlin.String#com.swedbankpay.mobilesdk.Consumer/PointingToDeclaration/"></a><br><br>the body of the request<br><br>|
  
  



