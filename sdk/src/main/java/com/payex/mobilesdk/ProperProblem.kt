package com.payex.mobilesdk

import com.google.gson.JsonObject

/**
 * A Problem parsed from an application/problem+json object.
 */
interface ProperProblem {
    /**
     * The raw application/problem+json object.
     */
    val raw: JsonObject
    /**
     * The HTTP status.
     */
    val status: Int
}