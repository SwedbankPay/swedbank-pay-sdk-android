package com.swedbankpay.mobilesdk.internal

internal const val LOG_TAG = "SwedbankPay"

internal fun <T : Any> checkBuilderNotNull(value: T?, fieldName: String) =
    checkNotNull(value) { "$fieldName not set" }
