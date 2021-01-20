[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PickUpAddress](./index.md)

# PickUpAddress

`data class PickUpAddress : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)

Pick-up address data for [RiskIndicator](../-risk-indicator/index.md).

When using [ShipIndicator.PICK_UP_AT_STORE](../-ship-indicator/-p-i-c-k_-u-p_-a-t_-s-t-o-r-e.md), you should populate this data as completely as
possible to decrease the risk factor of the purchase.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PickUpAddress(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`)`<br>Pick-up address data for [RiskIndicator](../-risk-indicator/index.md).`PickUpAddress(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, streetAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, coAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, city: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, zipCode: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, countryCode: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [city](city.md) | City of the payer`val city: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [coAddress](co-address.md) | C/O address of the payer`val coAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [countryCode](country-code.md) | Country code of the payer`val countryCode: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [name](name.md) | Name of the payer`val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [streetAddress](street-address.md) | Street address of the payer`val streetAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [zipCode](zip-code.md) | Zip code of the payer`val zipCode: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](write-to-parcel.md) | `fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`PickUpAddress`](./index.md)`>` |
