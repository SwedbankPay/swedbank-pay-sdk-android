package com.payex.mobilesdk

/**
 * Object detailing the reason for a [PayExProblem].
 *
 * See [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems].
 */
@Suppress("unused")
class PayExSubproblem(
    /**
     * Name of the erroneous part of the request
     */
    val name: String?,
    /**
     * A description of what was wrong
     */
    val description: String?
)