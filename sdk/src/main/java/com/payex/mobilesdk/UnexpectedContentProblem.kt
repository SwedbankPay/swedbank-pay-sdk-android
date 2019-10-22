package com.payex.mobilesdk

/**
 * A Pseudo-Problem where the server response was entirely unrecognized.
 */
interface UnexpectedContentProblem {
    /**
     * The HTTP status.
     */
    val status: Int
    /**
     * The Content-Type of the response, if any
     */
    val contentType: String?
    /**
     * The entity body of the response, if it had one and it could be read into a String.
     */
    val body: String?
}