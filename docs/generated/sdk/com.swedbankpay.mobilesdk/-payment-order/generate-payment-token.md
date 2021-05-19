//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[PaymentOrder](index.md)/[generatePaymentToken](generate-payment-token.md)



# generatePaymentToken  
[androidJvm]  
Content  
@SerializedName(value = generatePaymentToken)  
  
val [generatePaymentToken](generate-payment-token.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false  
More info  


If true, a payment token will be created from this payment order



You must also set [PaymentOrderPayer.payerReference](../-payment-order-payer/payer-reference.md) to generate a payment token. The payment token can be used later to reuse the same payment details; see [paymentToken](payment-token.md).

  



