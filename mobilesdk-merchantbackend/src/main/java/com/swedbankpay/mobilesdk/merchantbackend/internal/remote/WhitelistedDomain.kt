package com.swedbankpay.mobilesdk.merchantbackend.internal.remote

internal class WhitelistedDomain(
    private val domain: String,
    private val includeSubdomains: Boolean
) {
    fun matches(domain: String): Boolean {
        return domain == this.domain
                || (includeSubdomains && domain.endsWith(".${this.domain}"))
    }
}