package com.swedbankpay.mobilesdk.test.integration.util

import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable

private fun UiScrollable.isChildsBottomVisible(child: UiObject) =
    child.exists() && child.bounds.bottom < bounds.bottom

internal fun UiScrollable.scrollFullyIntoView(obj: UiObject): Boolean {
    if (isChildsBottomVisible(obj)) return true

    val maxSwipes = maxSearchSwipes
    scrollToBeginning(maxSwipes)
    if (isChildsBottomVisible(obj)) return true

    repeat(maxSwipes) {
        val scrolled = scrollForward()
        if (isChildsBottomVisible(obj)) return true
        if (!scrolled) return false
    }

    return false
}
