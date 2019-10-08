package com.payex.mobilesdk

interface UnexpectedContentProblem {
    val status: Int
    val contentType: String?
    val body: String?
}