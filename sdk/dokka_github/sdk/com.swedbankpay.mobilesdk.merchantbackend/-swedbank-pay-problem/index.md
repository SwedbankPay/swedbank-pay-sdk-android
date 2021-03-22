//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../index.md)/[SwedbankPayProblem](index.md)



# SwedbankPayProblem  
 [androidJvm] interface [SwedbankPayProblem](index.md)

A Problem defined by the Swedbank Pay backend. https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems

   


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/action/#/PointingToDeclaration/"></a>[action](action.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/action/#/PointingToDeclaration/"></a> [androidJvm] abstract val [action](action.md): [SwedbankPayAction](../index.md#%5Bcom.swedbankpay.mobilesdk.merchantbackend%2FSwedbankPayAction%2F%2F%2FPointingToDeclaration%2F%5D%2FClasslikes%2F2101262426)?Suggested action to take to recover from the error.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/detail/#/PointingToDeclaration/"></a>[detail](detail.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/detail/#/PointingToDeclaration/"></a> [androidJvm] abstract val [detail](detail.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?Human-readable details about the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/instance/#/PointingToDeclaration/"></a>[instance](instance.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/instance/#/PointingToDeclaration/"></a> [androidJvm] abstract val [instance](instance.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?Swedbank Pay internal identifier of the problem.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/problems/#/PointingToDeclaration/"></a>[problems](problems.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/problems/#/PointingToDeclaration/"></a> [androidJvm] abstract val [problems](problems.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[SwedbankPaySubproblem](../-swedbank-pay-subproblem/index.md)>Array of problem detail objects   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/raw/#/PointingToDeclaration/"></a>[raw](raw.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/raw/#/PointingToDeclaration/"></a> [androidJvm] abstract val [raw](raw.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The raw application/problem+json object.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/status/#/PointingToDeclaration/"></a>[status](status.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/status/#/PointingToDeclaration/"></a> [androidJvm] abstract val [status](status.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?The HTTP status.   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/title/#/PointingToDeclaration/"></a>[title](title.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/title/#/PointingToDeclaration/"></a> [androidJvm] abstract val [title](title.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?Human-readable description of the problem   <br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/type/#/PointingToDeclaration/"></a>[type](type.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/SwedbankPayProblem/type/#/PointingToDeclaration/"></a> [androidJvm] abstract val [type](type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)RFC 7807 default property: a URI reference that identifies the problem type.   <br>|


## Inheritors  
  
|  Name | 
|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Client.SwedbankPay///PointingToDeclaration/"></a>[MerchantBackendProblem.Client](../-merchant-backend-problem/-client/-swedbank-pay/index.md)|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendProblem.Server.SwedbankPay///PointingToDeclaration/"></a>[MerchantBackendProblem.Server](../-merchant-backend-problem/-server/-swedbank-pay/index.md)|

