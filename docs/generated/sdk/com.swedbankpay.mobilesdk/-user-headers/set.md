//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[UserHeaders](index.md)/[set](set.md)



# set  
[androidJvm]  
Content  
fun [set](set.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [UserHeaders](index.md)  
More info  


Sets a header in the request.



The name and value must not contain non-ASCII characters.



If a header with the same name has not been [add](add.md)ed or [set](set.md), this functions identically to [add](add.md). Otherwise the new value will replace any previous value.



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#kotlin.String/PointingToDeclaration/"></a>name| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#kotlin.String/PointingToDeclaration/"></a><br><br>the name of the header to add<br><br>|
| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#kotlin.String/PointingToDeclaration/"></a>value| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#kotlin.String/PointingToDeclaration/"></a><br><br>the value of the header<br><br>|
  


#### Throws  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#kotlin.String/PointingToDeclaration/"></a>[kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html)| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#kotlin.String/PointingToDeclaration/"></a><br><br>if name or value is invalid<br><br>|
  


[androidJvm]  
Content  
fun [set](set.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [Date](https://developer.android.com/reference/kotlin/java/util/Date.html)): [UserHeaders](index.md)  
More info  


Sets a header in the request.



The name must not contain non-ASCII characters. The value will be formatted as a http date [https://tools.ietf.org/html/rfc7231#section-7.1.1.2](https://tools.ietf.org/html/rfc7231#section-7.1.1.2)



If a header with the same name has not been [add](add.md)ed or [set](set.md), this functions identically to [add](add.md). Otherwise the new value will replace any previous value.



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#java.util.Date/PointingToDeclaration/"></a>name| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#java.util.Date/PointingToDeclaration/"></a><br><br>the name of the header to add<br><br>|
| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#java.util.Date/PointingToDeclaration/"></a>value| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#java.util.Date/PointingToDeclaration/"></a><br><br>the value of the header<br><br>|
  


#### Throws  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#java.util.Date/PointingToDeclaration/"></a>[kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html)| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#java.util.Date/PointingToDeclaration/"></a><br><br>if name or value is invalid<br><br>|
  



