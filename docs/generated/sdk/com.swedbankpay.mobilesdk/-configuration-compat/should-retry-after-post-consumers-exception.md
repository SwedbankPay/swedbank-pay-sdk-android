//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[ConfigurationCompat](index.md)/[shouldRetryAfterPostConsumersException](should-retry-after-post-consumers-exception.md)



# shouldRetryAfterPostConsumersException  
[androidJvm]  
Content  
suspend override fun [shouldRetryAfterPostConsumersException](should-retry-after-post-consumers-exception.md)(exception: [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  
More info  


Called by [PaymentFragment](../-payment-fragment/index.md) to determine whether it should fail or allow retry after it failed to start a consumer identification session.



#### Return  


true if retry should be allowed, false otherwise



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat/shouldRetryAfterPostConsumersException/#java.lang.Exception/PointingToDeclaration/"></a>exception| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat/shouldRetryAfterPostConsumersException/#java.lang.Exception/PointingToDeclaration/"></a><br><br>the exception that caused the failure<br><br>|
  
  



