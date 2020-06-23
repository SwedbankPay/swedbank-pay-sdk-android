[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentViewModel](./index.md)

# PaymentViewModel

`class PaymentViewModel : AndroidViewModel`

ViewModel
for communicating with a [PaymentFragment](../-payment-fragment/index.md).

Get a PaymentViewModel from the containing Activity (but see notes at [PaymentFragment](../-payment-fragment/index.md))

```
    ViewModelProviders.of(activity).get(PaymentViewModel::class.java)
```

### Types

| Name | Summary |
|---|---|
| [RichState](-rich-state/index.md) | Contains the state of the payment process and possible associated data.`class RichState` |
| [State](-state/index.md) | State of a payment process`enum class State` |

### Properties

| Name | Summary |
|---|---|
| [richState](rich-state.md) | The current state and associated data of the [PaymentFragment](../-payment-fragment/index.md) corresponding to this [PaymentViewModel](./index.md).`val richState: LiveData<RichState!>` |
| [state](state.md) | The current state of the [PaymentFragment](../-payment-fragment/index.md) corresponding to this [PaymentViewModel](./index.md).`val state: LiveData<State!>` |

### Functions

| Name | Summary |
|---|---|
| [onCleared](on-cleared.md) | `fun onCleared(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [retryPreviousAction](retry-previous-action.md) | If the current state is [RETRYABLE_ERROR](-state/-r-e-t-r-y-a-b-l-e_-e-r-r-o-r/index.md), attempts the previous action again. This call transitions the state to [IN_PROGRESS](-state/-i-n_-p-r-o-g-r-e-s-s/index.md).`fun retryPreviousAction(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
