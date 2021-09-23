package com.swedbankpay.mobilesdk.test

import android.os.Bundle

internal fun Bundle.isEqualTo(other: Bundle): Boolean {
    if (this == other) return true
    val keys = keySet()
    if (keys != other.keySet()) return false
    return keys.all {
        val v1 = this[it]
        val v2 = other[it]
        when (v1) {
            is Bundle -> v2 is Bundle && v1.isEqualTo(v2)
            else -> v1 == v2
        }
    }
}
