package com.swedbankpay.mobilesdk.paymentsession.webviewservice.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import java.net.URISyntaxException

object UriUtil {

    private val INTENT_URI_FLAGS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        Intent.URI_ANDROID_APP_SCHEME or Intent.URI_INTENT_SCHEME
    } else {
        Intent.URI_INTENT_SCHEME
    }

    internal fun attemptHandleByExternalApp(uri: String, context: Context): Boolean {
        val intent = try {
            getExternalAppIntent(uri)
        } catch (_: URISyntaxException) {
            return false
        }

        return try {
            context.startActivity(intent)
            true
        } catch (_: ActivityNotFoundException) {
            attemptOpenIntentFallbackUrl(uri, intent, context)
        }
    }

    private fun attemptOpenIntentFallbackUrl(uri: String, intent: Intent, context: Context): Boolean {
        if (uri.startsWith("intent:")) {
            intent.extras?.getString("browser_fallback_url")?.let {
                try {
                    val fallbackIntent = getExternalAppIntent(it)
                    context.startActivity(fallbackIntent)
                    return true
                } catch (_: URISyntaxException) {
                    // do nothing
                } catch (_: ActivityNotFoundException) {
                    // do nothing
                }
            }
        }
        return false
    }

    private fun getExternalAppIntent(uri: String): Intent {
        val intent = Intent.parseUri(uri, INTENT_URI_FLAGS)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                    or Intent.FLAG_ACTIVITY_NEW_TASK
        )
        return intent
    }

    fun webViewCanOpen(uri: Uri?) = when (uri?.scheme) {
        // Allow about:blank. This is mostly useful for testing.
        "about" -> uri.schemeSpecificPart == "blank"
        "http", "https" -> true
        else -> false
    }
}