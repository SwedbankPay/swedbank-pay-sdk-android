[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [ViewPaymentOrderInfo](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`ViewPaymentOrderInfo(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`)``ViewPaymentOrderInfo(webViewBaseUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, viewPaymentOrder: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, completeUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cancelUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, paymentUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, termsOfServiceUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, instrument: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, validInstruments: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>? = null, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null)`

Data required to show the payment menu.

If you provide a custom [Configuration](../-configuration/index.md),
you must get the relevant data from your services
and return a ViewPaymentOrderInfo object
in your [Configuration.postPaymentorders](../-configuration/post-paymentorders.md) method.

