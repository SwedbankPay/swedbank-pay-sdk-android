[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [ConfigurationCompat](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`ConfigurationCompat()`

Java compatibility wrapper for [Configuration](../-configuration/index.md).

For each callback defined in [Configuration](../-configuration/index.md), this class
contains a corresponding callback but without the suspend modifier.
The suspending methods of [Configuration](../-configuration/index.md) invoke the corresponding
regular methods using the
[IO Dispatcher](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-i-o.html).
This means your callbacks run in a background thread, so be careful with synchronization.

