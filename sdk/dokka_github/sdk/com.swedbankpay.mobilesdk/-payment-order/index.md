[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentOrder](./index.md)

# PaymentOrder

`data class PaymentOrder : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)`, ExtensibleJsonObject`

Description a payment order.

This class mirrors the body the Swedbank Pay
[POST /psp/paymentorders](https://developer.swedbankpay.com/checkout/other-features#creating-a-payment-order)
endpoint, and is designed to work together with [MerchantBackendConfiguration](#)
and a server implementing the
[Merchant Backend API](https://https://developer.swedbankpay.com/modules-sdks/mobile-sdk/merchant-backend),
but you can also use it with your custom [Configuration](../-configuration/index.md).

Please refer to the Swedbank Pay documentation for the meaning of the payment order properties.

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | `class Builder` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Description a payment order.`PaymentOrder(operation: `[`PaymentOrderOperation`](../-payment-order-operation/index.md)` = PaymentOrderOperation.PURCHASE, currency: `[`Currency`](https://docs.oracle.com/javase/6/docs/api/java/util/Currency.html)`, amount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, vatAmount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, userAgent: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = DEFAULT_USER_AGENT, language: `[`Language`](../-language/index.md)` = Language.ENGLISH, instrument: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, generateRecurrenceToken: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, generatePaymentToken: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, restrictedToInstruments: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>? = null, urls: `[`PaymentOrderUrls`](../-payment-order-urls/index.md)`, payeeInfo: `[`PayeeInfo`](../-payee-info/index.md)` = PayeeInfo(), payer: `[`PaymentOrderPayer`](../-payment-order-payer/index.md)`? = null, orderItems: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`OrderItem`](../-order-item/index.md)`>? = null, riskIndicator: `[`RiskIndicator`](../-risk-indicator/index.md)`? = null, disablePaymentMenu: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, extensionProperties: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [amount](amount.md) | `val amount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [currency](currency.md) | `val currency: `[`Currency`](https://docs.oracle.com/javase/6/docs/api/java/util/Currency.html) |
| [description](description.md) | `val description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [disablePaymentMenu](disable-payment-menu.md) | `val disablePaymentMenu: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [extensionProperties](extension-properties.md) | `val extensionProperties: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`?` |
| [generatePaymentToken](generate-payment-token.md) | `val generatePaymentToken: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` |
| [generateRecurrenceToken](generate-recurrence-token.md) | `val generateRecurrenceToken: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [instrument](instrument.md) | `val instrument: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [language](language.md) | `val language: `[`Language`](../-language/index.md) |
| [operation](operation.md) | `val operation: `[`PaymentOrderOperation`](../-payment-order-operation/index.md) |
| [orderItems](order-items.md) | `val orderItems: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`OrderItem`](../-order-item/index.md)`>?` |
| [payeeInfo](payee-info.md) | `val payeeInfo: `[`PayeeInfo`](../-payee-info/index.md) |
| [payer](payer.md) | `val payer: `[`PaymentOrderPayer`](../-payment-order-payer/index.md)`?` |
| [restrictedToInstruments](restricted-to-instruments.md) | `val restrictedToInstruments: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` |
| [riskIndicator](risk-indicator.md) | `val riskIndicator: `[`RiskIndicator`](../-risk-indicator/index.md)`?` |
| [urls](urls.md) | `val urls: `[`PaymentOrderUrls`](../-payment-order-urls/index.md) |
| [userAgent](user-agent.md) | `val userAgent: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [vatAmount](vat-amount.md) | `val vatAmount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](write-to-parcel.md) | `fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`PaymentOrder`](./index.md)`>` |
| [DEFAULT_USER_AGENT](-d-e-f-a-u-l-t_-u-s-e-r_-a-g-e-n-t.md) | Default value for the [userAgent](user-agent.md) property.`const val DEFAULT_USER_AGENT: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
