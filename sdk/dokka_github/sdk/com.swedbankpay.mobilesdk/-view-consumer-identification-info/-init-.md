[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [ViewConsumerIdentificationInfo](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`ViewConsumerIdentificationInfo(webViewBaseUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, viewConsumerIdentification: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)`

Data required to show the checkin view.

If you provide a custom [Configuration](../-configuration/index.md),
you must get the relevant data from your services
and return a ViewConsumerIdentificationInfo object
in your [Configuration.postConsumers](../-configuration/post-consumers.md) method.

