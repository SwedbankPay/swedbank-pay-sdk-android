[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentOrderUrls](./index.md)

# PaymentOrderUrls

`data class PaymentOrderUrls : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | `class Builder` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PaymentOrderUrls(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, backendUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, callbackUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, termsOfServiceUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, identifier: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = UUID.randomUUID().toString())`<br>`PaymentOrderUrls(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, hostUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, backendUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, callbackUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, termsOfServiceUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, identifier: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = UUID.randomUUID().toString())`<br>`PaymentOrderUrls(hostUrls: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, completeUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cancelUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, paymentUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, callbackUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, termsOfServiceUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [callbackUrl](callback-url.md) | `val callbackUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [cancelUrl](cancel-url.md) | `val cancelUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [completeUrl](complete-url.md) | `val completeUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [hostUrls](host-urls.md) | `val hostUrls: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [paymentUrl](payment-url.md) | `val paymentUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [termsOfServiceUrl](terms-of-service-url.md) | `val termsOfServiceUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](write-to-parcel.md) | `fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`PaymentOrderUrls`](./index.md)`>` |
