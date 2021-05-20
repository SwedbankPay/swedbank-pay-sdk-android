//[sdk](../../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../../index.md)/[MerchantBackendProblem](../index.md)/[Client](index.md)



# Client  
 [androidJvm] sealed class [Client](index.md) : [MerchantBackendProblem](../index.md)

Base class for [Problems](../index.md) caused by the service refusing or not understanding a request sent to it by the client.



A Client Problem always implies a HTTP status in 400-499.

   


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.Companion///PointingToDeclaration/"></a>[Companion](-companion/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.Companion///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK///PointingToDeclaration/"></a>[MobileSDK](-mobile-s-d-k/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>sealed class [MobileSDK](-mobile-s-d-k/index.md) : [MerchantBackendProblem.Client](index.md)  <br>More info  <br>Base class for [Client](index.md) Problems defined by the example backend.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay///PointingToDeclaration/"></a>[SwedbankPay](-swedbank-pay/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>sealed class [SwedbankPay](-swedbank-pay/index.md) : [MerchantBackendProblem.Client](index.md), [SwedbankPayProblem](../../-swedbank-pay-problem/index.md)  <br>More info  <br>Base class for [Client](index.md) problems defined by the Swedbank Pay backend.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.Unknown///PointingToDeclaration/"></a>[Unknown](-unknown/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.Unknown///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [Unknown](-unknown/index.md)(**jsonObject**: JsonObject) : [MerchantBackendProblem.Client](index.md)  <br>More info  <br>[Client](index.md) problem with an unrecognized type.  <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[describeContents](../-server/-unknown/index.md#-1578325224%2FFunctions%2F462465411)| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>abstract fun [describeContents](../-server/-unknown/index.md#-1578325224%2FFunctions%2F462465411)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../../com.swedbankpay.mobilesdk/-problem/equals.md)| <a name="com.swedbankpay.mobilesdk/Problem/equals/#kotlin.Any?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open operator override fun [equals](../../../com.swedbankpay.mobilesdk/-problem/equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../../com.swedbankpay.mobilesdk/-problem/hash-code.md)| <a name="com.swedbankpay.mobilesdk/Problem/hashCode/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [hashCode](../../../com.swedbankpay.mobilesdk/-problem/hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Problem/toString/#/PointingToDeclaration/"></a>[toString](../../../com.swedbankpay.mobilesdk/-problem/to-string.md)| <a name="com.swedbankpay.mobilesdk/Problem/toString/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [toString](../../../com.swedbankpay.mobilesdk/-problem/to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](../write-to-parcel.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [writeToParcel](../write-to-parcel.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/detail/#/PointingToDeclaration/"></a>[detail](index.md#2050169304%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/detail/#/PointingToDeclaration/"></a> [androidJvm] val [detail](index.md#2050169304%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a detailed explanation of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/instance/#/PointingToDeclaration/"></a>[instance](index.md#-2076635372%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/instance/#/PointingToDeclaration/"></a> [androidJvm] val [instance](index.md#-2076635372%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a URI reference that identifies the specific occurrence of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/jsonObject/#/PointingToDeclaration/"></a>[jsonObject](index.md#-2096169310%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/jsonObject/#/PointingToDeclaration/"></a> [androidJvm] val [jsonObject](index.md#-2096169310%2FProperties%2F462465411): JsonObjectThe raw RFC 7807 object parsed as a Gson JsonObject.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/raw/#/PointingToDeclaration/"></a>[raw](index.md#-1440806199%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/raw/#/PointingToDeclaration/"></a> [androidJvm] val [raw](index.md#-1440806199%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The raw RFC 7807 object.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/status/#/PointingToDeclaration/"></a>[status](index.md#1229490519%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/status/#/PointingToDeclaration/"></a> [androidJvm] val [status](index.md#1229490519%2FProperties%2F462465411): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?RFC 7807 default property: the HTTP status codeThis should always be the same as the actual HTTP status code reported by the server.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/title/#/PointingToDeclaration/"></a>[title](index.md#406305177%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/title/#/PointingToDeclaration/"></a> [androidJvm] val [title](index.md#406305177%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a short summary of the problem.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/type/#/PointingToDeclaration/"></a>[type](index.md#842788367%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client/type/#/PointingToDeclaration/"></a> [androidJvm] val [type](index.md#842788367%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)RFC 7807 default property: a URI reference that identifies the problem type.   <br>|


## Inheritors  
  
|  Name | 
|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.MobileSDK///PointingToDeclaration/"></a>[MerchantBackendProblem.Client](-mobile-s-d-k/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay///PointingToDeclaration/"></a>[MerchantBackendProblem.Client](-swedbank-pay/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.Unknown///PointingToDeclaration/"></a>[MerchantBackendProblem.Client](-unknown/index.md)|

