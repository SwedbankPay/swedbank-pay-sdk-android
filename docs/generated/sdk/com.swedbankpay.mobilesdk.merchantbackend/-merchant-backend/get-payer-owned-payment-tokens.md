//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../index.md)/[MerchantBackend](index.md)/[getPayerOwnedPaymentTokens](get-payer-owned-payment-tokens.md)



# getPayerOwnedPaymentTokens  
[androidJvm]  
Content  
suspend fun [getPayerOwnedPaymentTokens](get-payer-owned-payment-tokens.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), configuration: [MerchantBackendConfiguration](../-merchant-backend-configuration/index.md), payerReference: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), vararg extraHeaderNamesAndValues: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PayerOwnedPaymentTokensResponse](../-payer-owned-payment-tokens-response/index.md)  
More info  


Retrieves the payment tokens owned by the given payerReference.



Your backend must enable this functionality separately.



#### Return  


the response from Swedbank Pay



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackend/getPayerOwnedPaymentTokens/#android.content.Context#com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a>context| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackend/getPayerOwnedPaymentTokens/#android.content.Context#com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a><br><br>a Context from your application<br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackend/getPayerOwnedPaymentTokens/#android.content.Context#com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a>configuration| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackend/getPayerOwnedPaymentTokens/#android.content.Context#com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a><br><br>the backend configuration<br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackend/getPayerOwnedPaymentTokens/#android.content.Context#com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a>payerReference| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackend/getPayerOwnedPaymentTokens/#android.content.Context#com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a><br><br>the reference to query<br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackend/getPayerOwnedPaymentTokens/#android.content.Context#com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a>extraHeaderNamesAndValues| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackend/getPayerOwnedPaymentTokens/#android.content.Context#com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a><br><br>any header names and values you wish to append to the request<br><br>|
  
  



