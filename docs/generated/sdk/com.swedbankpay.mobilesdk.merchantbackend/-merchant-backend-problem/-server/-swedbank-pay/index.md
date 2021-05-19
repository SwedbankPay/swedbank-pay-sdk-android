//[sdk](../../../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../../../index.md)/[MerchantBackendProblem](../../index.md)/[Server](../index.md)/[SwedbankPay](index.md)



# SwedbankPay  
 [androidJvm] sealed class [SwedbankPay](index.md) : [MerchantBackendProblem.Server](../index.md), [SwedbankPayProblem](../../../-swedbank-pay-problem/index.md)

Base class for [Server](../index.md) problems defined by the Swedbank Pay backend. https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems

   


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay.ConfigurationError///PointingToDeclaration/"></a>[ConfigurationError](-configuration-error/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay.ConfigurationError///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [ConfigurationError](-configuration-error/index.md)(**jsonObject**: JsonObject) : [MerchantBackendProblem.Server.SwedbankPay](index.md)  <br>More info  <br>There is a problem with your merchant configuration.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay.SystemError///PointingToDeclaration/"></a>[SystemError](-system-error/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay.SystemError///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [SystemError](-system-error/index.md)(**jsonObject**: JsonObject) : [MerchantBackendProblem.Server.SwedbankPay](index.md)  <br>More info  <br>General internal error in Swedbank Pay systems.  <br><br><br>|


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
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/action/#/PointingToDeclaration/"></a>[action](action.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/action/#/PointingToDeclaration/"></a> [androidJvm] open override val [action](action.md): [SwedbankPayAction](../../../index.md#853214653%2FClasslikes%2F462465411)?Suggested action to take to recover from the error.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/detail/#/PointingToDeclaration/"></a>[detail](index.md#-1276841685%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/detail/#/PointingToDeclaration/"></a> [androidJvm] val [detail](index.md#-1276841685%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a detailed explanation of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/instance/#/PointingToDeclaration/"></a>[instance](index.md#416439719%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/instance/#/PointingToDeclaration/"></a> [androidJvm] val [instance](index.md#416439719%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a URI reference that identifies the specific occurrence of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/jsonObject/#/PointingToDeclaration/"></a>[jsonObject](index.md#1452209269%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/jsonObject/#/PointingToDeclaration/"></a> [androidJvm] val [jsonObject](index.md#1452209269%2FProperties%2F462465411): JsonObjectThe raw RFC 7807 object parsed as a Gson JsonObject.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/problems/#/PointingToDeclaration/"></a>[problems](problems.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/problems/#/PointingToDeclaration/"></a> [androidJvm] open override val [problems](problems.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[SwedbankPaySubproblem](../../../-swedbank-pay-subproblem/index.md)>Array of problem detail objects   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/raw/#/PointingToDeclaration/"></a>[raw](index.md#1897770070%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/raw/#/PointingToDeclaration/"></a> [androidJvm] val [raw](index.md#1897770070%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The raw RFC 7807 object.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/status/#/PointingToDeclaration/"></a>[status](index.md#-2097520470%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/status/#/PointingToDeclaration/"></a> [androidJvm] val [status](index.md#-2097520470%2FProperties%2F462465411): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?RFC 7807 default property: the HTTP status codeThis should always be the same as the actual HTTP status code reported by the server.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/title/#/PointingToDeclaration/"></a>[title](index.md#437529574%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/title/#/PointingToDeclaration/"></a> [androidJvm] val [title](index.md#437529574%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a short summary of the problem.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/type/#/PointingToDeclaration/"></a>[type](index.md#1259437602%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay/type/#/PointingToDeclaration/"></a> [androidJvm] val [type](index.md#1259437602%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)RFC 7807 default property: a URI reference that identifies the problem type.   <br>|


## Inheritors  
  
|  Name | 
|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay.SystemError///PointingToDeclaration/"></a>[MerchantBackendProblem.Server.SwedbankPay](-system-error/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay.ConfigurationError///PointingToDeclaration/"></a>[MerchantBackendProblem.Server.SwedbankPay](-configuration-error/index.md)|

