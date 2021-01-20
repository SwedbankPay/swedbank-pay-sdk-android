[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [PayerOwnedPaymentTokens](./index.md)

# PayerOwnedPaymentTokens

`data class PayerOwnedPaymentTokens`

Payload of [PayerOwnedPaymentTokensResponse](../-payer-owned-payment-tokens-response/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Payload of [PayerOwnedPaymentTokensResponse](../-payer-owned-payment-tokens-response/index.md)`PayerOwnedPaymentTokens(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, payerReference: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, paymentTokens: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PaymentTokenInfo`](../-payment-token-info/index.md)`>)` |

### Properties

| Name | Summary |
|---|---|
| [id](id.md) | The id (url) of this resource.`val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [payerReference](payer-reference.md) | The payerReference associated with these tokens`val payerReference: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [paymentTokens](payment-tokens.md) | The list of tokens and associated information`val paymentTokens: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PaymentTokenInfo`](../-payment-token-info/index.md)`>` |
