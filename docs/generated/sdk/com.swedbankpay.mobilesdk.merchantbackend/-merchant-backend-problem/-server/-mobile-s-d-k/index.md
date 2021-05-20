//[sdk](../../../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../../../index.md)/[MerchantBackendProblem](../../index.md)/[Server](../index.md)/[MobileSDK](index.md)



# MobileSDK  
 [androidJvm] sealed class [MobileSDK](index.md) : [MerchantBackendProblem.Server](../index.md)

Base class for [Server](../index.md) Problems defined by the example backend.

   


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK.BackendConnectionFailure///PointingToDeclaration/"></a>[BackendConnectionFailure](-backend-connection-failure/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK.BackendConnectionFailure///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [BackendConnectionFailure](-backend-connection-failure/index.md)(**jsonObject**: JsonObject) : [MerchantBackendProblem.Server.MobileSDK](index.md)  <br>More info  <br>The merchant backend failed to connect to the Swedbank Pay backend.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK.BackendConnectionTimeout///PointingToDeclaration/"></a>[BackendConnectionTimeout](-backend-connection-timeout/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK.BackendConnectionTimeout///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [BackendConnectionTimeout](-backend-connection-timeout/index.md)(**jsonObject**: JsonObject) : [MerchantBackendProblem.Server.MobileSDK](index.md)  <br>More info  <br>The merchant backend timed out trying to connect to the Swedbank Pay backend.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK.InvalidBackendResponse///PointingToDeclaration/"></a>[InvalidBackendResponse](-invalid-backend-response/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK.InvalidBackendResponse///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [InvalidBackendResponse](-invalid-backend-response/index.md)(**jsonObject**: JsonObject, **backendStatus**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [MerchantBackendProblem.Server.MobileSDK](index.md)  <br>More info  <br>The merchant backend received an invalid response from the Swedbank Pay backend.  <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[describeContents](../-unknown/index.md#-1578325224%2FFunctions%2F462465411)| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>abstract fun [describeContents](../-unknown/index.md#-1578325224%2FFunctions%2F462465411)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../../../com.swedbankpay.mobilesdk/-problem/equals.md)| <a name="com.swedbankpay.mobilesdk/Problem/equals/#kotlin.Any?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open operator override fun [equals](../../../../com.swedbankpay.mobilesdk/-problem/equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../../../com.swedbankpay.mobilesdk/-problem/hash-code.md)| <a name="com.swedbankpay.mobilesdk/Problem/hashCode/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [hashCode](../../../../com.swedbankpay.mobilesdk/-problem/hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/toString/#/PointingToDeclaration/"></a>[toString](../../../../com.swedbankpay.mobilesdk/-problem/to-string.md)| <a name="com.swedbankpay.mobilesdk/Problem/toString/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [toString](../../../../com.swedbankpay.mobilesdk/-problem/to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](../../write-to-parcel.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [writeToParcel](../../write-to-parcel.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/detail/#/PointingToDeclaration/"></a>[detail](index.md#-2052789958%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/detail/#/PointingToDeclaration/"></a> [androidJvm] val [detail](index.md#-2052789958%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a detailed explanation of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/instance/#/PointingToDeclaration/"></a>[instance](index.md#2054458870%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/instance/#/PointingToDeclaration/"></a> [androidJvm] val [instance](index.md#2054458870%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a URI reference that identifies the specific occurrence of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/jsonObject/#/PointingToDeclaration/"></a>[jsonObject](index.md#-664384252%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/jsonObject/#/PointingToDeclaration/"></a> [androidJvm] val [jsonObject](index.md#-664384252%2FProperties%2F462465411): JsonObjectThe raw RFC 7807 object parsed as a Gson JsonObject.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/raw/#/PointingToDeclaration/"></a>[raw](index.md#465847975%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/raw/#/PointingToDeclaration/"></a> [androidJvm] val [raw](index.md#465847975%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The raw RFC 7807 object.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/status/#/PointingToDeclaration/"></a>[status](index.md#1421498553%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/status/#/PointingToDeclaration/"></a> [androidJvm] val [status](index.md#1421498553%2FProperties%2F462465411): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?RFC 7807 default property: the HTTP status codeThis should always be the same as the actual HTTP status code reported by the server.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/title/#/PointingToDeclaration/"></a>[title](index.md#-1250069001%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/title/#/PointingToDeclaration/"></a> [androidJvm] val [title](index.md#-1250069001%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a short summary of the problem.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/type/#/PointingToDeclaration/"></a>[type](index.md#-180474383%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK/type/#/PointingToDeclaration/"></a> [androidJvm] val [type](index.md#-180474383%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)RFC 7807 default property: a URI reference that identifies the problem type.   <br>|


## Inheritors  
  
|  Name | 
|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK.BackendConnectionTimeout///PointingToDeclaration/"></a>[MerchantBackendProblem.Server.MobileSDK](-backend-connection-timeout/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK.BackendConnectionFailure///PointingToDeclaration/"></a>[MerchantBackendProblem.Server.MobileSDK](-backend-connection-failure/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK.InvalidBackendResponse///PointingToDeclaration/"></a>[MerchantBackendProblem.Server.MobileSDK](-invalid-backend-response/index.md)|

