[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [Problem](./index.md)

# Problem

`open class Problem : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)`, `[`Serializable`](https://docs.oracle.com/javase/6/docs/api/java/io/Serializable.html)

An RFC 7807 HTTP API Problem Details object.

The SDK defines a subclass of Problem for problems expected to be
reported from a server implementing the Merchant Backend API.

There is a [subclass](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-problem/index.md)
for problems expected to be reported by a server implementing the
Merchant Backend API.

IMPORTANT: Problem synchronizes on itself, so you should never synchronize
on a Problem object yourself.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Interprets a Gson JsonObject as a Problem.`Problem(jsonObject: JsonObject)`<br>Parses a Problem from a String.`Problem(raw: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)``Problem(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [detail](detail.md) | RFC 7807 default property: a detailed explanation of the problem`val detail: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [instance](instance.md) | RFC 7807 default property: a URI reference that identifies the specific occurrence of the problem`val instance: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [jsonObject](json-object.md) | The raw RFC 7807 object parsed as a Gson JsonObject.`val jsonObject: JsonObject` |
| [raw](raw.md) | The raw RFC 7807 object.`val raw: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [status](status.md) | RFC 7807 default property: the HTTP status code`val status: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [title](title.md) | RFC 7807 default property: a short summary of the problem.`val title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [type](type.md) | RFC 7807 default property: a URI reference that identifies the problem type.`val type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `open fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](write-to-parcel.md) | `open fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`Problem`](./index.md)`>` |

### Inheritors

| Name | Summary |
|---|---|
| [MerchantBackendProblem](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-problem/index.md) | Base class for any problems encountered in the payment.`sealed class MerchantBackendProblem : `[`Problem`](./index.md) |
