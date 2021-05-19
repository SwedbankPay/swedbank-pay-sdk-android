//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../index.md)/[MerchantBackendProblem](index.md)



# MerchantBackendProblem  
 [androidJvm] sealed class [MerchantBackendProblem](index.md) : [Problem](../../com.swedbankpay.mobilesdk/-problem/index.md)

Base class for any problems encountered in the payment.



Problems always result from communication with the backend; lower-level network errors are not represented by Problems, but rather by IOExceptions as is usual.



Swedbank interfaces, as well as the example merchant backend, report problems using the Problem Details for HTTP APIs protocol (https://tools.ietf.org/html/rfc7807), specifically the json representation. Your custom merchant backend is enouraged to do so as well. These classes provide a convenient java representation of the problems so your client code does not need to deal with the raw json. Any custom problem cases you add to your merchant backend will be reported as "Unknown" problems, and you will have to implement parsing for those in your client, of course.



All problems are either [Client](-client/index.md) or [Server](-server/index.md) problems. A Client problem is one where there was something wrong with the request the client app sent to the service. A Client problem always implies an HTTP response status in the Client Error range, 400-499.



A Server problem in one where the service understood the request, but could not fulfill it. If the backend responds in an unexpected manner, the situation will be interpreted as a Server error, unless the response status is in 400-499, in which case it is still considered a Client error.



This separation to Client and Server errors provides a crude but often effective way of distinguishing between temporary service unavailability and permanent configuration errors. Indeed, the PaymentFragment will internally consider any Client errors to be fatal, but most Server errors to be retryable.



Client and Server errors are further divided to specific types. See individual class documentation for details.

   


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client///PointingToDeclaration/"></a>[Client](-client/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>sealed class [Client](-client/index.md) : [MerchantBackendProblem](index.md)  <br>More info  <br>Base class for [Problems](index.md) caused by the service refusing or not understanding a request sent to it by the client.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server///PointingToDeclaration/"></a>[Server](-server/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>sealed class [Server](-server/index.md) : [MerchantBackendProblem](index.md)  <br>More info  <br>Base class for [Problems](index.md) caused by the service backend.  <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[describeContents](-server/-unknown/index.md#-1578325224%2FFunctions%2F462465411)| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>abstract fun [describeContents](-server/-unknown/index.md#-1578325224%2FFunctions%2F462465411)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../com.swedbankpay.mobilesdk/-problem/equals.md)| <a name="com.swedbankpay.mobilesdk/Problem/equals/#kotlin.Any?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open operator override fun [equals](../../com.swedbankpay.mobilesdk/-problem/equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../com.swedbankpay.mobilesdk/-problem/hash-code.md)| <a name="com.swedbankpay.mobilesdk/Problem/hashCode/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [hashCode](../../com.swedbankpay.mobilesdk/-problem/hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/toString/#/PointingToDeclaration/"></a>[toString](../../com.swedbankpay.mobilesdk/-problem/to-string.md)| <a name="com.swedbankpay.mobilesdk/Problem/toString/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [toString](../../com.swedbankpay.mobilesdk/-problem/to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](write-to-parcel.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [writeToParcel](write-to-parcel.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/detail/#/PointingToDeclaration/"></a>[detail](index.md#-1889271045%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/detail/#/PointingToDeclaration/"></a> [androidJvm] val [detail](index.md#-1889271045%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a detailed explanation of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/instance/#/PointingToDeclaration/"></a>[instance](index.md#282344311%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/instance/#/PointingToDeclaration/"></a> [androidJvm] val [instance](index.md#282344311%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a URI reference that identifies the specific occurrence of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/jsonObject/#/PointingToDeclaration/"></a>[jsonObject](index.md#1435541061%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/jsonObject/#/PointingToDeclaration/"></a> [androidJvm] val [jsonObject](index.md#1435541061%2FProperties%2F462465411): JsonObjectThe raw RFC 7807 object parsed as a Gson JsonObject.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/raw/#/PointingToDeclaration/"></a>[raw](index.md#809266310%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/raw/#/PointingToDeclaration/"></a> [androidJvm] val [raw](index.md#809266310%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The raw RFC 7807 object.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/status/#/PointingToDeclaration/"></a>[status](index.md#1585017466%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/status/#/PointingToDeclaration/"></a> [androidJvm] val [status](index.md#1585017466%2FProperties%2F462465411): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?RFC 7807 default property: the HTTP status codeThis should always be the same as the actual HTTP status code reported by the server.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/title/#/PointingToDeclaration/"></a>[title](index.md#-1937530858%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/title/#/PointingToDeclaration/"></a> [androidJvm] val [title](index.md#-1937530858%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a short summary of the problem.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/type/#/PointingToDeclaration/"></a>[type](index.md#1875559410%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/type/#/PointingToDeclaration/"></a> [androidJvm] val [type](index.md#1875559410%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)RFC 7807 default property: a URI reference that identifies the problem type.   <br>|


## Inheritors  
  
|  Name | 
|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client///PointingToDeclaration/"></a>[MerchantBackendProblem](-client/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server///PointingToDeclaration/"></a>[MerchantBackendProblem](-server/index.md)|

