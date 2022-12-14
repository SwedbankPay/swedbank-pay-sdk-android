package com.swedbankpay.mobilesdk.internal

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

internal const val LOG_TAG = "SwedbankPay"

internal fun <T : Any> checkBuilderNotNull(value: T?, fieldName: String) =
    checkNotNull(value) { "$fieldName not set" }


///Handling deprecations by splitting the calls based on API level
internal fun <T: Parcelable> Bundle.getParcelableInternal(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, clazz)
    } else {
        getDeprecatedParcelable(key)
    }
}

@Suppress("DEPRECATION")
internal fun <T: Parcelable> Bundle.getDeprecatedParcelable(key: String): T? {
    return getParcelable(key)
}