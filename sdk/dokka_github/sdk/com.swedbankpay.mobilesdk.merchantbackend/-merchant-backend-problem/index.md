[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [MerchantBackendProblem](./index.md)

# MerchantBackendProblem

`sealed class MerchantBackendProblem : `[`Problem`](../../com.swedbankpay.mobilesdk/-problem/index.md)

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

### Types

| Name | Summary |
|---|---|
| [Client](-client/index.md) | Base class for [Problems](./index.md) caused by the service refusing or not understanding a request sent to it by the client.`sealed class Client : `[`MerchantBackendProblem`](./index.md) |
| [Server](-server/index.md) | Base class for [Problems](./index.md) caused by the service backend.`sealed class Server : `[`MerchantBackendProblem`](./index.md) |

### Functions

| Name | Summary |
|---|---|
| [writeToParcel](write-to-parcel.md) | `open fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
