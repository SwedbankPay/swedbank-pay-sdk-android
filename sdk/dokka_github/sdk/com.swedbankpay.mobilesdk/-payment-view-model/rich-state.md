[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentViewModel](index.md) / [richState](./rich-state.md)

# richState

`val richState: LiveData<RichState!>`

The current state and associated data of the [PaymentFragment](../-payment-fragment/index.md) corresponding to this [PaymentViewModel](index.md).

For convenience, this property will retain the last-known state of a PaymentFragment
after it has been removed. When a new PaymentFragment is added to the same Activity,
this property will reflect that PaymentFragment from there on. To support multiple
PaymentFragments in an Activity, see [PaymentFragment.ArgumentsBuilder.viewModelProviderKey](../-payment-fragment/-arguments-builder/view-model-provider-key.md).

Due to the semantics of [Transformations](#), you should be careful if accessing
this value using [LiveData.getValue](#) directly rather than by an [Observer](#).
Specifically, if nothing is observing this property (possibly indirectly, such as through
the [state](state.md) property), then the value will not be updated, and the state may be permanently
lost if the PaymentFragment is removed before adding an observer to this property.

