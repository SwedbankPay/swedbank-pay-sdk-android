[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [InvalidInstrumentException](./index.md)

# InvalidInstrumentException

`class InvalidInstrumentException : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)

Reported as the [com.swedbankpay.mobilesdk.PaymentViewModel.RichState.updateException](../../com.swedbankpay.mobilesdk/-payment-view-model/-rich-state/update-exception.md)
if the instrument was not valid for the payment order.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Reported as the [com.swedbankpay.mobilesdk.PaymentViewModel.RichState.updateException](../../com.swedbankpay.mobilesdk/-payment-view-model/-rich-state/update-exception.md) if the instrument was not valid for the payment order.`InvalidInstrumentException(instrument: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cause: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`?)` |

### Properties

| Name | Summary |
|---|---|
| [instrument](instrument.md) | `val instrument: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
