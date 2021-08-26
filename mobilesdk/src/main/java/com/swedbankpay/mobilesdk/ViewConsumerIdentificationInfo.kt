package com.swedbankpay.mobilesdk

/**
 * Data required to show the checkin view.
 *
 * If you provide a custom [Configuration],
 * you must get the relevant data from your services
 * and return a ViewConsumerIdentificationInfo object
 * in your [Configuration.postConsumers] method.
 */
data class ViewConsumerIdentificationInfo(
    /**
     * The url to use as the [android.webkit.WebView] page url
     * when showing the checkin UI. If `null`, defaults to
     * `about:blank`, as [documented][android.webkit.WebView.loadDataWithBaseURL].
     */
    val webViewBaseUrl: String? = null,

    /**
     * The `view-consumer-identification` link from Swedbank Pay.
     */
    val viewConsumerIdentification: String
)
