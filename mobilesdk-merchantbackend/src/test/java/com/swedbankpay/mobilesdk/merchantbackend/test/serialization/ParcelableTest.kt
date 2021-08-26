package com.swedbankpay.mobilesdk.merchantbackend.test.serialization

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.JsonObject
import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendProblem
import com.swedbankpay.mobilesdk.test.testParcelable
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Tests for Parcelable implementations
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
class ParcelableTest {
    /**
     * Check that MerchantBackendProblem parcelizes correctly
     */
    @Test
    fun testMerchantBackendProblem() {
        testParcelable(MerchantBackendProblem.Client.SwedbankPay.NotFound(JsonObject().apply {
            addProperty("type", "https://api.payex.com/psp/errordetail/notfound")
            addProperty("test", "data")
        }))
    }
}