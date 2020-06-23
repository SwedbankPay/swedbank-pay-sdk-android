[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PayeeInfo](./index.md)

# PayeeInfo

`data class PayeeInfo : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | `class Builder` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PayeeInfo(payeeId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", payeeReference: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", payeeName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, productCategory: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, orderReference: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, subsite: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [orderReference](order-reference.md) | `val orderReference: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [payeeId](payee-id.md) | `val payeeId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [payeeName](payee-name.md) | `val payeeName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [payeeReference](payee-reference.md) | `val payeeReference: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [productCategory](product-category.md) | `val productCategory: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [subsite](subsite.md) | `val subsite: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](write-to-parcel.md) | `fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`PayeeInfo`](./index.md)`>` |
