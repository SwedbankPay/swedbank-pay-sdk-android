package com.swedbankpay.mobilesdk.nativepayments.util

import android.net.Uri
import com.swedbankpay.mobilesdk.Configuration

internal object SwishUriUtil {

    fun String.toIntentUri(configuration: Configuration): Uri? {
        var swishUri = Uri.parse(this)

        if (swishUri.scheme == "swish") {
            val paymentUrl = Uri.parse(configuration.postNativePaymentOrders().paymentUrl)
            val callbackUrl =
                Uri.Builder()
                    .scheme("intent")
                    .authority(paymentUrl.host)
                    .path(paymentUrl.path)
                    .fragment("Intent;scheme=${paymentUrl.scheme};action=com.swedbankpay.mobilesdk.NATIVE_PAYMENT_CALLBACK;package=${configuration.packageName};end;")

            swishUri = swishUri.addUriParameter("callbackUrl", callbackUrl.toString())

            return swishUri
        }

        return null
    }

    private fun Uri.addUriParameter(key: String, newValue: String): Uri =
        with(buildUpon()) {
            clearQuery()
            queryParameterNames.forEach {
                if (it != key) appendQueryParameter(it, getQueryParameter(it))
            }
            appendQueryParameter(key, newValue)
            build()
        }
}