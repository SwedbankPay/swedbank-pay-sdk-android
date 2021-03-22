//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[ViewConsumerIdentificationInfo](index.md)



# ViewConsumerIdentificationInfo  
 [androidJvm] data class [ViewConsumerIdentificationInfo](index.md)(**webViewBaseUrl**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **viewConsumerIdentification**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))

Data required to show the checkin view.



If you provide a custom [Configuration](../-configuration/index.md), you must get the relevant data from your services and return a ViewConsumerIdentificationInfo object in your [Configuration.postConsumers](../-configuration/post-consumers.md) method.

   


## Constructors  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/ViewConsumerIdentificationInfo/ViewConsumerIdentificationInfo/#kotlin.String?#kotlin.String/PointingToDeclaration/"></a>[ViewConsumerIdentificationInfo](-view-consumer-identification-info.md)| <a name="com.swedbankpay.mobilesdk/ViewConsumerIdentificationInfo/ViewConsumerIdentificationInfo/#kotlin.String?#kotlin.String/PointingToDeclaration/"></a> [androidJvm] fun [ViewConsumerIdentificationInfo](-view-consumer-identification-info.md)(webViewBaseUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, viewConsumerIdentification: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))   <br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/ViewConsumerIdentificationInfo/viewConsumerIdentification/#/PointingToDeclaration/"></a>[viewConsumerIdentification](view-consumer-identification.md)| <a name="com.swedbankpay.mobilesdk/ViewConsumerIdentificationInfo/viewConsumerIdentification/#/PointingToDeclaration/"></a> [androidJvm] val [viewConsumerIdentification](view-consumer-identification.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The view-consumer-identification link from Swedbank Pay.   <br>|
| <a name="com.swedbankpay.mobilesdk/ViewConsumerIdentificationInfo/webViewBaseUrl/#/PointingToDeclaration/"></a>[webViewBaseUrl](web-view-base-url.md)| <a name="com.swedbankpay.mobilesdk/ViewConsumerIdentificationInfo/webViewBaseUrl/#/PointingToDeclaration/"></a> [androidJvm] val [webViewBaseUrl](web-view-base-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullThe url to use as the [android.webkit.WebView](https://developer.android.com/reference/kotlin/android/webkit/WebView.html) page url when showing the checkin UI.   <br>|

