//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[Configuration](index.md)/[getErrorMessage](get-error-message.md)



# getErrorMessage  
[androidJvm]  
Content  
open fun [getErrorMessage](get-error-message.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), exception: [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?  
More info  


Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to show an error message because an operation failed.



You can return null if you have no further details to provide.



#### Return  


an error message



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/Configuration/getErrorMessage/#android.content.Context#java.lang.Exception/PointingToDeclaration/"></a>context| <a name="com.swedbankpay.mobilesdk/Configuration/getErrorMessage/#android.content.Context#java.lang.Exception/PointingToDeclaration/"></a><br><br>an application context<br><br>|
| <a name="com.swedbankpay.mobilesdk/Configuration/getErrorMessage/#android.content.Context#java.lang.Exception/PointingToDeclaration/"></a>exception| <a name="com.swedbankpay.mobilesdk/Configuration/getErrorMessage/#android.content.Context#java.lang.Exception/PointingToDeclaration/"></a><br><br>the exception that caused the failure<br><br>|
  
  



