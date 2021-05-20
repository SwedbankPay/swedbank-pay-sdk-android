//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[ViewPaymentOrderInfo](index.md)



# ViewPaymentOrderInfo  
 [androidJvm] data class [ViewPaymentOrderInfo](index.md)(**webViewBaseUrl**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **viewPaymentOrder**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **completeUrl**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **cancelUrl**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **paymentUrl**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **termsOfServiceUrl**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **instrument**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **availableInstruments**: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>?, **userData**: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

Data required to show the payment menu.



If you provide a custom [Configuration](../-configuration/index.md), you must get the relevant data from your services and return a ViewPaymentOrderInfo object in your [Configuration.postPaymentorders](../-configuration/post-paymentorders.md) method.

   


## Constructors  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/ViewPaymentOrderInfo/#kotlin.String?#kotlin.String#kotlin.String#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.collections.List[kotlin.String]?#kotlin.Any?/PointingToDeclaration/"></a>[ViewPaymentOrderInfo](-view-payment-order-info.md)| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/ViewPaymentOrderInfo/#kotlin.String?#kotlin.String#kotlin.String#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.collections.List[kotlin.String]?#kotlin.Any?/PointingToDeclaration/"></a> [androidJvm] fun [ViewPaymentOrderInfo](-view-payment-order-info.md)(webViewBaseUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, viewPaymentOrder: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), completeUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cancelUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, paymentUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, termsOfServiceUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, instrument: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, availableInstruments: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>? = null, userData: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null)   <br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[describeContents](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-problem/-server/-unknown/index.md#-1578325224%2FFunctions%2F462465411)| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>abstract fun [describeContents](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-problem/-server/-unknown/index.md#-1578325224%2FFunctions%2F462465411)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="android.os/Parcelable/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](index.md#-1754457655%2FFunctions%2F462465411)| <a name="android.os/Parcelable/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>abstract fun [writeToParcel](index.md#-1754457655%2FFunctions%2F462465411)(p0: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/availableInstruments/#/PointingToDeclaration/"></a>[availableInstruments](available-instruments.md)| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/availableInstruments/#/PointingToDeclaration/"></a> [androidJvm] val [availableInstruments](available-instruments.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>? = nullIf the payment order is in instrument mode, all the valid instruments for it.   <br>|
| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/cancelUrl/#/PointingToDeclaration/"></a>[cancelUrl](cancel-url.md)| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/cancelUrl/#/PointingToDeclaration/"></a> [androidJvm] val [cancelUrl](cancel-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullThe cancelUrl of the payment order   <br>|
| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/completeUrl/#/PointingToDeclaration/"></a>[completeUrl](complete-url.md)| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/completeUrl/#/PointingToDeclaration/"></a> [androidJvm] val [completeUrl](complete-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The completeUrl of the payment order   <br>|
| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/instrument/#/PointingToDeclaration/"></a>[instrument](instrument.md)| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/instrument/#/PointingToDeclaration/"></a> [androidJvm] val [instrument](instrument.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullIf the payment order is in instrument mode, the current instrument.   <br>|
| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/paymentUrl/#/PointingToDeclaration/"></a>[paymentUrl](payment-url.md)| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/paymentUrl/#/PointingToDeclaration/"></a> [androidJvm] val [paymentUrl](payment-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?The paymentUrl of the payment order   <br>|
| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/termsOfServiceUrl/#/PointingToDeclaration/"></a>[termsOfServiceUrl](terms-of-service-url.md)| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/termsOfServiceUrl/#/PointingToDeclaration/"></a> [androidJvm] val [termsOfServiceUrl](terms-of-service-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullThe termsOfServiceUrl of the payment order   <br>|
| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/userData/#/PointingToDeclaration/"></a>[userData](user-data.md)| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/userData/#/PointingToDeclaration/"></a> [androidJvm] val [userData](user-data.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = nullAny value you may need for your [Configuration](../-configuration/index.md).   <br>|
| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/viewPaymentOrder/#/PointingToDeclaration/"></a>[viewPaymentOrder](view-payment-order.md)| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/viewPaymentOrder/#/PointingToDeclaration/"></a> [androidJvm] val [viewPaymentOrder](view-payment-order.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)The view-paymentorder link from Swedbank Pay.   <br>|
| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/webViewBaseUrl/#/PointingToDeclaration/"></a>[webViewBaseUrl](web-view-base-url.md)| <a name="com.swedbankpay.mobilesdk/ViewPaymentOrderInfo/webViewBaseUrl/#/PointingToDeclaration/"></a> [androidJvm] val [webViewBaseUrl](web-view-base-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullThe url to use as the [android.webkit.WebView](https://developer.android.com/reference/kotlin/android/webkit/WebView.html) page url when showing the checkin UI.   <br>|

