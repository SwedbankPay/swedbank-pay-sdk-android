package com.swedbankpay.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.swedbankpay.mobilesdk.internal.makeCreator
import com.swedbankpay.mobilesdk.internal.readUserData
import com.swedbankpay.mobilesdk.internal.writeUserData
import java.io.Serializable

/**
 * Data required to show the payment menu.
 *
 * If you provide a custom [Configuration],
 * you must get the relevant data from your services
 * and return a ViewPaymentOrderInfo object
 * in your [Configuration.postPaymentorders] method.
 */
data class ViewPaymentOrderInfo(
    /**
     * The url to use as the [android.webkit.WebView] page url
     * when showing the checkin UI. If `null`, defaults to
     * `about:blank`, as [documented][android.webkit.WebView.loadDataWithBaseURL].
     *
     * This should match your payment order's `hostUrls`.
     */
    val webViewBaseUrl: String? = null,

    /**
     * The `view-paymentorder` link from Swedbank Pay.
     */
    val viewPaymentOrder: String,

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

    /**
     * Any [Parcelable] or [Serializable] (`String` is fine) object you may need
     * for your [Configuration].
     *
     * See [Configuration.patchUpdatePaymentorderSetinstrument]; you will receive this
     * `ViewPaymentOrderInfo` object there.
     */
    val userData: Any? = null
): Parcelable {
    companion object {
        @JvmField
        val CREATOR = makeCreator(::ViewPaymentOrderInfo)
    }

    init {
        require(userData == null || userData is Parcelable || userData is Serializable) {
            "userData must be Parcelable or Serializable"
        }
    }

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(webViewBaseUrl)
        parcel.writeString(viewPaymentOrder)
        parcel.writeString(completeUrl)
        parcel.writeString(cancelUrl)
        parcel.writeString(paymentUrl)
        parcel.writeString(termsOfServiceUrl)
        parcel.writeString(instrument)
        parcel.writeStringList(availableInstruments)
        parcel.writeUserData(userData, flags)
    }

    constructor(parcel: Parcel) : this(
        webViewBaseUrl = parcel.readString(),
        viewPaymentOrder = checkNotNull(parcel.readString()),
        completeUrl = checkNotNull(parcel.readString()),
        cancelUrl = parcel.readString(),
        paymentUrl = parcel.readString(),
        termsOfServiceUrl = parcel.readString(),
        instrument = parcel.readString(),
        availableInstruments = parcel.createStringArrayList(),
        userData = parcel.readUserData()
    )
}
