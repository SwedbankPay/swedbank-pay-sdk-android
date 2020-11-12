[sdk](../../../index.md) / [com.swedbankpay.mobilesdk](../../index.md) / [PaymentViewModel](../index.md) / [RichState](./index.md)

# RichState

`class RichState`

Contains the state of the payment process and possible associated data.

### Properties

| Name | Summary |
|---|---|
| [exception](exception.md) | If the current state is [RETRYABLE_ERROR](../-state/-r-e-t-r-y-a-b-l-e_-e-r-r-o-r/index.md), or [FAILURE](../-state/-f-a-i-l-u-r-e/index.md) caused by an [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html), this property contains that exception.`val exception: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`?` |
| [retryableErrorMessage](retryable-error-message.md) | If the current state is [RETRYABLE_ERROR](../-state/-r-e-t-r-y-a-b-l-e_-e-r-r-o-r/index.md), this property contains an error message describing the situation.`val retryableErrorMessage: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [state](state.md) | The state of the payment process.`val state: State` |
| [terminalFailure](terminal-failure.md) | If the current state is [FAILURE](../-state/-f-a-i-l-u-r-e/index.md), and it was caused by an onError callback from the Chekout API, this property contains an object describing the error.`val terminalFailure: `[`TerminalFailure`](../../-terminal-failure/index.md)`?` |
| [updateException](update-exception.md) | If the current state is [IN_PROGRESS](../-state/-i-n_-p-r-o-g-r-e-s-s/index.md), and an attempt to update the payment order failed, the cause of the failure.`val updateException: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`?` |
| [viewPaymentOrderInfo](view-payment-order-info.md) | If the current state is [IN_PROGRESS](../-state/-i-n_-p-r-o-g-r-e-s-s/index.md) or [UPDATING_PAYMENT_ORDER](../-state/-u-p-d-a-t-i-n-g_-p-a-y-m-e-n-t_-o-r-d-e-r/index.md), the [ViewPaymentOrderInfo](../../-view-payment-order-info/index.md) object describing the current payment order. If the state is `UPDATING_PAYMENT_ORDER`, this is the last-known `ViewPaymentOrderInfo`.`val viewPaymentOrderInfo: `[`ViewPaymentOrderInfo`](../../-view-payment-order-info/index.md)`?` |
