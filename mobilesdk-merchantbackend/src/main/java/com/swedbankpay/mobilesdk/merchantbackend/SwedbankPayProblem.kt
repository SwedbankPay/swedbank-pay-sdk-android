package com.swedbankpay.mobilesdk.merchantbackend

/**
 * A Problem defined by the Swedbank Pay backend.
 * [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems]
 */
interface SwedbankPayProblem {
    /**
     * The raw application/problem+json object.
     */
    val raw: String
    /**
     * RFC 7807 default property: a URI reference that identifies the problem type.
     *
     * Defaults to "about:blank" if not present in the JSON.
     */
    val type: String
    /**
     * The HTTP status.
     */
    val status: Int?
    /**
     * Human-readable description of the problem
     */
    val title: String?
    /**
     * Human-readable details about the problem
     */
    val detail: String?
    /**
     * Swedbank Pay internal identifier of the problem. This may be useful in
     * debugging the issue with Swedbank Pay support.
     */
    val instance: String?
    /**
     * Suggested action to take to recover from the error.
     *
     * This is a [String] currently. Please refer to Swedbank Pay for possible values.
     */
    val action: SwedbankPayAction?
    /**
     * Array of problem detail objects
     */
    val problems: List<SwedbankPaySubproblem>
}