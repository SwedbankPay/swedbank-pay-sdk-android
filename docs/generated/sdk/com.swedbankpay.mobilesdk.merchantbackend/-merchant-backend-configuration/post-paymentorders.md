//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../index.md)/[MerchantBackendConfiguration](index.md)/[postPaymentorders](post-paymentorders.md)



# postPaymentorders  
[androidJvm]  
Content  
open suspend override fun [postPaymentorders](post-paymentorders.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), paymentOrder: [PaymentOrder](../../com.swedbankpay.mobilesdk/-payment-order/index.md)?, userData: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, consumerProfileRef: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): [ViewPaymentOrderInfo](../../com.swedbankpay.mobilesdk/-view-payment-order-info/index.md)  
More info  


Called by [PaymentFragment](../../com.swedbankpay.mobilesdk/-payment-fragment/index.md) when it needs to create a payment order. Your implementation must ultimately make the call to Swedbank Pay API and return a [ViewPaymentOrderInfo](../../com.swedbankpay.mobilesdk/-view-payment-order-info/index.md) describing the result.



#### Return  


ViewPaymentOrderInfo describing the payment order



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration/postPaymentorders/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#kotlin.String?/PointingToDeclaration/"></a>context| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration/postPaymentorders/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#kotlin.String?/PointingToDeclaration/"></a><br><br>an application context<br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration/postPaymentorders/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#kotlin.String?/PointingToDeclaration/"></a>paymentOrder| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration/postPaymentorders/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#kotlin.String?/PointingToDeclaration/"></a><br><br>the [PaymentOrder](../../com.swedbankpay.mobilesdk/-payment-order/index.md) object set as the PaymentFragment argument<br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration/postPaymentorders/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#kotlin.String?/PointingToDeclaration/"></a>userData| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration/postPaymentorders/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#kotlin.String?/PointingToDeclaration/"></a><br><br>the user data object set as the PaymentFragment argument<br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration/postPaymentorders/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#kotlin.String?/PointingToDeclaration/"></a>consumerProfileRef| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration/postPaymentorders/#android.content.Context#com.swedbankpay.mobilesdk.PaymentOrder?#kotlin.Any?#kotlin.String?/PointingToDeclaration/"></a><br><br>if a checkin was performed first, the consumerProfileRef from checkin<br><br>|
  
  



