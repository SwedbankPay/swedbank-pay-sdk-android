//[sdk](../../../../index.md)/[com.swedbankpay.mobilesdk](../../index.md)/[PaymentViewModel](../index.md)/[OnTermsOfServiceClickListener](index.md)/[onTermsOfServiceClick](on-terms-of-service-click.md)



# onTermsOfServiceClick  
[androidJvm]  
Content  
abstract fun [onTermsOfServiceClick](on-terms-of-service-click.md)(paymentViewModel: [PaymentViewModel](../index.md), url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  
More info  


Called when the user clicks on the Terms of Service link in the Payment Menu.



#### Return  


true if you handled the event yourself and wish to disable the default behaviour, false if you want to let the SDK show the ToS web page.



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.OnTermsOfServiceClickListener/onTermsOfServiceClick/#com.swedbankpay.mobilesdk.PaymentViewModel#kotlin.String/PointingToDeclaration/"></a>paymentViewModel| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.OnTermsOfServiceClickListener/onTermsOfServiceClick/#com.swedbankpay.mobilesdk.PaymentViewModel#kotlin.String/PointingToDeclaration/"></a><br><br>the [PaymentViewModel](../index.md) of the [PaymentFragment](../../-payment-fragment/index.md) the user is interacting with<br><br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.OnTermsOfServiceClickListener/onTermsOfServiceClick/#com.swedbankpay.mobilesdk.PaymentViewModel#kotlin.String/PointingToDeclaration/"></a>url| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.OnTermsOfServiceClickListener/onTermsOfServiceClick/#com.swedbankpay.mobilesdk.PaymentViewModel#kotlin.String/PointingToDeclaration/"></a><br><br>the Terms of Service url<br><br>|
  
  



