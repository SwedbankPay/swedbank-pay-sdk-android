package com.swedbankpay.mobilesdk

/**
 * Object detailing the reason for a [SwedbankPayProblem].
 *
 * See [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems].
 */
@Suppress("unused")
class SwedbankPaySubproblem(
    /**
     * Name of the erroneous part of the request
     */
    val name: String?,
    /**
     * A description of what was wrong
     */
    val description: String?
)