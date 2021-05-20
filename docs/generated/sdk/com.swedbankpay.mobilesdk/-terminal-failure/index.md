//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[TerminalFailure](index.md)



# TerminalFailure  
 [androidJvm] data class [TerminalFailure](index.md) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

Describes a terminal error condition signaled by an onError callback from Swedbank Pay.



See https://developer.swedbankpay.com/checkout/other-features#onerror

   


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[describeContents](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-problem/-server/-unknown/index.md#-1578325224%2FFunctions%2F462465411)| <a name="android.os/Parcelable/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>abstract fun [describeContents](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-problem/-server/-unknown/index.md#-1578325224%2FFunctions%2F462465411)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="android.os/Parcelable/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](../-view-payment-order-info/index.md#-1754457655%2FFunctions%2F462465411)| <a name="android.os/Parcelable/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>abstract fun [writeToParcel](../-view-payment-order-info/index.md#-1754457655%2FFunctions%2F462465411)(p0: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/TerminalFailure/details/#/PointingToDeclaration/"></a>[details](details.md)| <a name="com.swedbankpay.mobilesdk/TerminalFailure/details/#/PointingToDeclaration/"></a> [androidJvm] val [details](details.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?A human readable and descriptive text of the error.   <br>|
| <a name="com.swedbankpay.mobilesdk/TerminalFailure/messageId/#/PointingToDeclaration/"></a>[messageId](message-id.md)| <a name="com.swedbankpay.mobilesdk/TerminalFailure/messageId/#/PointingToDeclaration/"></a> [androidJvm] val [messageId](message-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?A unique identifier for the message.   <br>|
| <a name="com.swedbankpay.mobilesdk/TerminalFailure/origin/#/PointingToDeclaration/"></a>[origin](origin.md)| <a name="com.swedbankpay.mobilesdk/TerminalFailure/origin/#/PointingToDeclaration/"></a> [androidJvm] val [origin](origin.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?"consumer", "paymentmenu", "creditcard", identifies the system that originated the error.   <br>|

