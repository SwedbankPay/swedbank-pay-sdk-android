package com.swedbankpay.mobilesdk

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.checkBuilderNotNull
import kotlinx.parcelize.Parcelize
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.util.*

/**
 * A set of URLs relevant to a payment order.
 *
 * The Mobile SDK places some requirements on these URLs,  different to the web-page case.
 * See individual properties for discussion.
 */
@Parcelize
data class PaymentOrderUrls(
    /**
     * Array of URLs that are valid for embedding this payment order.
     *
     * The SDK generates the web page that embeds the payment order internally, so it is not really
     * hosted anywhere. However, the WebView will use the value returned in
     * [ViewPaymentOrderInfo.webViewBaseUrl] as the url of that generated page. Therefore,
     * the `webViewBaseUrl` you use should match `hostUrls` here.
     */
    @SerializedName("hostUrls") val hostUrls: List<String>,
    /**
     * The URL that the payment menu will redirect to when the payment is complete.
     *
     * The SDK will capture the navigation before it happens; the `completeUrl` will never be
     * actually loaded in the WebView. Thus, the only requirement for this URL is that is is
     * formally valid.
     */
    @SerializedName("completeUrl") val completeUrl: String,
    /**
     * The URL that the payment menu will redirect to when the payment is canceled.
     *
     * The SDK will capture the navigation before it happens; i.e. this works similarly to how
     * [completeUrl] does.
     */
    @SerializedName("cancelUrl") val cancelUrl: String? = null,
    /**
     * A URL that will be navigated to when the payment menu needs to be reloaded.
     *
     * The `paymentUrl` is used to get back to the payment menu after some third-party process
     * related to the payment is completed. As long as the process stays within the SDK controlled
     * WebView, we can intercept the navigation, like [completeUrl], and reload the payment menu.
     * However, because those processes may involve opening other applications, we must also be
     * prepared for `paymentUrl` being opened in other contexts; in particular, the browser app.
     *
     * When `paymentUrl` is opened in the browser app, what we want to happen is for the browser
     * to open app where the SDK is running, and hand the `paymentUrl` to the SDK. The SDK, of
     * course, provides an interface for the browser to do that, so what remains is that you must
     * set up a web server such that `paymentUrl` is hosted on that server, and when it is opened,
     * it serves an html page that uses the aforementioned interface to open the app using the SDK.
     *
     * To have the web page hosted at `paymentUrl` hand over the url to the SDK, you must use an
     * [intent-scheme URL](https://developer.chrome.com/docs/multidevice/android/intents/)
     * that starts an activity in your application (so make sure to set the `package` correctly),
     * with the action `com.swedbankpay.mobilesdk.VIEW_PAYMENTORDER`, with the intent uri being
     * the `paymentUrl`.
     *
     * Example: if `paymentUrl` is `https://example.com/payment/1234`, and your app's package is
     * `com.example.app`, then `https://example.com/payment/1234` should serve an html page that
     * navigates (you should implement both an immediate redirect and a button the user can press
     * for the navigation) to
     * `intent://example.com/payment/1234#Intent;package=com.example.app;action=com.swedbankpay.mobilesdk.VIEW_PAYMENTORDER;scheme=https`
     *
     * Each `paymentUrl` you create should be unique inside your application.
     */
    @SerializedName("paymentUrl") val paymentUrl: String? = null,
    /**
     * A URL on your server that receives status callbacks related to the payment.
     *
     * The SDK does not interact with this server-to-server URL and as such places no
     * requirements on it.
     */
    @SerializedName("callbackUrl") val callbackUrl: String? = null,
    /**
     * A URL to your Terms of Service.
     *
     * By default, pressing the Terms of Service link will open this URL in a new activity.
     * You may customize this behaviour through [PaymentViewModel.setOnTermsOfServiceClickListener].
     */
    @SerializedName("termsOfServiceUrl") val termsOfServiceUrl: String? = null
) : Parcelable {
    companion object {
        private fun buildCompleteUrl(backendUrl: HttpUrl) = checkNotNull(backendUrl.newBuilder("complete")).toString()
        private fun buildCancelUrl(backendUrl: HttpUrl) = backendUrl.newBuilder("cancel")?.toString()
        private fun buildPaymentUrl(context: Context, backendUrl: HttpUrl, id: String) =
            backendUrl.newBuilder("sdk-callback/android-intent")
                ?.addQueryParameter("package", context.packageName)
                ?.addQueryParameter("id", id)
                ?.toString()
    }

    /**
     * Creates a set of URLs suitable for use with a Merchant Backend server.
     *
     * This constructor sets `hostUrls` to contain only `backendUrl` (this coincides with how
     * [MerchantBackendConfiguration][com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration]
     * sets the backend URL as [ViewPaymentOrderInfo.webViewBaseUrl], `completeUrl` and `cancelUrl`
     * to static paths relative to `backendUrl`. The `paymentUrl` will be constructed using the
     * `backendUrl`, the path defined for the Android Intent Callback URL in the Merchant Backend
     * specification, the `identifier` argument (by default a random UUID), and the application
     * package name.
     *
     * Example:
     *
     * <table>
     *     <tr><td>`hostUrls`</td>   <td>`["https://example.com/"]`</td></tr>
     *     <tr><td>`completeUrl`</td><td>`"https://example.com/complete"`</td></tr>
     *     <tr><td>`cancelUrl`</td>  <td>`"https://example.com/cancel"`</td></tr>
     *     <tr><td>`paymentUrl`</td> <td>`"https://example.com/sdk-callback/android-intent?package=com.example.app&id=1234"`</td></tr>
     * </table>
     *
     * @param context an application [Context]
     * @param backendUrl the URL of your Merchant Backend
     * @param callbackUrl the `callbackUrl` to use, if any
     * @param termsOfServiceUrl the `termsOfServiceUrl` to use, if any
     * @param identifier an identifier for this payment order. Should be unique within your app. Defaults to a random UUID.
     */
    @JvmOverloads
    @Suppress("unused")
    constructor(
        context: Context,
        backendUrl: String,
        callbackUrl: String? = null,
        termsOfServiceUrl: String? = null,
        identifier: String = UUID.randomUUID().toString()
    ) : this(
        context = context,
        hostUrl = backendUrl,
        backendUrl = backendUrl,
        callbackUrl = callbackUrl,
        termsOfServiceUrl = termsOfServiceUrl,
        identifier = identifier
    )

    /**
     * Creates a set of URLs suitable for use with a Merchant Backend server.
     *
     * This version of the constructor allows you to set the URL used for `hostUrls` separately.
     * This is not usually needed.
     *
     * @param context an application [Context]
     * @param hostUrl the URL to place in `hostUrls`
     * @param backendUrl the URL of your Merchant Backend
     * @param callbackUrl the `callbackUrl` to use, if any
     * @param termsOfServiceUrl the `termsOfServiceUrl` to use, if any
     * @param identifier an identifier for this payment order. Should be unique within your app. Defaults to a random UUID.
     */
    constructor(
        context: Context,
        hostUrl: String,
        backendUrl: String,
        callbackUrl: String?,
        termsOfServiceUrl: String?,
        identifier: String = UUID.randomUUID().toString()
    ) : this(
        context = context,
        hostUrl = hostUrl,
        backendUrl = backendUrl.toHttpUrl(),
        callbackUrl = callbackUrl,
        termsOfServiceUrl = termsOfServiceUrl,
        identifier = identifier
    )

    private constructor(
        context: Context,
        hostUrl: String,
        backendUrl: HttpUrl,
        callbackUrl: String?,
        termsOfServiceUrl: String?,
        identifier: String
    ) : this(
        hostUrls = listOf(hostUrl),
        completeUrl = buildCompleteUrl(backendUrl),
        cancelUrl = buildCancelUrl(backendUrl),
        paymentUrl = buildPaymentUrl(context, backendUrl, identifier),
        callbackUrl = callbackUrl,
        termsOfServiceUrl = termsOfServiceUrl
    )

    @Suppress("unused")
    class Builder {
        private var hostUrls: List<String> = emptyList()
        private var completeUrl: String? = null
        private var cancelUrl: String? = null
        private var paymentUrl: String? = null
        private var callbackUrl: String? = null
        private var termsOfServiceUrl: String? = null

        @JvmOverloads
        fun fromContext(context: Context, backendUrl: String, identifier: String = UUID.randomUUID().toString()) = apply {
            val backendHttpUrl = backendUrl.toHttpUrl()
            hostUrls = listOf(backendUrl)
            completeUrl = buildCompleteUrl(backendHttpUrl)
            cancelUrl = buildCancelUrl(backendHttpUrl)
            paymentUrl = buildPaymentUrl(context, backendHttpUrl, identifier)
        }

        fun hostUrls(hostUrls: List<String>) = apply { this.hostUrls = hostUrls }
        fun completeUrl(completeUrl: String) = apply { this.completeUrl = completeUrl }
        fun cancelUrl(cancelUrl: String?) = apply { this.cancelUrl = cancelUrl }
        fun paymentUrl(paymentUrl: String?) = apply { this.paymentUrl = paymentUrl }
        fun callbackUrl(callbackUrl: String?) = apply { this.callbackUrl = callbackUrl }
        fun termsOfServiceUrl(termsOfServiceUrl: String?) = apply { this.termsOfServiceUrl = termsOfServiceUrl }

        fun build() = PaymentOrderUrls(
            hostUrls = hostUrls,
            completeUrl = checkBuilderNotNull(completeUrl, "completeUrl"),
            cancelUrl = cancelUrl,
            paymentUrl = paymentUrl,
            callbackUrl = callbackUrl,
            termsOfServiceUrl = termsOfServiceUrl
        )
    }
}
