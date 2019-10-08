package com.payex.mobilesdk

import com.google.gson.JsonObject

interface ProperProblem {
    val raw: JsonObject
    val status: Int
}