//[sdk](../../../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../../../index.md)/[MerchantBackendProblem](../../index.md)/[Client](../index.md)/[SwedbankPay](index.md)



# SwedbankPay  
 [androidJvm] sealed class [SwedbankPay](index.md) : [MerchantBackendProblem.Client](../index.md), [SwedbankPayProblem](../../../-swedbank-pay-problem/index.md)

Base class for [Client](../index.md) problems defined by the Swedbank Pay backend. https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems

   


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay.Forbidden///PointingToDeclaration/"></a>[Forbidden](-forbidden/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay.Forbidden///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [Forbidden](-forbidden/index.md)(**jsonObject**: JsonObject) : [MerchantBackendProblem.Client.SwedbankPay](index.md)  <br>More info  <br>The request was understood, but the service is refusing to fulfill it.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay.InputError///PointingToDeclaration/"></a>[InputError](-input-error/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay.InputError///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [InputError](-input-error/index.md)(**jsonObject**: JsonObject) : [MerchantBackendProblem.Client.SwedbankPay](index.md)  <br>More info  <br>The request could not be handled because the request was malformed somehow (e.g.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay.NotFound///PointingToDeclaration/"></a>[NotFound](-not-found/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay.NotFound///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [NotFound](-not-found/index.md)(**jsonObject**: JsonObject) : [MerchantBackendProblem.Client.SwedbankPay](index.md)  <br>More info  <br>The requested resource was not found.  <br><br><br>|


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
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/action/#/PointingToDeclaration/"></a>[action](action.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/action/#/PointingToDeclaration/"></a> [androidJvm] open override val [action](action.md): [SwedbankPayAction](../../../index.md#853214653%2FClasslikes%2F462465411)?Suggested action to take to recover from the error.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/detail/#/PointingToDeclaration/"></a>[detail](index.md#2068253107%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/detail/#/PointingToDeclaration/"></a> [androidJvm] val [detail](index.md#2068253107%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a detailed explanation of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/instance/#/PointingToDeclaration/"></a>[instance](index.md#-1877969873%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/instance/#/PointingToDeclaration/"></a> [androidJvm] val [instance](index.md#-1877969873%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a URI reference that identifies the specific occurrence of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/jsonObject/#/PointingToDeclaration/"></a>[jsonObject](index.md#-157185795%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/jsonObject/#/PointingToDeclaration/"></a> [androidJvm] val [jsonObject](index.md#-157185795%2FProperties%2F462465411): JsonObjectThe raw RFC 7807 object parsed as a Gson JsonObject.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/problems/#/PointingToDeclaration/"></a>[problems](problems.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/problems/#/PointingToDeclaration/"></a> [androidJvm] open override val [problems](problems.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[SwedbankPaySubproblem](../../../-swedbank-pay-subproblem/index.md)>Array of problem detail objects   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/raw/#/PointingToDeclaration/"></a>[raw](index.md#-394420018%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/raw/#/PointingToDeclaration/"></a> [androidJvm] val [raw](index.md#-394420018%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The raw RFC 7807 object.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/status/#/PointingToDeclaration/"></a>[status](index.md#1247574322%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/status/#/PointingToDeclaration/"></a> [androidJvm] val [status](index.md#1247574322%2FProperties%2F462465411): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?RFC 7807 default property: the HTTP status codeThis should always be the same as the actual HTTP status code reported by the server.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/title/#/PointingToDeclaration/"></a>[title](index.md#961077854%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/title/#/PointingToDeclaration/"></a> [androidJvm] val [title](index.md#961077854%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a short summary of the problem.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/type/#/PointingToDeclaration/"></a>[type](index.md#-1078978390%2FProperties%2F462465411)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay/type/#/PointingToDeclaration/"></a> [androidJvm] val [type](index.md#-1078978390%2FProperties%2F462465411): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)RFC 7807 default property: a URI reference that identifies the problem type.   <br>|


## Inheritors  
  
|  Name | 
|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay.InputError///PointingToDeclaration/"></a>[MerchantBackendProblem.Client.SwedbankPay](-input-error/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay.Forbidden///PointingToDeclaration/"></a>[MerchantBackendProblem.Client.SwedbankPay](-forbidden/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay.NotFound///PointingToDeclaration/"></a>[MerchantBackendProblem.Client.SwedbankPay](-not-found/index.md)|

