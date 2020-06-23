[sdk](../../../index.md) / [com.swedbankpay.mobilesdk](../../index.md) / [PaymentFragment](../index.md) / [ArgumentsBuilder](./index.md)

# ArgumentsBuilder

`class ArgumentsBuilder`

Builder class for the argument [Bundle](https://developer.android.com/reference/android/os/Bundle.html) used by PaymentFragment.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Builder class for the argument [Bundle](https://developer.android.com/reference/android/os/Bundle.html) used by PaymentFragment.`ArgumentsBuilder()` |

### Functions

| Name | Summary |
|---|---|
| [build](build.md) | Adds the values in this ArgumentsBuilder to a [Bundle](https://developer.android.com/reference/android/os/Bundle.html).`fun build(bundle: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`): `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)<br>Convenience for `build(Bundle())`.`fun build(): `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html) |
| [consumer](consumer.md) | Sets a consumer for this payment.`fun consumer(consumer: `[`Consumer`](../../-consumer/index.md)`?): ArgumentsBuilder` |
| [paymentOrder](payment-order.md) | Sets the payment order to create`fun paymentOrder(paymentOrder: `[`PaymentOrder`](../../-payment-order/index.md)`): ArgumentsBuilder` |
| [setEnabledDefaultUI](set-enabled-default-u-i.md) | Set the enabled default user interfaces.`fun setEnabledDefaultUI(vararg defaultUI: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): ArgumentsBuilder` |
| [useBrowser](use-browser.md) | Sets if the payment flow should open an external browser or continue in WebView.`fun useBrowser(external: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): ArgumentsBuilder` |
| [viewModelProviderKey](view-model-provider-key.md) | Sets the key used on the containing [activity's](#) [ViewModelProvider](#) for the [PaymentViewModel](../../-payment-view-model/index.md). This is only useful for special scenarios.`fun viewModelProviderKey(viewModelKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): ArgumentsBuilder` |
