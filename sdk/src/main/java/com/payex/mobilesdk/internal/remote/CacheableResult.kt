package com.payex.mobilesdk.internal.remote

internal class CacheableResult<T>(
    val value: T,
    private val validUntilMillis: Long?
) {
    private val cachedValueIsValid
        get() = validUntilMillis != null && validUntilMillis >= System.currentTimeMillis()

    val cachedValueIfValid
        get() = value.takeIf { cachedValueIsValid }

    fun isValidLongerThan(other: CacheableResult<T>) =
        validUntilMillis != null && (other.validUntilMillis == null || validUntilMillis > other.validUntilMillis)
}
