[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [PayerOwnedPaymentTokensResponse](./index.md)

# PayerOwnedPaymentTokensResponse

`data class PayerOwnedPaymentTokensResponse`

Response to [MerchantBackend.getPayerOwnedPaymentTokens](../-merchant-backend/get-payer-owned-payment-tokens.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Response to [MerchantBackend.getPayerOwnedPaymentTokens](../-merchant-backend/get-payer-owned-payment-tokens.md)`PayerOwnedPaymentTokensResponse(payerOwnedPaymentTokens: `[`PayerOwnedPaymentTokens`](../-payer-owned-payment-tokens/index.md)`, operations: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Operation`](../-operation/index.md)`>)` |

### Properties

| Name | Summary |
|---|---|
| [operations](operations.md) | Operations you can perform on the whole list of tokens. Note that you generally cannot call these from your mobile app.`val operations: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Operation`](../-operation/index.md)`>` |
| [payerOwnedPaymentTokens](payer-owned-payment-tokens.md) | The response payload.`val payerOwnedPaymentTokens: `[`PayerOwnedPaymentTokens`](../-payer-owned-payment-tokens/index.md) |
