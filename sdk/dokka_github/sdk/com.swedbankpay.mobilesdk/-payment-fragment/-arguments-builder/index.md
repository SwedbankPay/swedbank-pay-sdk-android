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
| [consumer](consumer.md) | Sets a consumer for this payment. Also enables or disables checkin based on the argument: If `consumer` is `null`, disables checkin; if consumer `consumer` is not `null`, enables checkin. If you wish to override this, call [useCheckin](use-checkin.md) afterwards.`fun consumer(consumer: `[`Consumer`](../../-consumer/index.md)`?): ArgumentsBuilder` |
| [debugIntentUris](debug-intent-uris.md) | Enables or disables verbose error dialogs when Android Intent Uris do not function correctly.`fun debugIntentUris(debugIntentUris: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): ArgumentsBuilder` |
| [paymentOrder](payment-order.md) | Sets the payment order to create`fun paymentOrder(paymentOrder: `[`PaymentOrder`](../../-payment-order/index.md)`?): ArgumentsBuilder` |
| [setEnabledDefaultUI](set-enabled-default-u-i.md) | Set the enabled default user interfaces.`fun setEnabledDefaultUI(vararg defaultUI: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): ArgumentsBuilder` |
| [useBrowser](use-browser.md) | Sets if the payment flow should open an external browser or continue in WebView.`fun useBrowser(external: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): ArgumentsBuilder` |
| [useCheckin](use-checkin.md) | Enables or disables checkin for this payment. Mostly useful for using [userData](user-data.md) and a custom [Configuration](../../-configuration/index.md).`fun useCheckin(useCheckin: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): ArgumentsBuilder` |
| [userData](user-data.md) | Sets custom data for the payment.`fun userData(userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): ArgumentsBuilder` |
| [viewModelProviderKey](view-model-provider-key.md) | Sets the key used on the containing [activity's](#) [ViewModelProvider](#) for the [PaymentViewModel](../../-payment-view-model/index.md). This is only useful for special scenarios.`fun viewModelProviderKey(viewModelKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): ArgumentsBuilder` |
