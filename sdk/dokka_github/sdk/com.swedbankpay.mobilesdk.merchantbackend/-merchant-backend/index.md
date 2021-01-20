[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [MerchantBackend](./index.md)

# MerchantBackend

`object MerchantBackend`

Additional utilities supported by the Merchant Backend

### Functions

| Name | Summary |
|---|---|
| [getPayerOwnedPaymentTokens](get-payer-owned-payment-tokens.md) | Retrieves the payment tokens owned by the given payerReference.`suspend fun getPayerOwnedPaymentTokens(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, configuration: `[`MerchantBackendConfiguration`](../-merchant-backend-configuration/index.md)`, payerReference: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg extraHeaderNamesAndValues: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`PayerOwnedPaymentTokensResponse`](../-payer-owned-payment-tokens-response/index.md) |
