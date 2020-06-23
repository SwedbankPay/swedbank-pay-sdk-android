[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RequestDecoratorCompat](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`RequestDecoratorCompat()`

Java compatibility wrapper for [RequestDecorator](../-request-decorator/index.md).

For each callback defined in [RequestDecorator](../-request-decorator/index.md), this class
contains a corresponding callback but without the suspend modifier.
The suspending methods of [RequestDecorator](../-request-decorator/index.md) invoke the corresponding
regular methods using the
[IO Dispatcher](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-i-o.html).
This means your callbacks run in a background thread, so be careful with synchronization.

