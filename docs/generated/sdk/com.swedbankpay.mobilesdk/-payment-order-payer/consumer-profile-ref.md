//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[PaymentOrderPayer](index.md)/[consumerProfileRef](consumer-profile-ref.md)



# consumerProfileRef  
[androidJvm]  
Content  
@SerializedName(value = consumerProfileRef)  
  
val [consumerProfileRef](consumer-profile-ref.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null  
More info  


A consumer profile reference obtained through the Checkin flow.



If you have your [PaymentFragment](../-payment-fragment/index.md) to do the Checkin flow (see [PaymentFragment.ArgumentsBuilder.consumer](../-payment-fragment/-arguments-builder/consumer.md) and [PaymentFragment.ArgumentsBuilder.useCheckin](../-payment-fragment/-arguments-builder/use-checkin.md)), your [Configuration.postPaymentorders](../-configuration/post-paymentorders.md) will be called with the consumerProfileRef received from the Checkin flow. Your Configuration can then use that value here to forward it to your backend for payment order creation.

  



