package com.payex.mobilesdk.internal.remote

internal class WhitelistedDomain(
    private val domain: String,
    private val includeSubdomains: Boolean
) {
    fun matches(domain: String): Boolean {
        return if (includeSubdomains) {
            domain.endsWith(this.domain)
        } else {
            domain == this.domain
        }
    }
}