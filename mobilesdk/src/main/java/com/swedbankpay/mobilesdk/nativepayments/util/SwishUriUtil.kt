package com.swedbankpay.mobilesdk.nativepayments.util

import android.net.Uri
import com.swedbankpay.mobilesdk.ViewPaymentOrderInfo

internal object SwishUriUtil {

    fun String.addCallbackUrl(orderInfo: ViewPaymentOrderInfo): Uri? {
        var swishUri = Uri.parse(this)

        if (swishUri.scheme == "swish") {
            val paymentUrl = orderInfo.paymentUrl ?: return null

            swishUri = swishUri.addUriParameter("callbackurl", paymentUrl)

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