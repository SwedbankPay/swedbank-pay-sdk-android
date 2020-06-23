[sdk](../../../index.md) / [com.swedbankpay.mobilesdk](../../index.md) / [PaymentFragment](../index.md) / [ArgumentsBuilder](index.md) / [viewModelProviderKey](./view-model-provider-key.md)

# viewModelProviderKey

`fun viewModelProviderKey(viewModelKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): ArgumentsBuilder`

Sets the key used on the containing [activity's](#)
[ViewModelProvider](#)
for the [PaymentViewModel](../../-payment-view-model/index.md). This is only useful for special scenarios.

### Parameters

`viewModelKey` - the [androidx.lifecycle.ViewModelProvider](#) key the PaymentFragment uses to find its [PaymentViewModel](../../-payment-view-model/index.md) in the containing [activity](#)