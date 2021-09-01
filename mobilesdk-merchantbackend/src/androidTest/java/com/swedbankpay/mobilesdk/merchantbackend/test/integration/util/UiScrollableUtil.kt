package com.swedbankpay.mobilesdk.merchantbackend.test.integration.util

import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import org.junit.Assert

private const val fineScrollDeadZonePercentage = 0.4
private const val fineScrollStepsPerScroll = 15
private const val fineScrollMaxSwipes = 150

internal fun UiScrollable.waitAndScrollFullyIntoViewAndAssertExists(child: UiObject, timeout: Long) {
    Assert.assertTrue(
        "Widget not found: ${child.selector}",
        retryUntilTrue(timeout) {
            scrollFullyIntoView(child)
        }
    )
}

private fun UiScrollable.scrollFullyIntoView(obj: UiObject): Boolean {
    if (isChildsBottomVisible(obj)) return true

    scrollToBeginning(maxSearchSwipes)
    if (isChildsBottomVisible(obj)) return true

    val oldSwipeDeadZonePercentage = swipeDeadZonePercentage
    swipeDeadZonePercentage = fineScrollDeadZonePercentage
    val found = scrollFinelyUntilChildsBottomVisible(obj, fineScrollMaxSwipes)
    swipeDeadZonePercentage = oldSwipeDeadZonePercentage
    return found
}

private fun UiScrollable.scrollFinelyUntilChildsBottomVisible(
    child: UiObject,
    maxSwipes: Int
): Boolean {
    repeat(maxSwipes) {
        val scrolled = scrollForward(fineScrollStepsPerScroll)
        if (isChildsBottomVisible(child)) return true
        if (!scrolled) return false
    }
    return false
}

private fun UiScrollable.isChildsBottomVisible(child: UiObject) =
    child.exists() && child.bounds.bottom < bounds.bottom