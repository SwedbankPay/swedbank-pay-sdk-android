package com.swedbankpay.mobilesdk.test.serialization

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.swedbankpay.mobilesdk.Problem
import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendProblem
import org.junit.Assert
import org.junit.Test
import java.io.*

/**
 * Tests to verify that Serializable classes work correctly
 */
class SerializableTest {
    private inline fun <reified T : Serializable> exerciseSerialization(serializable: T): T {
        return exerciseSerializationUntyped(serializable) as T
    }
    private fun exerciseSerializationUntyped(serializable: Serializable): Any {
        val serialized = ByteArrayOutputStream().use { baos ->
            ObjectOutputStream(baos).use { oos ->
                oos.writeObject(serializable)
            }
            baos.toByteArray()
        }

        val deserialized = ObjectInputStream(ByteArrayInputStream(serialized)).use {
            it.readObject()
        }

        Assert.assertEquals(serializable, deserialized)
        return deserialized
    }

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

    /**
     * Check that MerchantBackendProblem serializes correctly
     */
    @Test
    fun testMerchantBackendProblem() {
        exerciseSerialization(MerchantBackendProblem.Client.SwedbankPay.NotFound(JsonObject().apply {
            addProperty("type", "https://api.payex.com/psp/errordetail/notfound")
            addProperty("test", "data")
        }))
    }

    /**
     * Check that MerchantBackendProblem with subproblems serializes correctly
     */
    @Test
    fun testMerchantBackendProblemWithSubproblems() {
        val problem = MerchantBackendProblem.Client.SwedbankPay.NotFound(JsonObject().apply {
            addProperty("type", "https://api.payex.com/psp/errordetail/notfound")
            addProperty("test", "data")
            add("problems", JsonArray().apply {
                add(JsonObject().apply {
                    addProperty("name", "name")
                    addProperty("description", "description")
                    addProperty("bogus", "bogus")
                })
            })
        })

        val problem2 = exerciseSerialization(problem)

        val subproblems = problem.problems
        val subproblems2 = problem2.problems
        Assert.assertEquals(subproblems.size, subproblems2.size)
        for ((p1, p2) in subproblems.asSequence() zip subproblems2.asSequence()) {
            Assert.assertEquals(p1, p2)
        }
    }
}