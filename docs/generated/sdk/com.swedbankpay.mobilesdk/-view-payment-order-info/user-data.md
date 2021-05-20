//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[ViewPaymentOrderInfo](index.md)/[userData](user-data.md)



# userData  
[androidJvm]  
Content  
val [userData](user-data.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null  
More info  


Any value you may need for your [Configuration](../-configuration/index.md).



The value must be one that is valid for [Parcel.writeValue](https://developer.android.com/reference/kotlin/android/os/Parcel.html#writevalue), e.g. [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) or [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html).



See [Configuration.updatePaymentOrder](../-configuration/update-payment-order.md); you will receive this ViewPaymentOrderInfo object there.

  



