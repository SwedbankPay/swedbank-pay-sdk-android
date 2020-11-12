[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [ViewConsumerIdentificationInfo](index.md) / [webViewBaseUrl](./web-view-base-url.md)

# webViewBaseUrl

`abstract val webViewBaseUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

The url to use as the [android.webkit.WebView](https://developer.android.com/reference/android/webkit/WebView.html) page url
when showing the checkin UI. If `null`, defaults to
`about:blank`, as [documented](https://developer.android.com/reference/android/webkit/WebView.html#loadDataWithBaseURL(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)).

