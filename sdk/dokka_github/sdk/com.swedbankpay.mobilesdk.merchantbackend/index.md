[sdk](../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](./index.md)

## Package com.swedbankpay.mobilesdk.merchantbackend

### Types

| Name | Summary |
|---|---|
| [MerchantBackendConfiguration](-merchant-backend-configuration/index.md) | A [Configuration](../com.swedbankpay.mobilesdk/-configuration/index.md) class for the Merchant Backend API.`class MerchantBackendConfiguration : `[`Configuration`](../com.swedbankpay.mobilesdk/-configuration/index.md) |
| [MerchantBackendProblem](-merchant-backend-problem/index.md) | Base class for any problems encountered in the payment.`sealed class MerchantBackendProblem : `[`Problem`](../com.swedbankpay.mobilesdk/-problem/index.md) |
| [SwedbankPayAction](-swedbank-pay-action.md) | Action to take to correct a problem reported by the Swedbank Pay backend.`typealias SwedbankPayAction = `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [SwedbankPayProblem](-swedbank-pay-problem/index.md) | A Problem defined by the Swedbank Pay backend. [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems](https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems)`interface SwedbankPayProblem` |
| [SwedbankPaySubproblem](-swedbank-pay-subproblem/index.md) | Object detailing the reason for a [SwedbankPayProblem](-swedbank-pay-problem/index.md).`class SwedbankPaySubproblem : `[`Serializable`](https://docs.oracle.com/javase/6/docs/api/java/io/Serializable.html) |

### Exceptions

| Name | Summary |
|---|---|
| [InvalidInstrumentException](-invalid-instrument-exception/index.md) | Reported as the [com.swedbankpay.mobilesdk.PaymentViewModel.RichState.updateException](../com.swedbankpay.mobilesdk/-payment-view-model/-rich-state/update-exception.md) if the instrument was not valid for the payment order.`class InvalidInstrumentException : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html) |
| [RequestProblemException](-request-problem-exception/index.md) | IOException containing an RFC 7807 Problem object describing the error.`class RequestProblemException : `[`IOException`](https://docs.oracle.com/javase/6/docs/api/java/io/IOException.html) |
| [UnexpectedResponseException](-unexpected-response-exception/index.md) | The server returned a response that [MerchantBackendConfiguration](-merchant-backend-configuration/index.md) was not prepared for.`class UnexpectedResponseException : `[`IOException`](https://docs.oracle.com/javase/6/docs/api/java/io/IOException.html) |
