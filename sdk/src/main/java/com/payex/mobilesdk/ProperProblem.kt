package com.payex.mobilesdk

import com.google.gson.JsonObject

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