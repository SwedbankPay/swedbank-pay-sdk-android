package com.swedbankpay.mobilesdk.merchantbackend.test.serialization

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendProblem
import com.swedbankpay.mobilesdk.test.exerciseSerialization
import org.junit.Assert
import org.junit.Test

class SerializableTest {
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