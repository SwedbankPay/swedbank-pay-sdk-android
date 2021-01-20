[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [ViewConsumerIdentificationInfo](./index.md)

# ViewConsumerIdentificationInfo

`data class ViewConsumerIdentificationInfo`

Data required to show the checkin view.

If you provide a custom [Configuration](../-configuration/index.md),
you must get the relevant data from your services
and return a ViewConsumerIdentificationInfo object
in your [Configuration.postConsumers](../-configuration/post-consumers.md) method.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Data required to show the checkin view.`ViewConsumerIdentificationInfo(webViewBaseUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, viewConsumerIdentification: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [viewConsumerIdentification](view-consumer-identification.md) | The `view-consumer-identification` link from Swedbank Pay.`val viewConsumerIdentification: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [webViewBaseUrl](web-view-base-url.md) | The url to use as the [android.webkit.WebView](https://developer.android.com/reference/android/webkit/WebView.html) page url when showing the checkin UI. If `null`, defaults to `about:blank`, as [documented](https://developer.android.com/reference/android/webkit/WebView.html#loadDataWithBaseURL(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)).`val webViewBaseUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
