[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentFragment](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`PaymentFragment()`

A [Fragment](#) that handles a payment process.

A PaymentFragment is single-shot: It is configured for a single payment
and cannot be reused. You must create a new PaymentFragment for every payment
the user makes.

You need a [Configuration](../-configuration/index.md) object for system-level setup.
Usually you only need one [Configuration](../-configuration/index.md), which
you can set as [defaultConfiguration](default-configuration.md).
For advanced use-cases, override [getConfiguration](get-configuration.md) instead.

You must set the [arguments](#) of a PaymentFragment before use.
The argument [Bundle](https://developer.android.com/reference/android/os/Bundle.html) is easiest to create by [ArgumentsBuilder](-arguments-builder/index.md).
Alternatively, you may prepare the Bundle yourself and set appropriate
values for the ARG_* keys defined in [PaymentFragment.Companion](#).

You may observe the state of the PaymentFragment via [PaymentViewModel](../-payment-view-model/index.md).
Access the PaymentViewModel through the containing [activity](#):

```
    ViewModelProviders.of(activity).get(PaymentViewModel.class)
```

Optionally, you may specify a custom [ViewModelProvider](#)
key by [ArgumentsBuilder.viewModelProviderKey](-arguments-builder/view-model-provider-key.md) or [ARG_VIEW_MODEL_PROVIDER_KEY](-a-r-g_-v-i-e-w_-m-o-d-e-l_-p-r-o-v-i-d-e-r_-k-e-y.md),
e.g. if you want to support multiple PaymentFragments in an Activity (not recommended).

After configuring the PaymentFragment, [add](#) it to
your [activity](#). The payment process will begin as soon as the
PaymentFragment is started (i.e. visible to the user).

The containing [activity](#) should observe
[PaymentViewModel.state](../-payment-view-model/state.md); at a minimum it should hide the PaymentFragment when
[PaymentViewModel.State.isFinal](../-payment-view-model/-state/is-final.md) is `true`.

Subclassing notes

The correct functioning of PaymentFragment depends on the argument [Bundle](https://developer.android.com/reference/android/os/Bundle.html) having
the expected values for the keys defined in [PaymentFragment.Companion](#),
which is easiest to ensure by using [ArgumentsBuilder](-arguments-builder/index.md).
If your subclass needs custom arguments, you should add the default arguments
to your argument [Bundle](https://developer.android.com/reference/android/os/Bundle.html) by [ArgumentsBuilder.build](-arguments-builder/build.md).

If you override [onCreateView](on-create-view.md), you must call the superclass implementation and add the [View](https://developer.android.com/reference/android/view/View.html)
returned by that method to your layout (or use it as the return value).

If you override [onSaveInstanceState](on-save-instance-state.md), you must call the superclass implementation.

All [Bundle](https://developer.android.com/reference/android/os/Bundle.html) keys used by PaymentFragment are namespaced by a "com.swedbankpay.mobilesdk" prefix,
so they should not conflict with any custom keys.

