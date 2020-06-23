[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentFragment](index.md) / [getConfiguration](./get-configuration.md)

# getConfiguration

`protected open fun getConfiguration(): `[`Configuration`](../-configuration/index.md)

Provides the [Configuration](../-configuration/index.md) for this PaymentFragment.

The default implementation returns the value set in [defaultConfiguration](default-configuration.md),
throwing an exception if it is not set. Override this method to choose
the [Configuration](../-configuration/index.md) dynamically. Note, however, that this method is only called
once for each PaymentFragment instance, namely in the [onCreate](on-create.md) method.
This means that the Configuration of a given PaymentFragment instance
cannot change once set.

