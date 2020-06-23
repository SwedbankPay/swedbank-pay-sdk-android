[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RequestDecorator](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`RequestDecorator()`

Callback for adding custom headers to backend requests.

All requests made to the merchant backend will call back to
the [decorateAnyRequest](decorate-any-request.md) method. This is a good place to add
API keys and session tokens and the like. Afterwards each request
will call back to its specific decoration method, where you can
add request-specific headers if such is relevant to your use-case.

The sequence of operations is this:

* SDK prepares a request
* decorateAnyRequest is called
* decorateAnyRequest returns
* decorate is called
* decorate returns
* the request is executed

Note that the methods in this class are Kotlin coroutines
(suspending functions). This way you can include long-running
tasks (e.g. network I/O) in your custom header creation. You must
not return from the callback until you have set all your headers;
indeed you must not call any methods on the passed UserHeaders object
after returning from the method.

There is a [Java compatibility class](../-request-decorator-compat/index.md) where the callbacks
are regular methods running in a background thread.

