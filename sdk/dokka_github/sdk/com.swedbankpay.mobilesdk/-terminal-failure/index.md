//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[TerminalFailure](index.md)



# TerminalFailure  
 [androidJvm] class [TerminalFailure](index.md) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

Describes a terminal error condition signaled by an onError callback from Swedbank Pay.



See https://developer.swedbankpay.com/checkout/other-features#onerror

   


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/TerminalFailure.Companion///PointingToDeclaration/"></a>[Companion](-companion/index.md)| <a name="com.swedbankpay.mobilesdk/TerminalFailure.Companion///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/TerminalFailure/describeContents/#/PointingToDeclaration/"></a>[describeContents](describe-contents.md)| <a name="com.swedbankpay.mobilesdk/TerminalFailure/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [describeContents](describe-contents.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/TerminalFailure/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](write-to-parcel.md)| <a name="com.swedbankpay.mobilesdk/TerminalFailure/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [writeToParcel](write-to-parcel.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/TerminalFailure/details/#/PointingToDeclaration/"></a>[details](details.md)| <a name="com.swedbankpay.mobilesdk/TerminalFailure/details/#/PointingToDeclaration/"></a> [androidJvm] val [details](details.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?A human readable and descriptive text of the error.   <br>|
| <a name="com.swedbankpay.mobilesdk/TerminalFailure/messageId/#/PointingToDeclaration/"></a>[messageId](message-id.md)| <a name="com.swedbankpay.mobilesdk/TerminalFailure/messageId/#/PointingToDeclaration/"></a> [androidJvm] val [messageId](message-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?A unique identifier for the message.   <br>|
| <a name="com.swedbankpay.mobilesdk/TerminalFailure/origin/#/PointingToDeclaration/"></a>[origin](origin.md)| <a name="com.swedbankpay.mobilesdk/TerminalFailure/origin/#/PointingToDeclaration/"></a> [androidJvm] val [origin](origin.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?"consumer", "paymentmenu", "creditcard", identifies the system that originated the error.   <br>|

