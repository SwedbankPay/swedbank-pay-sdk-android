[sdk](../../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../../index.md) / [MerchantBackendProblem](../index.md) / [Client](./index.md)

# Client

`sealed class Client : `[`MerchantBackendProblem`](../index.md)

Base class for [Problems](../index.md) caused by the service refusing or
not understanding a request sent to it by the client.

A Client Problem always implies a HTTP status in 400-499.

### Types

| Name | Summary |
|---|---|
| [MobileSDK](-mobile-s-d-k/index.md) | Base class for [Client](./index.md) Problems defined by the example backend.`sealed class MobileSDK : Client` |
| [SwedbankPay](-swedbank-pay/index.md) | Base class for [Client](./index.md) problems defined by the Swedbank Pay backend. [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems](https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems)`sealed class SwedbankPay : Client, `[`SwedbankPayProblem`](../../-swedbank-pay-problem/index.md) |
| [Unknown](-unknown/index.md) | [Client](./index.md) problem with an unrecognized type.`class Unknown : Client` |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<Client>` |
