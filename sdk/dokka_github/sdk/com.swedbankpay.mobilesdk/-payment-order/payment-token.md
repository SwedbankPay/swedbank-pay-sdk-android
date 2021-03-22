//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[PaymentOrder](index.md)/[paymentToken](payment-token.md)



# paymentToken  
[androidJvm]  
Content  
@(value = paymentToken)  
  
val [paymentToken](payment-token.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null  
More info  


A payment token to use for this payment.



You must also set [PaymentOrderPayer.payerReference](../-payment-order-payer/payer-reference.md) to use a payment token; the payerReference must match the one used when the payment token was generated.

  



