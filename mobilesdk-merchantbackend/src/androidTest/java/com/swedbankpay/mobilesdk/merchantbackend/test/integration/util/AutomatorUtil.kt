package com.swedbankpay.mobilesdk.merchantbackend.test.integration.util

import android.os.SystemClock
import androidx.test.uiautomator.UiObject
import org.junit.Assert

// UI Automator uses 1000 ms internally when waiting for some condition,
// so we use the same here.
private const val pollInterval = 1000L

internal fun retryUntilTrue(timeout: Long, condition: () -> Boolean): Boolean {
    val start = SystemClock.uptimeMillis()
    var elapsedTime = 0L
    while (elapsedTime <= timeout) {
        if (condition()) return true
        elapsedTime = SystemClock.uptimeMillis() - start
        SystemClock.sleep(pollInterval)
    }
    if (condition()) return true
    return false
}

internal fun waitForOne(timeout: Long, objects: Array<UiObject>, error: String? = null): UiObject? {
    val start = SystemClock.uptimeMillis()
    var elapsedTime = 0L
    while (elapsedTime <= timeout) {
        for (obj in objects) {
            if (obj.waitForExists(pollInterval)) {
                return obj
            }
        }
        elapsedTime = SystemClock.uptimeMillis() - start
    }
    if (error != null) {
        Assert.fail(error)
    }
    return null
}