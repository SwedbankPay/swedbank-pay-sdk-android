[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentOrderUrls](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`PaymentOrderUrls(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, backendUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, callbackUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, termsOfServiceUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, identifier: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = UUID.randomUUID().toString())`

Creates a set of URLs suitable for use with a Merchant Backend server.

This constructor sets `hostUrls` to contain only `backendUrl` (this coincides with how
[MerchantBackendConfiguration](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-configuration/index.md)
sets the backend URL as [ViewPaymentOrderInfo.webViewBaseUrl](../-view-payment-order-info/web-view-base-url.md), `completeUrl` and `cancelUrl`
to static paths relative to `backendUrl`. The `paymentUrl` will be constructed using the
`backendUrl`, the path defined for the Android Intent Callback URL in the Merchant Backend
specification, the `identifier` argument (by default a random UUID), and the application
package name.

Example:

### Parameters

`context` - an application [Context](https://developer.android.com/reference/android/content/Context.html)

`backendUrl` - the URL of your Merchant Backend

`callbackUrl` - the `callbackUrl` to use, if any

`termsOfServiceUrl` - the `termsOfServiceUrl` to use, if any

`identifier` - an identifier for this payment order. Should be unique within your app. Defaults to a random UUID.`PaymentOrderUrls(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, hostUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, backendUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, callbackUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, termsOfServiceUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, identifier: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = UUID.randomUUID().toString())`

Creates a set of URLs suitable for use with a Merchant Backend server.

This version of the constructor allows you to set the URL used for `hostUrls` separately.
This is not usually needed.

### Parameters

`context` - an application [Context](https://developer.android.com/reference/android/content/Context.html)

`hostUrl` - the URL to place in `hostUrls`

`backendUrl` - the URL of your Merchant Backend

`callbackUrl` - the `callbackUrl` to use, if any

`termsOfServiceUrl` - the `termsOfServiceUrl` to use, if any

`identifier` - an identifier for this payment order. Should be unique within your app. Defaults to a random UUID.`PaymentOrderUrls(hostUrls: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, completeUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cancelUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, paymentUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, callbackUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, termsOfServiceUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)`

A set of URLs relevant to a payment order.

The Mobile SDK places some requirements on these URLs,  different to the web-page case.
See individual properties for discussion.

