package com.swedbankpay.mobilesdk.nativepayments.util

import android.net.Uri
import com.swedbankpay.mobilesdk.ViewPaymentOrderInfo

internal object UriCallbackUtil {

    fun String.addCallbackUrl(orderInfo: ViewPaymentOrderInfo): Uri? {
        var uri = Uri.parse(this)

        if (uri.scheme == "swish") {
            val paymentUrl = orderInfo.paymentUrl ?: return null

            uri = uri.addUriParameter("callbackurl", paymentUrl)

            return uri
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