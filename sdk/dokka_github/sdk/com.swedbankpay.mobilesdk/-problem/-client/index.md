[sdk](../../../index.md) / [com.swedbankpay.mobilesdk](../../index.md) / [Problem](../index.md) / [Client](./index.md)

# Client

`sealed class Client : `[`Problem`](../index.md)

Base class for [Problems](../index.md) caused by the service refusing or
not understanding a request sent to it by the client.

A Client Problem always implies a HTTP status in 400-499.

### Types

| Name | Summary |
|---|---|
| [MobileSDK](-mobile-s-d-k/index.md) | Base class for [Client](./index.md) Problems defined by the example backend.`sealed class MobileSDK : Client, `[`ProperProblem`](../../-proper-problem/index.md) |
| [SwedbankPay](-swedbank-pay/index.md) | Base class for [Client](./index.md) problems defined by the Swedbank Pay backend. [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems](https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems)`sealed class SwedbankPay : Client, `[`SwedbankPayProblem`](../../-swedbank-pay-problem/index.md) |
| [UnexpectedContent](-unexpected-content/index.md) | Pseudo-problem, not actually parsed from an application/problem+json response. This problem is emitted if the server response is in an unexpected format and the HTTP status is in the Client Error range (400-499).`class UnexpectedContent : Client, `[`UnexpectedContentProblem`](../../-unexpected-content-problem/index.md) |
| [Unknown](-unknown/index.md) | [Client](./index.md) problem with an unrecognized type.`class Unknown : Client, `[`UnknownProblem`](../../-unknown-problem/index.md) |
