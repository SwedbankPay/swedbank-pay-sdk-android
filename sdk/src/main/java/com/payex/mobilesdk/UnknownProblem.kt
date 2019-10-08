package com.payex.mobilesdk

interface UnknownProblem : ProperProblem {
    val type: String?
    val title: String?
    val detail: String?
    val instance: String?
}