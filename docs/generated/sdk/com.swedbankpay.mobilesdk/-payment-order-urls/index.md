//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[PaymentOrderUrls](index.md)



# PaymentOrderUrls  
 [androidJvm] data class [PaymentOrderUrls](index.md)(**hostUrls**: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>, **completeUrl**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **cancelUrl**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **paymentUrl**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **callbackUrl**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **termsOfServiceUrl**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

A set of URLs relevant to a payment order.



The Mobile SDK places some requirements on these URLs,  different to the web-page case. See individual properties for discussion.

   


## Constructors  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/PaymentOrderUrls/#android.content.Context#kotlin.String#kotlin.String?#kotlin.String?#kotlin.String/PointingToDeclaration/"></a>[PaymentOrderUrls](-payment-order-urls.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/PaymentOrderUrls/#android.content.Context#kotlin.String#kotlin.String?#kotlin.String?#kotlin.String/PointingToDeclaration/"></a> [androidJvm] @[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  <br>  <br>fun [PaymentOrderUrls](-payment-order-urls.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), backendUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), callbackUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, termsOfServiceUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = UUID.randomUUID().toString())Creates a set of URLs suitable for use with a Merchant Backend server.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/PaymentOrderUrls/#android.content.Context#kotlin.String#kotlin.String#kotlin.String?#kotlin.String?#kotlin.String/PointingToDeclaration/"></a>[PaymentOrderUrls](-payment-order-urls.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/PaymentOrderUrls/#android.content.Context#kotlin.String#kotlin.String#kotlin.String?#kotlin.String?#kotlin.String/PointingToDeclaration/"></a> [androidJvm] fun [PaymentOrderUrls](-payment-order-urls.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), hostUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), backendUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), callbackUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, termsOfServiceUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = UUID.randomUUID().toString())Creates a set of URLs suitable for use with a Merchant Backend server.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/PaymentOrderUrls/#kotlin.collections.List[kotlin.String]#kotlin.String#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?/PointingToDeclaration/"></a>[PaymentOrderUrls](-payment-order-urls.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/PaymentOrderUrls/#kotlin.collections.List[kotlin.String]#kotlin.String#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?/PointingToDeclaration/"></a> [androidJvm] fun [PaymentOrderUrls](-payment-order-urls.md)(hostUrls: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>, completeUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cancelUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, paymentUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, callbackUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, termsOfServiceUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null)   <br>|


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls.Builder///PointingToDeclaration/"></a>[Builder](-builder/index.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls.Builder///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>class [Builder](-builder/index.md)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls.Companion///PointingToDeclaration/"></a>[Companion](-companion/index.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls.Companion///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[describeContents](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-problem/-server/-unknown/index.md#-1578325224%2FFunctions%2F462465411)| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>abstract fun [describeContents](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-problem/-server/-unknown/index.md#-1578325224%2FFunctions%2F462465411)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="android.os/Parcelable/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](../-view-payment-order-info/index.md#-1754457655%2FFunctions%2F462465411)| <a name="android.os/Parcelable/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>abstract fun [writeToParcel](../-view-payment-order-info/index.md#-1754457655%2FFunctions%2F462465411)(p0: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/callbackUrl/#/PointingToDeclaration/"></a>[callbackUrl](callback-url.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/callbackUrl/#/PointingToDeclaration/"></a> [androidJvm] @SerializedName(value = callbackUrl)  <br>  <br>val [callbackUrl](callback-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullA URL on your server that receives status callbacks related to the payment.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/cancelUrl/#/PointingToDeclaration/"></a>[cancelUrl](cancel-url.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/cancelUrl/#/PointingToDeclaration/"></a> [androidJvm] @SerializedName(value = cancelUrl)  <br>  <br>val [cancelUrl](cancel-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullThe URL that the payment menu will redirect to when the payment is canceled.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/completeUrl/#/PointingToDeclaration/"></a>[completeUrl](complete-url.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/completeUrl/#/PointingToDeclaration/"></a> [androidJvm] @SerializedName(value = completeUrl)  <br>  <br>val [completeUrl](complete-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The URL that the payment menu will redirect to when the payment is complete.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/hostUrls/#/PointingToDeclaration/"></a>[hostUrls](host-urls.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/hostUrls/#/PointingToDeclaration/"></a> [androidJvm] @SerializedName(value = hostUrls)  <br>  <br>val [hostUrls](host-urls.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>Array of URLs that are valid for embedding this payment order.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/paymentUrl/#/PointingToDeclaration/"></a>[paymentUrl](payment-url.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/paymentUrl/#/PointingToDeclaration/"></a> [androidJvm] @SerializedName(value = paymentUrl)  <br>  <br>val [paymentUrl](payment-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullA URL that will be navigated to when the payment menu needs to be reloaded.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/termsOfServiceUrl/#/PointingToDeclaration/"></a>[termsOfServiceUrl](terms-of-service-url.md)| <a name="com.swedbankpay.mobilesdk/PaymentOrderUrls/termsOfServiceUrl/#/PointingToDeclaration/"></a> [androidJvm] @SerializedName(value = termsOfServiceUrl)  <br>  <br>val [termsOfServiceUrl](terms-of-service-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullA URL to your Terms of Service.   <br>|

