[sdk](../../../index.md) / [com.swedbankpay.mobilesdk](../../index.md) / [Configuration](../index.md) / [Builder](index.md) / [requestDecorator](./request-decorator.md)

# requestDecorator

`fun requestDecorator(requestDecorator: `[`RequestDecorator`](../../-request-decorator/index.md)`): Builder`

Sets a [RequestDecorator](../../-request-decorator/index.md) that adds custom headers to backend requests.

N.B! This object will can retained for an extended period, generally
the lifetime of the process. Be careful not to inadvertently leak
any resources this way. Be very careful if passing a (non-static)
inner class instance here.

**Return**
this

