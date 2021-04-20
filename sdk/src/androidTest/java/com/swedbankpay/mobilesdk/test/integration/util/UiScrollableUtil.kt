package com.swedbankpay.mobilesdk.test.integration.util

import android.os.SystemClock
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import org.junit.Assert

// UI Automator does not contain a "wait until can scroll into view"
// so, we implement one ourselves. This is essentially duplicating logic
// from UiObject; 1000L is taken from there.
private const val waitAndScrollPollInterval = 1000L

private const val fineScrollDeadZonePercentage = 0.4
private const val fineScrollStepsPerScroll = 15
private const val fineScrollMaxSwipes = 150

internal fun UiScrollable.waitAndScrollIntoViewAndAssertExists(child: UiObject, timeout: Long) {
    waitAndScrollIntoViewAndAssertExistsHelper(child, timeout, UiScrollable::scrollIntoView)
}

internal fun UiScrollable.waitAndScrollFullyIntoViewAndAssertExists(child: UiObject, timeout: Long) {
    waitAndScrollIntoViewAndAssertExistsHelper(child, timeout, UiScrollable::scrollFullyIntoView)
}

private fun UiScrollable.waitAndScrollIntoViewAndAssertExistsHelper(
    child: UiObject,
    timeout: Long,
    scroll: UiScrollable.(UiObject) -> Boolean
) {
    val start = SystemClock.uptimeMillis()
    var elapsedTime = 0L
    while (elapsedTime <= timeout) {
        if (scroll(child)) return
        elapsedTime = SystemClock.uptimeMillis() - start
        SystemClock.sleep(waitAndScrollPollInterval)
    }
    Assert.fail("Widget not found: ${child.selector}")
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