//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../index.md)/[MerchantBackend](index.md)



# MerchantBackend  
 [androidJvm] object [MerchantBackend](index.md)

Additional utilities supported by the Merchant Backend

   


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackend/deletePayerOwnerPaymentToken/#android.content.Context#com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration#com.swedbankpay.mobilesdk.merchantbackend.PaymentTokenInfo#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a>[deletePayerOwnerPaymentToken](delete-payer-owner-payment-token.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackend/deletePayerOwnerPaymentToken/#android.content.Context#com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration#com.swedbankpay.mobilesdk.merchantbackend.PaymentTokenInfo#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>suspend fun [deletePayerOwnerPaymentToken](delete-payer-owner-payment-token.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), configuration: [MerchantBackendConfiguration](../-merchant-backend-configuration/index.md), paymentTokenInfo: [PaymentTokenInfo](../-payment-token-info/index.md), comment: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), vararg extraHeaderNamesAndValues: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))  <br>More info  <br>Deletes the specified payment token.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackend/getPayerOwnedPaymentTokens/#android.content.Context#com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a>[getPayerOwnedPaymentTokens](get-payer-owned-payment-tokens.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackend/getPayerOwnedPaymentTokens/#android.content.Context#com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>suspend fun [getPayerOwnedPaymentTokens](get-payer-owned-payment-tokens.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), configuration: [MerchantBackendConfiguration](../-merchant-backend-configuration/index.md), payerReference: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), vararg extraHeaderNamesAndValues: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PayerOwnedPaymentTokensResponse](../-payer-owned-payment-tokens-response/index.md)  <br>More info  <br>Retrieves the payment tokens owned by the given payerReference.  <br><br><br>|

