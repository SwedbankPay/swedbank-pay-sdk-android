package com.swedbankpay.mobilesdk.merchantbackend

import com.swedbankpay.mobilesdk.Problem
import java.io.IOException

private fun formatMessage(problem: Problem): String {
    val message = problem.title ?: problem.detail ?: problem.type
    return "Problem with request: $message}"
}

/**
 * IOException containing an RFC 7807 Problem object
 * describing the error.
 */
class RequestProblemException(
    /**
     * The problem that occurred
     */
    val problem: Problem
) : IOException(formatMessage(problem))