package com.swedbankpay.mobilesdk

import android.content.Context
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.RefreshCallbackUrl
import com.swedbankpay.mobilesdk.internal.checkBuilderNotNull
import com.swedbankpay.mobilesdk.internal.ensureSuffix
import com.swedbankpay.mobilesdk.internal.makeCreator
import java.util.*

data class PaymentOrderUrls(
    @SerializedName("hostUrls") val hostUrls: List<String>,
    @SerializedName("completeUrl") val completeUrl: String,
    @SerializedName("cancelUrl") val cancelUrl: String? = null,
    @SerializedName("paymentUrl") val paymentUrl: String? = null,
    @SerializedName("callbackUrl") val callbackUrl: String? = null,
    @SerializedName("termsOfServiceUrl") val termsOfServiceUrl: String? = null,
    @Transient val paymentToken: String
) : Parcelable {
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = makeCreator(::PaymentOrderUrls)

        private fun getRandomToken() = UUID.randomUUID().toString()

        private val Context.callbackHost
            get() = getString(R.string.swedbankpaysdk_callback_host).also {
                require(it.isNotEmpty()) { """
                String resource swedbankpaysdk_callback_host not overridden.
                Please set this string to the hostname of your callback page server,
                or set the hostUrl(s) explicitly.
            """.trimIndent()
                }
            }

        private val Context.hostUrl get() = "https://${callbackHost}/"

        private fun getCompleteUrl(hostUrl: String) = "${hostUrl.ensureSuffix('/')}complete"
        private fun getCancelUrl(hostUrl: String) = "${hostUrl.ensureSuffix('/')}cancel"

        private val Context.callbackScheme
            get() = getString(R.string.swedbankpaysdk_callback_url_scheme).takeUnless { it.isEmpty() }

        private fun Context.getPaymentUrl(token: String) = callbackScheme?.let { scheme ->
            val encodedToken = Uri.encode(token)
            RefreshCallbackUrl.getCallbackPrefix(this)?.ensureSuffix('/')?.let { prefix ->
                "${prefix}reload?token=${encodedToken}&scheme=${Uri.encode(scheme)}"
            } ?: "$scheme:///reload?token=${encodedToken}"
        }
    }

    @JvmOverloads
    @Suppress("unused")
    constructor(
        context: Context,
        callbackUrl: String? = null,
        termsOfServiceUrl: String? = null,
        paymentToken: String = getRandomToken()
    ) : this(
        context = context,
        hostUrl = context.hostUrl,
        callbackUrl = callbackUrl,
        termsOfServiceUrl = termsOfServiceUrl,
        paymentToken = paymentToken
    )

    constructor(
        context: Context,
        hostUrl: String,
        callbackUrl: String?,
        termsOfServiceUrl: String?,
        paymentToken: String = getRandomToken()
    ) : this(
        hostUrls = listOf(hostUrl),
        completeUrl = getCompleteUrl(hostUrl),
        cancelUrl = getCancelUrl(hostUrl),
        paymentUrl = context.getPaymentUrl(paymentToken),
        callbackUrl = callbackUrl,
        termsOfServiceUrl = termsOfServiceUrl,
        paymentToken = paymentToken
    )

    @Suppress("unused")
    class Builder {
        private var hostUrls: List<String> = emptyList()
        private var completeUrl: String? = null
        private var cancelUrl: String? = null
        private var paymentUrl: String? = null
        private var callbackUrl: String? = null
        private var termsOfServiceUrl: String? = null
        private var paymentToken: String? = null

        @JvmOverloads
        fun fromContext(context: Context, paymentToken: String = getRandomToken()) = apply {
            val hostUrl = context.hostUrl
            hostUrls = listOf(hostUrl)
            completeUrl = getCompleteUrl(hostUrl)
            cancelUrl = getCancelUrl(hostUrl)
            paymentUrl = context.getPaymentUrl(paymentToken)
            this.paymentToken = paymentToken
        }

        fun hostUrls(hostUrls: List<String>) = apply { this.hostUrls = hostUrls }
        fun completeUrl(completeUrl: String) = apply { this.completeUrl = completeUrl }
        fun cancelUrl(cancelUrl: String?) = apply { this.cancelUrl = cancelUrl }
        fun paymentUrl(paymentUrl: String?) = apply { this.paymentUrl = paymentUrl }
        fun callbackUrl(callbackUrl: String?) = apply { this.callbackUrl = callbackUrl }
        fun termsOfServiceUrl(termsOfServiceUrl: String?) = apply { this.termsOfServiceUrl = termsOfServiceUrl }
        fun paymentToken(paymentToken: String?) = apply { this.paymentToken = paymentToken }

        fun build() = PaymentOrderUrls(
            hostUrls = hostUrls,
            completeUrl = checkBuilderNotNull(completeUrl, "completeUrl"),
            cancelUrl = cancelUrl,
            paymentUrl = paymentUrl,
            callbackUrl = callbackUrl,
            termsOfServiceUrl = termsOfServiceUrl,
            paymentToken = checkBuilderNotNull(paymentToken, "paymentToken")
        )
    }

    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.apply {
            writeStringList(hostUrls)
            writeString(completeUrl)
            writeString(cancelUrl)
            writeString(paymentUrl)
            writeString(callbackUrl)
            writeString(termsOfServiceUrl)
            writeString(paymentToken)
        }
    }
    private constructor(parcel: Parcel) : this(
        hostUrls = checkNotNull(parcel.createStringArrayList()),
        completeUrl = checkNotNull(parcel.readString()),
        cancelUrl = parcel.readString(),
        paymentUrl = parcel.readString(),
        callbackUrl = parcel.readString(),
        termsOfServiceUrl = parcel.readString(),
        paymentToken = checkNotNull(parcel.readString())
    )
}
