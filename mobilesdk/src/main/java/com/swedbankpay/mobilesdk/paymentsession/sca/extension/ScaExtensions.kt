package com.swedbankpay.mobilesdk.paymentsession.sca.extension

import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ExpectationModel
import java.net.URLEncoder

internal fun List<ExpectationModel>.toByteArray(): ByteArray {
    val result = StringBuilder()
    var first = true
    for (e in this) {
        if (first) first = false else result.append("&")
        if (e.value is String) {
            result.append(URLEncoder.encode(e.name, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(e.value, "UTF-8"))
        }
    }
    return result.toString().toByteArray()
}