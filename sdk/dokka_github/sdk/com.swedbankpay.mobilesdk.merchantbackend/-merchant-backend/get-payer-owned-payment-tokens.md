[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [MerchantBackend](index.md) / [getPayerOwnedPaymentTokens](./get-payer-owned-payment-tokens.md)

# getPayerOwnedPaymentTokens

`suspend fun getPayerOwnedPaymentTokens(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, configuration: `[`MerchantBackendConfiguration`](../-merchant-backend-configuration/index.md)`, payerReference: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg extraHeaderNamesAndValues: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`PayerOwnedPaymentTokensResponse`](../-payer-owned-payment-tokens-response/index.md)

Retrieves the payment tokens owned by the given
payerReference.

Your backend must enable this functionality separately.

### Parameters

`context` - a Context from your application

`configuration` - the backend configuration

`payerReference` - the reference to query

`extraHeaderNamesAndValues` - any header names and values you wish to append to the request

**Return**
the response from Swedbank Pay

