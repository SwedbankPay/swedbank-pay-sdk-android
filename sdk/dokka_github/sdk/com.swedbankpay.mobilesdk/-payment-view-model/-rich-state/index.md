[sdk](../../../index.md) / [com.swedbankpay.mobilesdk](../../index.md) / [PaymentViewModel](../index.md) / [RichState](./index.md)

# RichState

`class RichState`

Contains the state of the payment process and possible associated data.

### Properties

| Name | Summary |
|---|---|
| [ioException](io-exception.md) | If the current state is [RETRYABLE_ERROR](../-state/-r-e-t-r-y-a-b-l-e_-e-r-r-o-r/index.md), and it was caused by am [IOException](https://docs.oracle.com/javase/6/docs/api/java/io/IOException.html), this property contains that exception.`val ioException: `[`IOException`](https://docs.oracle.com/javase/6/docs/api/java/io/IOException.html)`?` |
| [problem](problem.md) | If the current state is [RETRYABLE_ERROR](../-state/-r-e-t-r-y-a-b-l-e_-e-r-r-o-r/index.md) or [FAILURE](../-state/-f-a-i-l-u-r-e/index.md), and it was caused by a problem response, this property contains an object describing the problem.`val problem: `[`Problem`](../../-problem/index.md)`?` |
| [retryableErrorMessage](retryable-error-message.md) | If the current state is [RETRYABLE_ERROR](../-state/-r-e-t-r-y-a-b-l-e_-e-r-r-o-r/index.md), this property contains an error message describing the situation.`val retryableErrorMessage: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [state](state.md) | The state of the payment process.`val state: State` |
| [terminalFailure](terminal-failure.md) | If the current state is [FAILURE](../-state/-f-a-i-l-u-r-e/index.md), and it was caused by an onError callback from the Chekout API, this property contains an object describing the error.`val terminalFailure: `[`TerminalFailure`](../../-terminal-failure/index.md)`?` |
