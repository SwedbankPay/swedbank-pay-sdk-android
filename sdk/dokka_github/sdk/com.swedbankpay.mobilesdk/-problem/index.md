[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [Problem](./index.md)

# Problem

`sealed class Problem : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)

Base class for any problems encountered in the payment.

Problems always result from communication with the backend;
lower-level network errors are not represented by Problems,
but rather by IOExceptions as is usual.

Swedbank interfaces, as well as the example merchant backend,
report problems using the Problem Details for HTTP APIs
protocol (https://tools.ietf.org/html/rfc7807), specifically the
json representation. Your custom merchant backend is enouraged to
do so as well. These classes provide a convenient java representation
of the problems so your client code does not need to deal with the raw
json. Any custom problem cases you add to your merchant backend will
be reported as "Unknown" problems, and you will have to implement
parsing for those in your client, of course.

All problems are either [Client](-client/index.md) or [Server](-server/index.md) problems. A Client
problem is one where there was something wrong with the request
the client app sent to the service. A Client problem always implies an
HTTP response status in the Client Error range, 400-499.

A Server problem in one where the service understood the request, but
could not fulfill it. If the backend responds in an unexpected
manner, the situation will be interpreted as a Server error, unless
the response status is in 400-499, in which case it is still considered a
Client error.

This separation to Client and Server errors provides a crude but often
effective way of distinguishing between temporary service unavailability
and permanent configuration errors. Indeed, the PaymentFragment will internally
consider any Client errors to be fatal, but most Server errors to be retryable.

Client and Server errors are further divided to specific types. See individual
class documentation for details.

There are also several interfaces defined for related problem types.
They are:

* [ProperProblem](../-proper-problem/index.md): problems actually parsed from a application/problem+json object
* [SwedbankPayProblem](../-swedbank-pay-problem/index.md): problems originating from Swedbank Pay backends
* [UnknownProblem](../-unknown-problem/index.md): problems of an unknown [type](https://tools.ietf.org/html/rfc7807#section-3.1)
* [UnexpectedContentProblem](../-unexpected-content-problem/index.md): (pseudo-)problems where the response was not application/problem+json

### Types

| Name | Summary |
|---|---|
| [Client](-client/index.md) | Base class for [Problems](./index.md) caused by the service refusing or not understanding a request sent to it by the client.`sealed class Client : `[`Problem`](./index.md) |
| [Server](-server/index.md) | Base class for [Problems](./index.md) caused by the service backend.`sealed class Server : `[`Problem`](./index.md) |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `open fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](write-to-parcel.md) | `open fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`Problem`](./index.md)`>` |
