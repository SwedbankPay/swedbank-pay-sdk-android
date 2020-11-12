[sdk](../../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../../index.md) / [MerchantBackendProblem](../index.md) / [Server](./index.md)

# Server

`sealed class Server : `[`MerchantBackendProblem`](../index.md)

Base class for [Problems](../index.md) caused by the service backend.

Any unexpected response where the HTTP status is outside 400-499
results in a Server Problem; usually it means the status was in 500-599.

### Types

| Name | Summary |
|---|---|
| [MobileSDK](-mobile-s-d-k/index.md) | Base class for [Server](./index.md) Problems defined by the example backend.`sealed class MobileSDK : Server` |
| [SwedbankPay](-swedbank-pay/index.md) | Base class for [Server](./index.md) problems defined by the Swedbank Pay backend. [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems](https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems)`sealed class SwedbankPay : Server, `[`SwedbankPayProblem`](../../-swedbank-pay-problem/index.md) |
| [Unknown](-unknown/index.md) | [Server](./index.md) problem with an unrecognized type.`class Unknown : Server` |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<Server>` |
