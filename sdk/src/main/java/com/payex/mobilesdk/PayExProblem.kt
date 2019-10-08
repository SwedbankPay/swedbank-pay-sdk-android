package com.payex.mobilesdk

interface PayExProblem : ProperProblem {
    val title: String?
    val detail: String?
    val instance: String?
    val action: PayexAction?
    val problems: List<PayExSubproblem>
}