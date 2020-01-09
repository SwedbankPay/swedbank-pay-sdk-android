package com.swedbankpay.mobilesdk.internal

import android.content.Context
import android.net.Uri
import com.swedbankpay.mobilesdk.R

// Make sure to preserve equals and hashCode if you need to change this to
// a non-data class.
internal data class CallbackUrl(val paymentUrl: String) {
    companion object {
        fun getPrefix(context: Context): String? {
            val callbackScheme = context.getString(R.string.swedbankpaysdk_callback_url_scheme)
            return if (callbackScheme.isNotEmpty()) {
                // If we don't have a host here, some overly-smart
                // part of the system causes an additional slash to be
                // added to our callback urls. The host is arbitrary,
                // but using the package name for extra uniqueness.
                "$callbackScheme://${context.packageName}/"
            } else {
                null
            }
        }

        fun parse(context: Context, uri: Uri): CallbackUrl? {
            val prefix = getPrefix(context)?.ensureSuffix('/')
            val callbackString = prefix?.let(uri.toString()::substringAfterPrefix)
            val callbackUri = callbackString?.let(Uri::parse)
            return when (callbackUri?.path) {
                "reload" -> parseReloadUri(callbackUri)
                else -> null
            }
        }

        private fun parseReloadUri(uri: Uri): CallbackUrl? {
            return uri.getQueryParameter("url")?.let(::CallbackUrl)
        }
    }
}

private fun String.ensureSuffix(suffix: Char): String {
    return if (endsWith(suffix)) {
        this
    } else {
        "$this$suffix"
    }
}

private fun String.substringAfterPrefix(prefix: String): String? {
    return if (startsWith(prefix)) {
        substring(prefix.length)
    } else {
        null
    }
}
