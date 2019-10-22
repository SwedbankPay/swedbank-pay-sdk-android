package com.payex.mobilesdk

/**
 * A Problem whose [type] was not recognized.
 *
 * Any custom errors you add to your merchant backend will be reported
 * as these. Examine the [type] to identify them. You can access the [raw] json
 * to read any custom fields.
 *
 * See [https://tools.ietf.org/html/rfc7807#section-3.1]
 */
interface UnknownProblem : ProperProblem {
    /**
     * The type of the problem. This is an URI which may or may not be dereferencable.
     * Defaults to `"about:blank"` if the json contained no explicit type.
     */
    val type: String
    /**
     * A short, human-readable description of the problem.
     */
    val title: String?
    /**
     * A detailed, human-readable description
     */
    val detail: String?
    /**
     * Optional unique identifier for this specific problem instance.
     * This is also an URI which may or may not be dereferencable.
     */
    val instance: String?
}