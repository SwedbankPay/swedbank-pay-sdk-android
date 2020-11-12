[sdk](../../../index.md) / [com.swedbankpay.mobilesdk](../../index.md) / [PaymentViewModel](../index.md) / [State](./index.md)

# State

`enum class State`

State of a payment process

### Enum Values

| Name | Summary |
|---|---|
| [IDLE](-i-d-l-e/index.md) | No payment active |
| [IN_PROGRESS](-i-n_-p-r-o-g-r-e-s-s/index.md) | Payment is active and is waiting either for user interaction or backend response |
| [UPDATING_PAYMENT_ORDER](-u-p-d-a-t-i-n-g_-p-a-y-m-e-n-t_-o-r-d-e-r/index.md) | Payment order is being updated (because you called [updatePaymentOrder](../update-payment-order.md)). |
| [COMPLETE](-c-o-m-p-l-e-t-e/index.md) | Payment is complete. You should hide the [PaymentFragment](../../-payment-fragment/index.md). This status does not signal anything of whether the payment was successful. You need to check the status from your servers. |
| [CANCELED](-c-a-n-c-e-l-e-d/index.md) | Payment was canceled by the user. You should hide the [PaymentFragment](../../-payment-fragment/index.md). |
| [RETRYABLE_ERROR](-r-e-t-r-y-a-b-l-e_-e-r-r-o-r/index.md) | Payment is active, but could not proceed. |
| [FAILURE](-f-a-i-l-u-r-e/index.md) | Payment has failed. You should hide the [PaymentFragment](../../-payment-fragment/index.md) and show a failure message. |

### Properties

| Name | Summary |
|---|---|
| [isFinal](is-final.md) | `true` if this is a final state for [PaymentFragment](../../-payment-fragment/index.md), `false` otherwise`abstract val isFinal: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
