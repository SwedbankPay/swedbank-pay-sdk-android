[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [ViewConsumerIdentificationInfo](./index.md)

# ViewConsumerIdentificationInfo

`interface ViewConsumerIdentificationInfo`

Data required to show the checkin view.

If you provide a custom [Configuration](../-configuration/index.md),
you must get the relevant data from your services
and return a ViewConsumerIdentificationInfo object
in your [Configuration.postConsumers](../-configuration/post-consumers.md) method.

### Properties

| Name | Summary |
|---|---|
| [viewConsumerIdentification](view-consumer-identification.md) | The `view-consumer-identification` link from Swedbank Pay.`abstract val viewConsumerIdentification: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [webViewBaseUrl](web-view-base-url.md) | The url to use as the [android.webkit.WebView](https://developer.android.com/reference/android/webkit/WebView.html) page url when showing the checkin UI. If `null`, defaults to `about:blank`, as [documented](https://developer.android.com/reference/android/webkit/WebView.html#loadDataWithBaseURL(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)).`abstract val webViewBaseUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
