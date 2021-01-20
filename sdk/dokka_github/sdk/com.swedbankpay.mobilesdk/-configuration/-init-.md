[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [Configuration](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Configuration()`

The Swedbank Pay configuration for your application.

You need a Configuration to use [PaymentFragment](../-payment-fragment/index.md).
If you want to use a custom way of communicating with your services,
you can create a subclass of Configuration.
If you wish to use the specified Merchant Backend API,
create a
[MerchantBackendConfiguration](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-configuration/index.md)
using
[MerchantBackendConfiguration.Builder](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-configuration/-builder/index.md).

In most cases, it is enough to set a
[default Configuration](../-payment-fragment/default-configuration.md).
However, for more advanced situations, you may override [PaymentFragment.getConfiguration](../-payment-fragment/get-configuration.md)
to provide a Configuration dynamically.

N.B! Configuration is specified as `suspend` functions, i.e. Kotlin coroutines.
As Java does not support these, a [compatibility class](../-configuration-compat/index.md)
is provided.

