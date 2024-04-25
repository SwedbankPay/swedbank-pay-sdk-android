package com.swedbankpay.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Data required to show the payment menu.
 *
 * If you provide a custom [Configuration],
 * you must get the relevant data from your services
 * and return a ViewPaymentOrderInfo object
 * in your [Configuration.postPaymentorders] method.
 */
@Parcelize
data class ViewPaymentOrderInfo(

    /**
     * To refer to the payment and expand its properties we need to store its id.
     */
    val id: String? = null,

    /**
     * The url to use as the [android.webkit.WebView] page url
     * when showing the checkin UI. If `null`, defaults to
     * `about:blank`, as [documented][android.webkit.WebView.loadDataWithBaseURL].
     *
     * This should match your payment order's `hostUrls`.
     */
    val webViewBaseUrl: String? = null,

    /**
     * The `view-paymentorder` link from Swedbank Pay (v2), or the `view-checkout` link (v3).
     */
    val viewPaymentLink: String? = null,

    /**
     * If CheckoutV3 is used
     */
    var isV3: Boolean,

    /**
     * The `completeUrl` of the payment order
     */
    val completeUrl: String,

    /**
     * The `cancelUrl` of the payment order
     */
    val cancelUrl: String? = null,

    /**
     * The `paymentUrl` of the payment order
     */
    val paymentUrl: String?,

    /**
     * The `termsOfServiceUrl` of the payment order
     */
    val termsOfServiceUrl: String? = null,

    /**
     * If the payment order is in instrument mode, the current instrument.
     *
     * The SDK does not use this value, but it may be useful if you have
     * customized instrument selection.
     */
    val instrument: String? = null,

    /**
     * If the payment order is in instrument mode, all the valid instruments for it.
     *
     * The SDK does not use this value, but it may be useful if you have
     * customized instrument selection.
     */
    val availableInstruments: List<String>? = null,

    val paid: PaymentOrderPaid? = null,

    /**
     *
     * Any value you may need for your [Configuration].
     *
     * The value must be one that is valid for [Parcel.writeValue], e.g. [String] or [Parcelable].
     *
     * See [Configuration.updatePaymentOrder]; you will receive this
     * `ViewPaymentOrderInfo` object there.
     */
    val userData: @RawValue Any? = null,

    /**
     * Any available operations that can be applied to this payment.
     */
    val operations: List<Operation>? = null
) : Parcelable {
    @Deprecated("Renamed: viewPaymentLink", ReplaceWith("viewPaymentLink"))
    val viewPaymentOrder: String? get() = viewPaymentLink

}

/**
 * Data specific to the paid entity of a payment. Contains the payment tokens if applicable.
 */
@Parcelize
data class PaymentOrderPaid(
    val payeeReference: String? = null,
    val tokens: List<PaymentOrderToken>? = null
) : Parcelable

/**
 * Data specific to the paid.tokens entity of a payment. Contains the token and related information.
 */
@Parcelize
data class PaymentOrderToken(
    val type: String? = null,
    val token: String? = null,
    val name: String? = null,
    val expiryDate: String? = null
) : Parcelable


fun String.isAlphaNumeric(): Boolean {
    return this.matches("^[a-zA-Z0-9]*$".toRegex())
}