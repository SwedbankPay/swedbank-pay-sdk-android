[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [Configuration](./index.md)

# Configuration

`class Configuration`

The Swedbank Pay configuration for your application.

You need a Configuration to use [PaymentFragment](../-payment-fragment/index.md).
Obtain a Configuration object from a [Configuration.Builder](-builder/index.md).
In most cases, it is enough to set a
[default Configuration](../-payment-fragment/default-configuration.md).
However, for more advanced situations, you may override [PaymentFragment.getConfiguration](../-payment-fragment/get-configuration.md)
to provide a Configuration dynamically.

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | A builder object for [Configuration](./index.md).`class Builder` |

### Properties

| Name | Summary |
|---|---|
| [backendUrl](backend-url.md) | `val backendUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
