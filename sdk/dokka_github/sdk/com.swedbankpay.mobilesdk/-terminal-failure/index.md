[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [TerminalFailure](./index.md)

# TerminalFailure

`class TerminalFailure : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)

Describes a terminal error condition signaled by an onError callback from Swedbank Pay.

See [https://developer.swedbankpay.com/checkout/other-features#onerror](https://developer.swedbankpay.com/checkout/other-features#onerror)

### Properties

| Name | Summary |
|---|---|
| [details](details.md) | A human readable and descriptive text of the error.`val details: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [messageId](message-id.md) | A unique identifier for the message.`val messageId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [origin](origin.md) | `"consumer"`, `"paymentmenu"`, `"creditcard"`, identifies the system that originated the error.`val origin: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](write-to-parcel.md) | `fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`TerminalFailure`](./index.md)`>` |
