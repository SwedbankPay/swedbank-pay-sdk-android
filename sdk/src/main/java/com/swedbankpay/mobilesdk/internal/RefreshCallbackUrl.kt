package com.swedbankpay.mobilesdk.internal

import android.content.Context
import android.net.Uri
import com.swedbankpay.mobilesdk.R

// Make sure to preserve equals and hashCode if you need to change this to
// a non-data class.
internal data class RefreshCallbackUrl(val token: String) {
    companion object {
        private val Context.callbackHost get() = getString(R.string.swedbankpaysdk_callback_host)
            .takeUnless { it.isEmpty() }

        private val Context.callbackPathPrefix get() = getString(R.string.swedbankpaysdk_callback_path_prefix)
            .takeIf {
                it.startsWith('/') && it.length > 1
            }

        fun getCallbackPrefix(context: Context) = context.run {
            callbackHost?.let { host ->
                callbackPathPrefix?.let { prefix ->
                    "https://$host$prefix"
                }
            }
        }

        fun parse(context: Context, uri: Uri): RefreshCallbackUrl? {
            val pathPrefix = when (uri.scheme) {
                "https" -> context.run {
                    if (uri.host == callbackHost) {
                        callbackPathPrefix?.ensureSuffix('/')
                    } else {
                        null
                    }
                }
                context.getString(R.string.swedbankpaysdk_callback_url_scheme) -> "/"
                else -> null
            }
            return pathPrefix?.let { parse(uri, it) }
        }

        private fun parse(uri: Uri, pathPrefix: String): RefreshCallbackUrl? {
            // N.B. At this time, we only need one type of callback,
            // namely "reload". It is not foreseen that more would be needed,
            // but if they are, then they should be parsed here, and probably this
            // class should become sealed class CallbackUrl or something to that effect.
            return when (uri.path?.substringAfterPrefix(pathPrefix)) {
                "reload" -> parseReloadUri(uri)
                else -> null
            }
        }

        private fun parseReloadUri(uri: Uri): RefreshCallbackUrl? {
            return uri.getQueryParameter("token")?.let(::RefreshCallbackUrl)
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
