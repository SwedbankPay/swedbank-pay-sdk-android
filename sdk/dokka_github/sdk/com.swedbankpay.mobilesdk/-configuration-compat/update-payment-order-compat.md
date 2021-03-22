//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[ConfigurationCompat](index.md)/[updatePaymentOrderCompat](update-payment-order-compat.md)



# updatePaymentOrderCompat  
[androidJvm]  
Content  
@[WorkerThread](https://developer.android.com/reference/kotlin/androidx/annotation/WorkerThread.html)()  
  
open fun [updatePaymentOrderCompat](update-payment-order-compat.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), paymentOrder: [PaymentOrder](../-payment-order/index.md)?, userData: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, viewPaymentOrderInfo: [ViewPaymentOrderInfo](../-view-payment-order-info/index.md), updateInfo: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [ViewPaymentOrderInfo](../-view-payment-order-info/index.md)  
More info  


Called by [PaymentFragment](../-payment-fragment/index.md) when it needs to update a payment order.



If you do not update payment orders after they have been created, you do not need to override this method.



#### Return  


ViewPaymentOrderInfo describing the payment order with the changed instrument



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat/updatePaymentOrderCompat/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#com.swedbankpay.mobilesdk.ViewPaymentOrderInfo#kotlin.Any?/PointingToDeclaration/"></a>context| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat/updatePaymentOrderCompat/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#com.swedbankpay.mobilesdk.ViewPaymentOrderInfo#kotlin.Any?/PointingToDeclaration/"></a><br><br>an application context<br><br>|
| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat/updatePaymentOrderCompat/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#com.swedbankpay.mobilesdk.ViewPaymentOrderInfo#kotlin.Any?/PointingToDeclaration/"></a>paymentOrder| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat/updatePaymentOrderCompat/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#com.swedbankpay.mobilesdk.ViewPaymentOrderInfo#kotlin.Any?/PointingToDeclaration/"></a><br><br>the [PaymentOrder](../-payment-order/index.md) object set as the PaymentFragment argument<br><br>|
| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat/updatePaymentOrderCompat/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#com.swedbankpay.mobilesdk.ViewPaymentOrderInfo#kotlin.Any?/PointingToDeclaration/"></a>userData| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat/updatePaymentOrderCompat/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#com.swedbankpay.mobilesdk.ViewPaymentOrderInfo#kotlin.Any?/PointingToDeclaration/"></a><br><br>the user data object set as the PaymentFragment argument<br><br>|
| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat/updatePaymentOrderCompat/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#com.swedbankpay.mobilesdk.ViewPaymentOrderInfo#kotlin.Any?/PointingToDeclaration/"></a>viewPaymentOrderInfo| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat/updatePaymentOrderCompat/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#com.swedbankpay.mobilesdk.ViewPaymentOrderInfo#kotlin.Any?/PointingToDeclaration/"></a><br><br>the current [ViewPaymentOrderInfo](../-view-payment-order-info/index.md) as returned from a call to this or [postPaymentorders](post-paymentorders.md)<br><br>|
| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat/updatePaymentOrderCompat/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#com.swedbankpay.mobilesdk.ViewPaymentOrderInfo#kotlin.Any?/PointingToDeclaration/"></a>updateInfo| <a name="com.swedbankpay.mobilesdk/ConfigurationCompat/updatePaymentOrderCompat/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#com.swedbankpay.mobilesdk.ViewPaymentOrderInfo#kotlin.Any?/PointingToDeclaration/"></a><br><br>the updateInfo value from the [PaymentViewModel.updatePaymentOrder](../-payment-view-model/update-payment-order.md) call<br><br>|
  
  



