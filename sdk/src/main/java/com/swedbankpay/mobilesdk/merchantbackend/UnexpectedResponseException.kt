package com.swedbankpay.mobilesdk.merchantbackend

import java.io.IOException

/**
 * The server returned a response that [MerchantBackendConfiguration]
 * was not prepared for.
 */
class UnexpectedResponseException(
    /**
     * The http status code
     */
    val status: Int,
    /**
     * The Content-Type of `body`, if available
     */
    val contentType: String?,
    /**
     * The response body
     */
    val body: String?,
    cause: Throwable?
) : IOException("Unexpected response from Merchant Backend", cause)