package com.swedbankpay.mobilesdk.merchantbackend

import java.io.Serializable

/**
 * Object detailing the reason for a [SwedbankPayProblem].
 *
 * See [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems].
 */
@Suppress("unused")
data class SwedbankPaySubproblem(
    /**
     * Name of the erroneous part of the request
     */
    val name: String?,
    /**
     * A description of what was wrong
     */
    val description: String?
) : Serializable