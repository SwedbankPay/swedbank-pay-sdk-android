[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentOrder](./index.md)

# PaymentOrder

`data class PaymentOrder : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)`, ExtensibleJsonObject`

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | `class Builder` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PaymentOrder(operation: `[`PaymentOrderOperation`](../-payment-order-operation/index.md)` = Defaults.operation, currency: `[`Currency`](https://docs.oracle.com/javase/6/docs/api/java/util/Currency.html)`, amount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, vatAmount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, userAgent: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = Defaults.userAgent, language: `[`Language`](../-language/index.md)` = Defaults.language, generateRecurrenceToken: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = Defaults.generateRecurrenceToken, restrictedToInstruments: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>? = Defaults.restrictedToInstruments, urls: `[`PaymentOrderUrls`](../-payment-order-urls/index.md)`, payeeInfo: `[`PayeeInfo`](../-payee-info/index.md)` = Defaults.payeeInfo, payer: `[`PaymentOrderPayer`](../-payment-order-payer/index.md)`? = Defaults.payer, orderItems: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`OrderItem`](../-order-item/index.md)`>? = Defaults.orderItems, riskIndicator: `[`RiskIndicator`](../-risk-indicator/index.md)`? = Defaults.riskIndicator, disablePaymentMenu: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = Defaults.disablePaymentMenu, extensionProperties: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [amount](amount.md) | `val amount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [currency](currency.md) | `val currency: `[`Currency`](https://docs.oracle.com/javase/6/docs/api/java/util/Currency.html) |
| [description](description.md) | `val description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [disablePaymentMenu](disable-payment-menu.md) | `val disablePaymentMenu: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [extensionProperties](extension-properties.md) | `val extensionProperties: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`?` |
| [generateRecurrenceToken](generate-recurrence-token.md) | `val generateRecurrenceToken: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
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
