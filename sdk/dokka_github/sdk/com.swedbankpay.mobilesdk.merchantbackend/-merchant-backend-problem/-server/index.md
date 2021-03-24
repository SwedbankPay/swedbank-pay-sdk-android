//[sdk](../../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../../index.md)/[MerchantBackendProblem](../index.md)/[Server](index.md)



# Server  
 [androidJvm] sealed class [Server](index.md) : [MerchantBackendProblem](../index.md)

Base class for [Problems](../index.md) caused by the service backend.



Any unexpected response where the HTTP status is outside 400-499 results in a Server Problem; usually it means the status was in 500-599.

   


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.Companion///PointingToDeclaration/"></a>[Companion](-companion/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.Companion///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK///PointingToDeclaration/"></a>[MobileSDK](-mobile-s-d-k/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>sealed class [MobileSDK](-mobile-s-d-k/index.md) : [MerchantBackendProblem.Server](index.md)  <br>More info  <br>Base class for [Server](index.md) Problems defined by the example backend.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay///PointingToDeclaration/"></a>[SwedbankPay](-swedbank-pay/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>sealed class [SwedbankPay](-swedbank-pay/index.md) : [MerchantBackendProblem.Server](index.md), [SwedbankPayProblem](../../-swedbank-pay-problem/index.md)  <br>More info  <br>Base class for [Server](index.md) problems defined by the Swedbank Pay backend.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.Unknown///PointingToDeclaration/"></a>[Unknown](-unknown/index.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.Unknown///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [Unknown](-unknown/index.md)(**jsonObject**: ) : [MerchantBackendProblem.Server](index.md)  <br>More info  <br>[Server](index.md) problem with an unrecognized type.  <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/Problem/describeContents/#/PointingToDeclaration/"></a>[describeContents](../../../com.swedbankpay.mobilesdk/-problem/describe-contents.md)| <a name="com.swedbankpay.mobilesdk/Problem/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [describeContents](../../../com.swedbankpay.mobilesdk/-problem/describe-contents.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](../write-to-parcel.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [writeToParcel](../write-to-parcel.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/detail/#/PointingToDeclaration/"></a>[detail](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2Fdetail%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/detail/#/PointingToDeclaration/"></a> [androidJvm] val [detail](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2Fdetail%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a detailed explanation of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/instance/#/PointingToDeclaration/"></a>[instance](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2Finstance%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/instance/#/PointingToDeclaration/"></a> [androidJvm] val [instance](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2Finstance%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a URI reference that identifies the specific occurrence of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/jsonObject/#/PointingToDeclaration/"></a>[jsonObject](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2FjsonObject%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/jsonObject/#/PointingToDeclaration/"></a> [androidJvm] val [jsonObject](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2FjsonObject%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000): The raw RFC 7807 object parsed as a Gson JsonObject.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/raw/#/PointingToDeclaration/"></a>[raw](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2Fraw%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/raw/#/PointingToDeclaration/"></a> [androidJvm] val [raw](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2Fraw%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The raw RFC 7807 object.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/status/#/PointingToDeclaration/"></a>[status](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2Fstatus%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/status/#/PointingToDeclaration/"></a> [androidJvm] val [status](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2Fstatus%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?RFC 7807 default property: the HTTP status codeThis should always be the same as the actual HTTP status code reported by the server.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/title/#/PointingToDeclaration/"></a>[title](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2Ftitle%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/title/#/PointingToDeclaration/"></a> [androidJvm] val [title](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2Ftitle%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?RFC 7807 default property: a short summary of the problem.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/type/#/PointingToDeclaration/"></a>[type](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2Ftype%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server/type/#/PointingToDeclaration/"></a> [androidJvm] val [type](index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FMerchantBackendProblem.Server%2Ftype%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)RFC 7807 default property: a URI reference that identifies the problem type.   <br>|


## Inheritors  
  
|  Name | 
|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.MobileSDK///PointingToDeclaration/"></a>[MerchantBackendProblem.Server](-mobile-s-d-k/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay///PointingToDeclaration/"></a>[MerchantBackendProblem.Server](-swedbank-pay/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.Unknown///PointingToDeclaration/"></a>[MerchantBackendProblem.Server](-unknown/index.md)|

