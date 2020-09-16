package com.swedbankpay.mobilesdk

/**
 * Data required to show the payment menu.
 *
 * If you provide a custom [Configuration],
 * you must get the relevant data from your services
 * and return a ViewPaymentOrderInfo object
 * in your [Configuration.postPaymentorders] method.
 */
interface ViewPaymentOrderInfo {
    /**
     * The url to use as the [android.webkit.WebView] page url
     * when showing the checkin UI. If `null`, defaults to
     * `about:blank`, as [documented][android.webkit.WebView.loadDataWithBaseURL].
     *
     * This should match your payment order's `hostUrls`.
     */
    val webViewBaseUrl: String?

    /**
     * The `view-paymentorder` link from Swedbank Pay.
     */
    val viewPaymentOrder: String

    /**
     * The `completeUrl` of the payment order
     */
    val completeUrl: String

    /**
     * The `cancelUrl` of the payment order
     */
    val cancelUrl: String?

    /**
     * The `paymentUrl` of the payment order
     */
    val paymentUrl: String?

    /**
     * The `termsOfServiceUrl` of the payment order
     */
    val termsOfServiceUrl: String?
}
