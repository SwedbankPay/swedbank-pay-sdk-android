[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentOrder](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`PaymentOrder(operation: `[`PaymentOrderOperation`](../-payment-order-operation/index.md)` = PaymentOrderOperation.PURCHASE, currency: `[`Currency`](https://docs.oracle.com/javase/6/docs/api/java/util/Currency.html)`, amount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, vatAmount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, userAgent: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = DEFAULT_USER_AGENT, language: `[`Language`](../-language/index.md)` = Language.ENGLISH, instrument: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, generateRecurrenceToken: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, generatePaymentToken: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, restrictedToInstruments: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>? = null, urls: `[`PaymentOrderUrls`](../-payment-order-urls/index.md)`, payeeInfo: `[`PayeeInfo`](../-payee-info/index.md)` = PayeeInfo(), payer: `[`PaymentOrderPayer`](../-payment-order-payer/index.md)`? = null, orderItems: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`OrderItem`](../-order-item/index.md)`>? = null, riskIndicator: `[`RiskIndicator`](../-risk-indicator/index.md)`? = null, disablePaymentMenu: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, extensionProperties: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`? = null)`

Description a payment order.

This class mirrors the body the Swedbank Pay
[POST /psp/paymentorders](https://developer.swedbankpay.com/checkout/other-features#creating-a-payment-order)
endpoint, and is designed to work together with [MerchantBackendConfiguration](#)
and a server implementing the
[Merchant Backend API](https://https://developer.swedbankpay.com/modules-sdks/mobile-sdk/merchant-backend),
but you can also use it with your custom [Configuration](../-configuration/index.md).

Please refer to the Swedbank Pay documentation for the meaning of the payment order properties.

