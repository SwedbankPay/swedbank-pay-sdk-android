package com.swedbankpay.mobilesdk

import okhttp3.Headers
import java.util.*

/**
 * Builder for custom headers.
 *
 * To add headers to a request, override the desired method in [RequestDecorator]
 * and call userHeaders.[add], eg:
 *
 *     override fun decorateCreatePaymentOrder(userHeaders: UserHeaders, body: String, consumerProfileRef: String?, merchantData: String?) {
 *         userHeaders.add("api-key", "secret-api-key")
 *         userHeaders.add("hmac", getHmac(body))
 *     }
 */
@Suppress("unused")
class UserHeaders internal constructor() {
    private val builder = Headers.Builder()

    /**
     * Adds a header line to the request.
     *
     * The header line must be a valid http header line, i.e. it must
     * have the format "Name:Value", and it must not contain non-ASCII
     * characters.
     *
     * @param headerLine the header line to add to the request
     * @throws IllegalArgumentException if headerLine is invalid
     */
    fun add(headerLine: String) = apply { builder.add(headerLine) }

    /**
     * Adds a header to the request.
     *
     * The name and value must not contain non-ASCII characters.
     *
     * @param name the name of the header to add
     * @param value the value of the header
     * @throws IllegalArgumentException if name or value is invalid
     */
    fun add(name: String, value: String) = apply { builder.add(name, value) }

    /**
     * Sets a header in the request.
     *
     * The name and value must not contain non-ASCII characters.
     *
     * If a header with the same name has not been [add]ed or [set], this
     * functions identically to [add]. Otherwise the new value will replace any
     * previous value.
     *
     * @param name the name of the header to add
     * @param value the value of the header
     * @throws IllegalArgumentException if name or value is invalid
     */
    fun set(name: String, value: String) = apply { builder[name] = value }

    /**
     * Adds a header to the request.
     *
     * The name must not contain non-ASCII characters.
     * The value will be formatted as a http date <https://tools.ietf.org/html/rfc7231#section-7.1.1.2>
     *
     * @param name the name of the header to add
     * @param value the value of the header
     * @throws IllegalArgumentException if name or value is invalid
     */
    fun add(name: String, value: Date) = apply { builder.add(name, value) }

    /**
     * Sets a header in the request.
     *
     * The name must not contain non-ASCII characters.
     * The value will be formatted as a http date <https://tools.ietf.org/html/rfc7231#section-7.1.1.2>
     *
     * If a header with the same name has not been [add]ed or [set], this
     * functions identically to [add]. Otherwise the new value will replace any
     * previous value.
     *
     * @param name the name of the header to add
     * @param value the value of the header
     * @throws IllegalArgumentException if name or value is invalid
     */
    fun set(name: String, value: Date) = apply { builder[name] = value }

    /**
     * Adds a header without validating the value.
     *
     * The name still must not contain non-ASCII characters. However,
     * the value can contain arbitrary characters.
     *
     * N.B! The header value will be encoded in UTF-8. Be sure
     * your backend expects this. Non-ASCII characters in headers are
     * NOT valid in HTTP/1.1.
     *
     * @param name the name of the header to add
     * @param value the value of the header
     * @throws IllegalArgumentException if name is invalid
     */
    fun addNonAscii(name: String, value: String) = apply { builder.addUnsafeNonAscii(name, value) }

    internal fun toHeaders() = builder.build()
}