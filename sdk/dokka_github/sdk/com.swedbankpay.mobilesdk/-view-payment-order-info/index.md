[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [ViewPaymentOrderInfo](./index.md)

# ViewPaymentOrderInfo

`data class ViewPaymentOrderInfo : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)

Data required to show the payment menu.

If you provide a custom [Configuration](../-configuration/index.md),
you must get the relevant data from your services
and return a ViewPaymentOrderInfo object
in your [Configuration.postPaymentorders](../-configuration/post-paymentorders.md) method.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ViewPaymentOrderInfo(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`)`<br>Data required to show the payment menu.`ViewPaymentOrderInfo(webViewBaseUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, viewPaymentOrder: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, completeUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cancelUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, paymentUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, termsOfServiceUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, instrument: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, validInstruments: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>? = null, userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [cancelUrl](cancel-url.md) | The `cancelUrl` of the payment order`val cancelUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [completeUrl](complete-url.md) | The `completeUrl` of the payment order`val completeUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [instrument](instrument.md) | If the payment order is in instrument mode, the current instrument.`val instrument: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [paymentUrl](payment-url.md) | The `paymentUrl` of the payment order`val paymentUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [termsOfServiceUrl](terms-of-service-url.md) | The `termsOfServiceUrl` of the payment order`val termsOfServiceUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [userData](user-data.md) | Any [Parcelable](https://developer.android.com/reference/android/os/Parcelable.html) or [Serializable](https://docs.oracle.com/javase/6/docs/api/java/io/Serializable.html) (`String` is fine) object you may need for your [Configuration](../-configuration/index.md).`val userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
| [validInstruments](valid-instruments.md) | If the payment order is in instrument mode, all the valid instruments for it.`val validInstruments: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` |
| [viewPaymentOrder](view-payment-order.md) | The `view-paymentorder` link from Swedbank Pay.`val viewPaymentOrder: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [webViewBaseUrl](web-view-base-url.md) | The url to use as the [android.webkit.WebView](https://developer.android.com/reference/android/webkit/WebView.html) page url when showing the checkin UI. If `null`, defaults to `about:blank`, as [documented](https://developer.android.com/reference/android/webkit/WebView.html#loadDataWithBaseURL(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)).`val webViewBaseUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](write-to-parcel.md) | `fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`ViewPaymentOrderInfo`](./index.md)`>` |
