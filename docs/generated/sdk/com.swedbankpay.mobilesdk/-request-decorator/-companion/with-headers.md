//[sdk](../../../../index.md)/[com.swedbankpay.mobilesdk](../../index.md)/[RequestDecorator](../index.md)/[Companion](index.md)/[withHeaders](with-headers.md)



# withHeaders  
[androidJvm]  
Content  
@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)()  
  
fun [withHeaders](with-headers.md)(vararg namesAndValues: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [RequestDecorator](../index.md)  
More info  


Create a RequestDecorator that attaches the specified headers to all SDK requests.



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/RequestDecorator.Companion/withHeaders/#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a>namesAndValues| <a name="com.swedbankpay.mobilesdk/RequestDecorator.Companion/withHeaders/#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a><br><br>the header names and values, alternating<br><br>|
  
  


[androidJvm]  
Content  
@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)()  
  
fun [withHeaders](with-headers.md)(headers: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>): [RequestDecorator](../index.md)  
More info  


Create a RequestDecorator that attaches the specified headers to all SDK requests.



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/RequestDecorator.Companion/withHeaders/#kotlin.collections.Map[kotlin.String,kotlin.String]/PointingToDeclaration/"></a>headers| <a name="com.swedbankpay.mobilesdk/RequestDecorator.Companion/withHeaders/#kotlin.collections.Map[kotlin.String,kotlin.String]/PointingToDeclaration/"></a><br><br>map of header names to corresponding values<br><br>|
  
  



