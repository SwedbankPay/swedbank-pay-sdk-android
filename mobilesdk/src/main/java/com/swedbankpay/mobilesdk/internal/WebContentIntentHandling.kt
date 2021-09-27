package com.swedbankpay.mobilesdk.internal

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import java.net.URISyntaxException

private val INTENT_URI_FLAGS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
    Intent.URI_ANDROID_APP_SCHEME or Intent.URI_INTENT_SCHEME
} else {
    Intent.URI_INTENT_SCHEME
}

internal fun InternalPaymentViewModel.attemptHandleByExternalApp(uri: String): Boolean {
    val intent = try {
        getExternalAppIntent(uri)
    } catch (_: URISyntaxException) {
        return false
    }

    return try {
        if (!useExternalBrowser) {
            requireNonBrowser(intent)
        }
        getApplication<Application>().startActivity(intent)
        true
    } catch (_: ActivityNotFoundException) {
        attemptOpenIntentFallbackUrl(uri, intent)
    }
}

private fun InternalPaymentViewModel.attemptOpenIntentFallbackUrl(uri: String, intent: Intent): Boolean {
    if (uri.startsWith("intent:")) {
        intent.extras?.getString("browser_fallback_url")?.let {
            try {
                val fallbackIntent = getExternalAppIntent(it)
                getApplication<Application>().startActivity(fallbackIntent)
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

private fun InternalPaymentViewModel.requireNonBrowser(intent: Intent) {
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
        legacyRequireNonBrowser(intent)
    }
}

private fun InternalPaymentViewModel.legacyRequireNonBrowser(intent: Intent) {
    val scheme = intent.scheme
    if (scheme == "http" || scheme == "https" || scheme == "about") {
        val resolveInfo = getApplication<Application>().packageManager
            .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            ?: throw ActivityNotFoundException()
        val matchCategory = resolveInfo.match and IntentFilter.MATCH_CATEGORY_MASK
        // Using "host" match category as a heuristic here.
        // An app that handles http(s) uris for any host is more than likely a browser.
        if (matchCategory < IntentFilter.MATCH_CATEGORY_HOST) {
            throw ActivityNotFoundException()
        }
    }
}
