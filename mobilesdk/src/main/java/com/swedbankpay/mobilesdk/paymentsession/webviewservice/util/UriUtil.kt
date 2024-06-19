package com.swedbankpay.mobilesdk.paymentsession.webviewservice.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
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
            requireNonBrowser(intent, context)
            context.startActivity(intent)
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
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

    private fun requireNonBrowser(intent: Intent, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            intent.addFlags(Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER)

            // FLAG_ACTIVITY_REQUIRE_NON_BROWSER is not respected for about:blank, breaking our
            // tests. So, we check for that ourselves. Such a navigation should never happen in
            // production, so it should be safe to always fail to start an activity here.
            if (
                intent.action == Intent.ACTION_VIEW
                && intent.data.toString() == "about:blank"
            ) {
                throw ActivityNotFoundException()
            }
        } else {
            legacyRequireNonBrowser(intent, context)
        }
    }

    private fun legacyRequireNonBrowser(intent: Intent, context: Context) {
        val scheme = intent.scheme
        if (scheme == "http" || scheme == "https" || scheme == "about") {
            val resolveInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val matchDefaultOnly = PackageManager.ResolveInfoFlags.of((PackageManager.MATCH_DEFAULT_ONLY.toLong()))
                context.packageManager
                    .resolveActivity(intent, matchDefaultOnly)
                    ?: throw ActivityNotFoundException()
            } else {
                context.packageManager
                    .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
                    ?: throw ActivityNotFoundException()
            }

            val matchCategory = resolveInfo.match and IntentFilter.MATCH_CATEGORY_MASK
            // Using "host" match category as a heuristic here.
            // An app that handles http(s) uris for any host is more than likely a browser.
            if (matchCategory < IntentFilter.MATCH_CATEGORY_HOST) {
                throw ActivityNotFoundException()
            }
        }
    }

    fun webViewCanOpen(uri: Uri?) = when (uri?.scheme) {
        // Allow about:blank. This is mostly useful for testing.
        "about" -> uri.schemeSpecificPart == "blank"
        "http", "https" -> true
        else -> false
    }
}