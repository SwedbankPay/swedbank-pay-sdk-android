//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[Problem](index.md)



# Problem  
 [androidJvm] open class [Problem](index.md) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html), [Serializable](https://developer.android.com/reference/kotlin/java/io/Serializable.html)

An RFC 7807 HTTP API Problem Details object.



The SDK defines a subclass of Problem for problems expected to be reported from a server implementing the Merchant Backend API.



There is a [subclass](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-problem/index.md) for problems expected to be reported by a server implementing the Merchant Backend API.



IMPORTANT: Problem synchronizes on itself, so you should never synchronize on a Problem object yourself.

   


## Constructors  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/Problem/Problem/#com.google.gson.JsonObject/PointingToDeclaration/"></a>[Problem](-problem.md)| <a name="com.swedbankpay.mobilesdk/Problem/Problem/#com.google.gson.JsonObject/PointingToDeclaration/"></a> [androidJvm] fun [Problem](-problem.md)(jsonObject: )Interprets a Gson JsonObject as a Problem.   <br>|
| <a name="com.swedbankpay.mobilesdk/Problem/Problem/#kotlin.String/PointingToDeclaration/"></a>[Problem](-problem.md)| <a name="com.swedbankpay.mobilesdk/Problem/Problem/#kotlin.String/PointingToDeclaration/"></a> [androidJvm] fun [Problem](-problem.md)(raw: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))Parses a Problem from a String.   <br>|
| <a name="com.swedbankpay.mobilesdk/Problem/Problem/#android.os.Parcel/PointingToDeclaration/"></a>[Problem](-problem.md)| <a name="com.swedbankpay.mobilesdk/Problem/Problem/#android.os.Parcel/PointingToDeclaration/"></a> [androidJvm] fun [Problem](-problem.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html))   <br>|


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/Problem.Companion///PointingToDeclaration/"></a>[Companion](-companion/index.md)| <a name="com.swedbankpay.mobilesdk/Problem.Companion///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/Problem/describeContents/#/PointingToDeclaration/"></a>[describeContents](describe-contents.md)| <a name="com.swedbankpay.mobilesdk/Problem/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [describeContents](describe-contents.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](write-to-parcel.md)| <a name="com.swedbankpay.mobilesdk/Problem/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [writeToParcel](write-to-parcel.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/Problem/detail/#/PointingToDeclaration/"></a>[detail](detail.md)| <a name="com.swedbankpay.mobilesdk/Problem/detail/#/PointingToDeclaration/"></a> [androidJvm] val [detail](detail.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a detailed explanation of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk/Problem/instance/#/PointingToDeclaration/"></a>[instance](instance.md)| <a name="com.swedbankpay.mobilesdk/Problem/instance/#/PointingToDeclaration/"></a> [androidJvm] val [instance](instance.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a URI reference that identifies the specific occurrence of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk/Problem/jsonObject/#/PointingToDeclaration/"></a>[jsonObject](json-object.md)| <a name="com.swedbankpay.mobilesdk/Problem/jsonObject/#/PointingToDeclaration/"></a> [androidJvm] val [jsonObject](json-object.md): The raw RFC 7807 object parsed as a Gson JsonObject.   <br>|
| <a name="com.swedbankpay.mobilesdk/Problem/raw/#/PointingToDeclaration/"></a>[raw](raw.md)| <a name="com.swedbankpay.mobilesdk/Problem/raw/#/PointingToDeclaration/"></a> [androidJvm] val [raw](raw.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The raw RFC 7807 object.   <br>|
| <a name="com.swedbankpay.mobilesdk/Problem/status/#/PointingToDeclaration/"></a>[status](status.md)| <a name="com.swedbankpay.mobilesdk/Problem/status/#/PointingToDeclaration/"></a> [androidJvm] val [status](status.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?RFC 7807 default property: the HTTP status codeThis should always be the same as the actual HTTP status code reported by the server.   <br>|
| <a name="com.swedbankpay.mobilesdk/Problem/title/#/PointingToDeclaration/"></a>[title](title.md)| <a name="com.swedbankpay.mobilesdk/Problem/title/#/PointingToDeclaration/"></a> [androidJvm] val [title](title.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a short summary of the problem.   <br>|
| <a name="com.swedbankpay.mobilesdk/Problem/type/#/PointingToDeclaration/"></a>[type](type.md)| <a name="com.swedbankpay.mobilesdk/Problem/type/#/PointingToDeclaration/"></a> [androidJvm] val [type](type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)RFC 7807 default property: a URI reference that identifies the problem type.   <br>|


## Inheritors  
  
|  Name | 
|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem///PointingToDeclaration/"></a>[MerchantBackendProblem](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-problem/index.md)|

