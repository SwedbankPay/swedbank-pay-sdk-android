package com.swedbankpay.mobilesdk

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.internal.checkBuilderNotNull
import com.swedbankpay.mobilesdk.internal.makeCreator
import okhttp3.HttpUrl
import java.util.*

data class PaymentOrderUrls(
    @SerializedName("hostUrls") val hostUrls: List<String>,
    @SerializedName("completeUrl") val completeUrl: String,
    @SerializedName("cancelUrl") val cancelUrl: String? = null,
    @SerializedName("paymentUrl") val paymentUrl: String? = null,
    @SerializedName("callbackUrl") val callbackUrl: String? = null,
    @SerializedName("termsOfServiceUrl") val termsOfServiceUrl: String? = null//,
) : Parcelable {
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = makeCreator(::PaymentOrderUrls)

        private fun buildCompleteUrl(backendUrl: HttpUrl) = checkNotNull(backendUrl.newBuilder("complete")).toString()
        private fun buildCancelUrl(backendUrl: HttpUrl) = backendUrl.newBuilder("cancel")?.toString()
        private fun buildPaymentUrl(context: Context, backendUrl: HttpUrl, id: String) =
            backendUrl.newBuilder("sdk-callback/android-intent")
                ?.addQueryParameter("package", context.packageName)
                ?.addQueryParameter("id", id)
                ?.toString()
    }

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
        backendUrl = HttpUrl.get(backendUrl),
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
            val backendHttpUrl = HttpUrl.get(backendUrl)
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

    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.apply {
            writeStringList(hostUrls)
            writeString(completeUrl)
            writeString(cancelUrl)
            writeString(paymentUrl)
            writeString(callbackUrl)
            writeString(termsOfServiceUrl)
        }
    }
    private constructor(parcel: Parcel) : this(
        hostUrls = checkNotNull(parcel.createStringArrayList()),
        completeUrl = checkNotNull(parcel.readString()),
        cancelUrl = parcel.readString(),
        paymentUrl = parcel.readString(),
        callbackUrl = parcel.readString(),
        termsOfServiceUrl = parcel.readString()
    )
}
