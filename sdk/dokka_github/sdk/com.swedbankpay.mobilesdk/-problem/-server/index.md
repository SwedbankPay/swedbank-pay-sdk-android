[sdk](../../../index.md) / [com.swedbankpay.mobilesdk](../../index.md) / [Problem](../index.md) / [Server](./index.md)

# Server

`sealed class Server : `[`Problem`](../index.md)

Base class for [Problems](../index.md) caused by the service backend.

Any unexpected response where the HTTP status is outside 400-499
results in a Server Problem; usually it means the status was in 500-599.

### Types

| Name | Summary |
|---|---|
| [MobileSDK](-mobile-s-d-k/index.md) | Base class for [Server](./index.md) Problems defined by the example backend.`sealed class MobileSDK : Server, `[`ProperProblem`](../../-proper-problem/index.md) |
| [SwedbankPay](-swedbank-pay/index.md) | Base class for [Server](./index.md) problems defined by the Swedbank Pay backend. [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems](https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems)`sealed class SwedbankPay : Server, `[`SwedbankPayProblem`](../../-swedbank-pay-problem/index.md) |
| [UnexpectedContent](-unexpected-content/index.md) | Pseudo-problem, not actually parsed from an application/problem+json response. This problem is emitted if the server response is in an unexpected format and the HTTP status is not in the Client Error range.`class UnexpectedContent : Server, `[`UnexpectedContentProblem`](../../-unexpected-content-problem/index.md) |
| [Unknown](-unknown/index.md) | [Server](./index.md) problem with an unrecognized type.`class Unknown : Server, `[`UnknownProblem`](../../-unknown-problem/index.md) |
