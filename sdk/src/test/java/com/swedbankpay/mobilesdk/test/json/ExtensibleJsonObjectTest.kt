package com.swedbankpay.mobilesdk.test.json

import android.os.Build
import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.internal.BundleTypeAdapterFactory
import com.swedbankpay.mobilesdk.internal.remote.ExtensibleJsonObject
import com.swedbankpay.mobilesdk.test.TestConstants
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Tests for ExtensibleJsonObject
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ExtensibleJsonObjectTest {
    private companion object {
        const val testProperty = "test_property"
        const val testValue = "test_value"
    }

    private val testExtensionProperties = Bundle().apply {
        putString("test_property", "test_value")
    }

    private fun <T : ExtensibleJsonObject> testExtensibleObject(t: T) {
        val json = GsonBuilder()
            .registerTypeAdapterFactory(ExtensibleJsonObject.TypeAdapterFactory)
            .registerTypeAdapterFactory(BundleTypeAdapterFactory)
            .create()
            .toJsonTree(t).asJsonObject

        val extensionProperty = json[testProperty]
        Assert.assertNotNull(extensionProperty)
        Assert.assertTrue(extensionProperty.isJsonPrimitive)
        Assert.assertTrue(extensionProperty.asJsonPrimitive.isString)
        Assert.assertEquals(testValue, extensionProperty.asString)
    }

    /**
     * Check that Consumer.extensionProperties are serialized correctly
     */
    @Test
    fun testConsumer() {
        val consumer = TestConstants.consumer.copy(extensionProperties = testExtensionProperties)
        testExtensibleObject(consumer)
    }

    /**
     * Check that PaymentOrder.extensionProperties are serialized correctly
     */
    @Test
    fun testPaymentOrder() {
        val paymentOrder = TestConstants.paymentOrder.copy(extensionProperties = testExtensionProperties)
        testExtensibleObject(paymentOrder)
    }
}