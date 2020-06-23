[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentFragment](./index.md)

# PaymentFragment

`open class PaymentFragment : Fragment`

A [Fragment](#) that handles a payment process.

A PaymentFragment is single-shot: It is configured for a single payment
and cannot be reused. You must create a new PaymentFragment for every payment
the user makes.

You need a [Configuration](../-configuration/index.md) object for system-level setup. Obtain one
from a [Configuration.Builder](../-configuration/-builder/index.md). Usually you only need one [Configuration](../-configuration/index.md), which
you can set as [defaultConfiguration](default-configuration.md). For advanced use-cases, override [getConfiguration](get-configuration.md)
instead.

You must set the [arguments](#) of a PaymentFragment before use.
The argument [Bundle](https://developer.android.com/reference/android/os/Bundle.html) is created by [ArgumentsBuilder](-arguments-builder/index.md).
The arguments must contain a [PaymentOrder](../-payment-order/index.md) to create.
See [ArgumentsBuilder](-arguments-builder/index.md) for further options.

You may observe the state of the PaymentFragment via [PaymentViewModel](../-payment-view-model/index.md).
Access the PaymentViewModel through the containing [activity](#):

```
    ViewModelProviders.of(activity).get(PaymentViewModel.class)
```

Optionally, you may specify a custom [ViewModelProvider](#)
key by [ArgumentsBuilder.viewModelProviderKey](-arguments-builder/view-model-provider-key.md),
e.g. if you want to support multiple PaymentFragments in an Activity (not recommended).

After configuring the PaymentFragment, [add](#) it to
your [activity](#). The payment process will begin as soon as the
PaymentFragment is started (i.e. visible to the user).

The containing [activity](#) should observe
[PaymentViewModel.state](../-payment-view-model/state.md); at a minimum it should hide the PaymentFragment when
[PaymentViewModel.State.isFinal](../-payment-view-model/-state/is-final.md) is `true`.

Subclassing notes

The correct functioning of PaymentFragment depends on the argument [Bundle](https://developer.android.com/reference/android/os/Bundle.html) being
created with [ArgumentsBuilder](-arguments-builder/index.md). If your subclass needs custom arguments,
you should add the default arguments to your argument [Bundle](https://developer.android.com/reference/android/os/Bundle.html) by [ArgumentsBuilder.build](-arguments-builder/build.md).

If you override [onCreateView](on-create-view.md), you must call the superclass implementation and add the [View](https://developer.android.com/reference/android/view/View.html)
returned by that method to your layout (or use it as the return value).

If you override [onSaveInstanceState](on-save-instance-state.md), you must call the superclass implementation.

All [Bundle](https://developer.android.com/reference/android/os/Bundle.html) keys used by PaymentFragment are namespaced by a "com.swedbankpay.mobilesdk" prefix,
so they should not conflict with any custom keys.

### Types

| Name | Summary |
|---|---|
| [ArgumentsBuilder](-arguments-builder/index.md) | Builder class for the argument [Bundle](https://developer.android.com/reference/android/os/Bundle.html) used by PaymentFragment.`class ArgumentsBuilder` |

### Annotations

| Name | Summary |
|---|---|
| [DefaultUI](-default-u-i/index.md) | `annotation class DefaultUI` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | A [Fragment](#) that handles a payment process.`PaymentFragment()` |

### Functions

| Name | Summary |
|---|---|
| [getConfiguration](get-configuration.md) | Provides the [Configuration](../-configuration/index.md) for this PaymentFragment.`open fun getConfiguration(): `[`Configuration`](../-configuration/index.md) |
| [onCreate](on-create.md) | `open fun onCreate(savedInstanceState: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onCreateView](on-create-view.md) | `open fun onCreateView(inflater: `[`LayoutInflater`](https://developer.android.com/reference/android/view/LayoutInflater.html)`, container: `[`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html)`?, savedInstanceState: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`?): `[`View`](https://developer.android.com/reference/android/view/View.html)`?` |
| [onSaveInstanceState](on-save-instance-state.md) | `open fun onSaveInstanceState(outState: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onStart](on-start.md) | `open fun onStart(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [ARG_CONSUMER](-a-r-g_-c-o-n-s-u-m-e-r.md) | `const val ARG_CONSUMER: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ARG_ENABLED_DEFAULT_UI](-a-r-g_-e-n-a-b-l-e-d_-d-e-f-a-u-l-t_-u-i.md) | `const val ARG_ENABLED_DEFAULT_UI: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ARG_PAYMENT_ORDER](-a-r-g_-p-a-y-m-e-n-t_-o-r-d-e-r.md) | `const val ARG_PAYMENT_ORDER: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ARG_USE_BROWSER](-a-r-g_-u-s-e_-b-r-o-w-s-e-r.md) | `const val ARG_USE_BROWSER: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ARG_VIEW_MODEL_PROVIDER_KEY](-a-r-g_-v-i-e-w_-m-o-d-e-l_-p-r-o-v-i-d-e-r_-k-e-y.md) | `const val ARG_VIEW_MODEL_PROVIDER_KEY: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [defaultConfiguration](default-configuration.md) | The [Configuration](../-configuration/index.md) to use if [getConfiguration](get-configuration.md) is not overridden.`var defaultConfiguration: `[`Configuration`](../-configuration/index.md)`?` |
| [ERROR_MESSAGE](-e-r-r-o-r_-m-e-s-s-a-g-e.md) | Default UI flag: a less laconic, though a bit technical, error message See [ArgumentsBuilder.setEnabledDefaultUI](-arguments-builder/set-enabled-default-u-i.md)`const val ERROR_MESSAGE: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [RETRY_PROMPT](-r-e-t-r-y_-p-r-o-m-p-t.md) | Default UI flag: a prompt to retry a failed request that can reasonably be retried See [ArgumentsBuilder.setEnabledDefaultUI](-arguments-builder/set-enabled-default-u-i.md)`const val RETRY_PROMPT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [SUCCESS_MESSAGE](-s-u-c-c-e-s-s_-m-e-s-s-a-g-e.md) | Default UI flag: a laconic success message See [ArgumentsBuilder.setEnabledDefaultUI](-arguments-builder/set-enabled-default-u-i.md)`const val SUCCESS_MESSAGE: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
