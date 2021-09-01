package com.swedbankpay.mobilesdk.merchantbackend.test.integration.util

import androidx.test.uiautomator.UiObject
import org.junit.Assert

internal fun UiObject.clickUntilCheckedAndAssert(timeout: Long) {
    Assert.assertTrue(
        "Could not check $selector",
        clickUntilTrue(timeout, ::isChecked)
    )
}

internal fun UiObject.clickUntilFocusedAndAssert(timeout: Long) {
    Assert.assertTrue(
        "Could not focus $selector",
        clickUntilTrue(timeout, ::isFocused)
    )
}

private fun UiObject.clickUntilTrue(timeout: Long, condition: () -> Boolean): Boolean {
    return retryUntilTrue(timeout) {
        click() && condition()
    }
}
