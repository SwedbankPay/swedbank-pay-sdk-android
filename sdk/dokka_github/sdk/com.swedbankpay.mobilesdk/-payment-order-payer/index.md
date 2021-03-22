//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[PaymentOrderPayer](index.md)



# PaymentOrderPayer  
 [androidJvm] data class [PaymentOrderPayer](index.md)(**consumerProfileRef**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **email**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **msisdn**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **payerReference**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

Information about the payer of a payment order

   


## Constructors  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/PaymentOrderPayer/#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?/PointingToDeclaration/"></a>[PaymentOrderPayer](-payment-order-payer.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/PaymentOrderPayer/#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?/PointingToDeclaration/"></a> [androidJvm] fun [PaymentOrderPayer](-payment-order-payer.md)(consumerProfileRef: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, email: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, msisdn: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, payerReference: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null)   <br>|


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer.Builder///PointingToDeclaration/"></a>[Builder](-builder/index.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer.Builder///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [Builder](-builder/index.md)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer.Companion///PointingToDeclaration/"></a>[Companion](-companion/index.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer.Companion///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/describeContents/#/PointingToDeclaration/"></a>[describeContents](describe-contents.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [describeContents](describe-contents.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](write-to-parcel.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [writeToParcel](write-to-parcel.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/consumerProfileRef/#/PointingToDeclaration/"></a>[consumerProfileRef](consumer-profile-ref.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/consumerProfileRef/#/PointingToDeclaration/"></a> [androidJvm] @(value = consumerProfileRef)  <br>  <br>val [consumerProfileRef](consumer-profile-ref.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullA consumer profile reference obtained through the Checkin flow.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/email/#/PointingToDeclaration/"></a>[email](email.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/email/#/PointingToDeclaration/"></a> [androidJvm] @(value = email)  <br>  <br>val [email](email.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullThe email address of the payer.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/msisdn/#/PointingToDeclaration/"></a>[msisdn](msisdn.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/msisdn/#/PointingToDeclaration/"></a> [androidJvm] @(value = msisdn)  <br>  <br>val [msisdn](msisdn.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullThe phone number of the payer.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/payerReference/#/PointingToDeclaration/"></a>[payerReference](payer-reference.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderPayer/payerReference/#/PointingToDeclaration/"></a> [androidJvm] @(value = payerReference)  <br>  <br>val [payerReference](payer-reference.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullAn opaque, unique reference to the payer.   <br>|

