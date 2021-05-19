//[sdk](../../../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../../../index.md)/[MerchantBackendProblem](../../index.md)/[Client](../index.md)/[MobileSDK](index.md)



# MobileSDK  
 [androidJvm] sealed class [MobileSDK](index.md) : [MerchantBackendProblem.Client](../index.md)

Base class for [Client](../index.md) Problems defined by the example backend.

   


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK.InvalidRequest///PointingToDeclaration/"></a>[InvalidRequest](-invalid-request/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK.InvalidRequest///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [InvalidRequest](-invalid-request/index.md) : [MerchantBackendProblem.Client.MobileSDK](index.md)  <br>More info  <br>The merchant backend did not understand the request.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK.Unauthorized///PointingToDeclaration/"></a>[Unauthorized](-unauthorized/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK.Unauthorized///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [Unauthorized](-unauthorized/index.md) : [MerchantBackendProblem.Client.MobileSDK](index.md)  <br>More info  <br>The merchant backend rejected the request because its authentication headers were invalid.  <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[describeContents](../../-server/-unknown/index.md#-1578325224%2FFunctions%2F462465411)| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>abstract fun [describeContents](../../-server/-unknown/index.md#-1578325224%2FFunctions%2F462465411)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../../../com.swedbankpay.mobilesdk/-problem/equals.md)| <a name="com.swedbankpay.mobilesdk/Problem/equals/#kotlin.Any?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open operator override fun [equals](../../../../com.swedbankpay.mobilesdk/-problem/equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../../../com.swedbankpay.mobilesdk/-problem/hash-code.md)| <a name="com.swedbankpay.mobilesdk/Problem/hashCode/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [hashCode](../../../../com.swedbankpay.mobilesdk/-problem/hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/toString/#/PointingToDeclaration/"></a>[toString](../../../../com.swedbankpay.mobilesdk/-problem/to-string.md)| <a name="com.swedbankpay.mobilesdk/Problem/toString/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [toString](../../../../com.swedbankpay.mobilesdk/-problem/to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](../../write-to-parcel.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [writeToParcel](../../write-to-parcel.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/detail/#/PointingToDeclaration/"></a>[detail](index.md#-96238654%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/detail/#/PointingToDeclaration/"></a> [androidJvm] val [detail](index.md#-96238654%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a detailed explanation of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/instance/#/PointingToDeclaration/"></a>[instance](index.md#1104586366%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/instance/#/PointingToDeclaration/"></a> [androidJvm] val [instance](index.md#1104586366%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a URI reference that identifies the specific occurrence of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/jsonObject/#/PointingToDeclaration/"></a>[jsonObject](index.md#1336173452%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/jsonObject/#/PointingToDeclaration/"></a> [androidJvm] val [jsonObject](index.md#1336173452%2FProperties%2F462465411): JsonObjectThe raw RFC 7807 object parsed as a Gson JsonObject.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/raw/#/PointingToDeclaration/"></a>[raw](index.md#-653854433%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/raw/#/PointingToDeclaration/"></a> [androidJvm] val [raw](index.md#-653854433%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The raw RFC 7807 object.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/status/#/PointingToDeclaration/"></a>[status](index.md#-916917439%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/status/#/PointingToDeclaration/"></a> [androidJvm] val [status](index.md#-916917439%2FProperties%2F462465411): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?RFC 7807 default property: the HTTP status codeThis should always be the same as the actual HTTP status code reported by the server.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/title/#/PointingToDeclaration/"></a>[title](index.md#752708207%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/title/#/PointingToDeclaration/"></a> [androidJvm] val [title](index.md#752708207%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a short summary of the problem.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/type/#/PointingToDeclaration/"></a>[type](index.md#-531510663%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK/type/#/PointingToDeclaration/"></a> [androidJvm] val [type](index.md#-531510663%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)RFC 7807 default property: a URI reference that identifies the problem type.   <br>|


## Inheritors  
  
|  Name | 
|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK.Unauthorized///PointingToDeclaration/"></a>[MerchantBackendProblem.Client.MobileSDK](-unauthorized/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK.InvalidRequest///PointingToDeclaration/"></a>[MerchantBackendProblem.Client.MobileSDK](-invalid-request/index.md)|

