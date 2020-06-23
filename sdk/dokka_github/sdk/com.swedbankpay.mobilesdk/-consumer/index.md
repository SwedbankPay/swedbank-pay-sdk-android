[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [Consumer](./index.md)

# Consumer

`data class Consumer : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)`, ExtensibleJsonObject`

A consumer to identify using the [checkin](https://developer.swedbankpay.com/checkout/checkin) flow.

Please refer to the
[Swedbank Pay documentation](https://developer.swedbankpay.com/checkout/checkin#checkin-back-end)
for further information.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | A consumer to identify using the [checkin](https://developer.swedbankpay.com/checkout/checkin) flow.`Consumer(operation: `[`ConsumerOperation`](../-consumer-operation/index.md)` = ConsumerOperation.INITIATE_CONSUMER_SESSION, language: `[`Language`](../-language/index.md)` = Language.ENGLISH, shippingAddressRestrictedToCountryCodes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, extensionProperties: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [extensionProperties](extension-properties.md) | `val extensionProperties: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`?` |
| [language](language.md) | The language to use in the checkin view.`val language: `[`Language`](../-language/index.md) |
| [operation](operation.md) | The operation to perform.`val operation: `[`ConsumerOperation`](../-consumer-operation/index.md) |
| [shippingAddressRestrictedToCountryCodes](shipping-address-restricted-to-country-codes.md) | List of ISO-3166 codes of countries the merchant can ship to.`val shippingAddressRestrictedToCountryCodes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](write-to-parcel.md) | `fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`Consumer`](./index.md)`>` |
