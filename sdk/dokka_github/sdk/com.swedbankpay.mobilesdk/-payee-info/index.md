//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[PayeeInfo](index.md)



# PayeeInfo  
 [androidJvm] data class [PayeeInfo](index.md)(**payeeId**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **payeeReference**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **payeeName**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **productCategory**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **orderReference**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **subsite**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

Information about the payee (recipient) of a payment order

   


## Constructors  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/PayeeInfo/PayeeInfo/#kotlin.String#kotlin.String#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?/PointingToDeclaration/"></a>[PayeeInfo](-payee-info.md)| <a name="com.swedbankpay.mobilesdk/PayeeInfo/PayeeInfo/#kotlin.String#kotlin.String#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?/PointingToDeclaration/"></a> [androidJvm] fun [PayeeInfo](-payee-info.md)(payeeId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = "", payeeReference: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = "", payeeName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, productCategory: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, orderReference: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, subsite: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null)   <br>|


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PayeeInfo.Builder///PointingToDeclaration/"></a>[Builder](-builder/index.md)| <a name="com.swedbankpay.mobilesdk/PayeeInfo.Builder///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [Builder](-builder/index.md)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PayeeInfo.Companion///PointingToDeclaration/"></a>[Companion](-companion/index.md)| <a name="com.swedbankpay.mobilesdk/PayeeInfo.Companion///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PayeeInfo/describeContents/#/PointingToDeclaration/"></a>[describeContents](describe-contents.md)| <a name="com.swedbankpay.mobilesdk/PayeeInfo/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [describeContents](describe-contents.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PayeeInfo/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](write-to-parcel.md)| <a name="com.swedbankpay.mobilesdk/PayeeInfo/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [writeToParcel](write-to-parcel.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PayeeInfo/orderReference/#/PointingToDeclaration/"></a>[orderReference](order-reference.md)| <a name="com.swedbankpay.mobilesdk/PayeeInfo/orderReference/#/PointingToDeclaration/"></a> [androidJvm] @(value = orderReference)  <br>  <br>val [orderReference](order-reference.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullA reference to your own merchant system.   <br>|
| <a name="com.swedbankpay.mobilesdk/PayeeInfo/payeeId/#/PointingToDeclaration/"></a>[payeeId](payee-id.md)| <a name="com.swedbankpay.mobilesdk/PayeeInfo/payeeId/#/PointingToDeclaration/"></a> [androidJvm] @(value = payeeId)  <br>  <br>val [payeeId](payee-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The unique identifier of this payee set by Swedbank Pay.   <br>|
| <a name="com.swedbankpay.mobilesdk/PayeeInfo/payeeName/#/PointingToDeclaration/"></a>[payeeName](payee-name.md)| <a name="com.swedbankpay.mobilesdk/PayeeInfo/payeeName/#/PointingToDeclaration/"></a> [androidJvm] @(value = payeeName)  <br>  <br>val [payeeName](payee-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullName of the payee, usually the name of the merchant.   <br>|
| <a name="com.swedbankpay.mobilesdk/PayeeInfo/payeeReference/#/PointingToDeclaration/"></a>[payeeReference](payee-reference.md)| <a name="com.swedbankpay.mobilesdk/PayeeInfo/payeeReference/#/PointingToDeclaration/"></a> [androidJvm] @(value = payeeReference)  <br>  <br>val [payeeReference](payee-reference.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)[A unique reference for this operation](https://developer.swedbankpay.com/checkout/other-features#payee-reference).   <br>|
| <a name="com.swedbankpay.mobilesdk/PayeeInfo/productCategory/#/PointingToDeclaration/"></a>[productCategory](product-category.md)| <a name="com.swedbankpay.mobilesdk/PayeeInfo/productCategory/#/PointingToDeclaration/"></a> [androidJvm] @(value = productCategory)  <br>  <br>val [productCategory](product-category.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullA product category or number sent in from the payee/merchant.   <br>|
| <a name="com.swedbankpay.mobilesdk/PayeeInfo/subsite/#/PointingToDeclaration/"></a>[subsite](subsite.md)| <a name="com.swedbankpay.mobilesdk/PayeeInfo/subsite/#/PointingToDeclaration/"></a> [androidJvm] @(value = subsite)  <br>  <br>val [subsite](subsite.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullUsed for split settlement.   <br>|

