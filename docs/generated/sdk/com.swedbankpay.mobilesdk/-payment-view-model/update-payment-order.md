//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[PaymentViewModel](index.md)/[updatePaymentOrder](update-payment-order.md)



# updatePaymentOrder  
[androidJvm]  
Content  
fun [updatePaymentOrder](update-payment-order.md)(updateInfo: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?)  
More info  


Attempts to update the ongoing payment order. The meaning of updateInfo is up to your  [Configuration.updatePaymentOrder](../-configuration/update-payment-order.md) implementation.



If you are using [com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-configuration/index.md), and the payment order is in instrument mode, you can set the instrument by calling this with a String argument specifying the new instrument; see [PaymentInstruments](../-payment-instruments/index.md).



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel/updatePaymentOrder/#kotlin.Any?/PointingToDeclaration/"></a>updateInfo| <a name="com.swedbankpay.mobilesdk/PaymentViewModel/updatePaymentOrder/#kotlin.Any?/PointingToDeclaration/"></a><br><br>any data you need to perform the update. The value must be one that is valid for [Parcel.writeValue](https://developer.android.com/reference/kotlin/android/os/Parcel.html#writevalue), e.g. [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) or [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html).<br><br>|
  
  



