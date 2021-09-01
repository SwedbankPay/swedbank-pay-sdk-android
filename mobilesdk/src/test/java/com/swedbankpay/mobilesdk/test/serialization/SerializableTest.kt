package com.swedbankpay.mobilesdk.test.serialization

import com.swedbankpay.mobilesdk.Problem
import com.swedbankpay.mobilesdk.test.exerciseSerialization
import org.junit.Test

/**
 * Tests to verify that Serializable classes work correctly
 */
class SerializableTest {
    /**
     * Check that Problem serializes correctly
     */
    @Test
    fun testProblem() {
        exerciseSerialization(
            Problem("""
            {
            "type":"https://example.com/notfound",
            "title":"Not Found",
            "status":404,
            "test":"data"
            }
        """.trimIndent())
        )
    }
}