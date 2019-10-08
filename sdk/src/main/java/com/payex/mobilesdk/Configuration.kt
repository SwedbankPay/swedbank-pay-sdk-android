package com.payex.mobilesdk

import android.content.Context
import com.payex.mobilesdk.internal.remote.CacheableResult
import com.payex.mobilesdk.internal.remote.WhitelistedDomain
import com.payex.mobilesdk.internal.remote.json.Link
import com.payex.mobilesdk.internal.remote.json.TopLevelResources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl

/**
 * The PayEx configuration for your application.
 *
 * You need a Configuration to use [PaymentFragment].
 * Obtain a Configuration object from a [Configuration.Builder].
 * In most cases, it is enough to set a
 * [default Configuration][PaymentFragment.defaultConfiguration].
 * However, for more advanced situations, you may override [PaymentFragment.getConfiguration]
 * to provide a Configuration dynamically.
 */
class Configuration private constructor(builder: Builder) {
    /**
     * A builder object for [Configuration].
     *
     * @param backendUrl the URL of your merchant backend
     */
    class Builder(val backendUrl: String) {
        internal var requestDecorator: RequestDecorator? = null
        internal val domainWhitelist = ArrayList<WhitelistedDomain>()

        /**
         * Creates a [Configuration] object using the current values of the Builder.
         * @return a new [Configuration] object with values copied from this Builder
         */
        fun build() = Configuration(this)

        /**
         * Sets a [RequestDecorator] that adds custom headers to backend requests.
         *
         * N.B! This object will can retained for an extended period, generally
         * the lifetime of the process. Be careful not to inadvertently leak
         * any resources this way. Be very careful if passing a (non-static)
         * inner class instance here.
         * @return this
         */
        fun requestDecorator(requestDecorator: RequestDecorator) = apply {
            this.requestDecorator = requestDecorator
        }

        /**
         * Adds a domain to the list of allowed domains.
         *
         * By default, the list contains the domain of the backend URL,
         * including its subdomains. If you wish to change that default,
         * you must call this method for each domain you wish to allow.
         * If you call this method, the default will NOT be used, so you
         * need to add the domain of the backend URL explicitly.
         *
         * @param domain the domain to whitelist
         * @param includeSubdomains if `true`, also adds any subdomains of `domain` to the whitelist
         * @return this
         */
        fun whitelistDomain(domain: String, includeSubdomains: Boolean) = apply {
            domainWhitelist.add(WhitelistedDomain(domain, includeSubdomains))
        }
    }

    internal val rootLink = Link.Root(HttpUrl.get(builder.backendUrl))
    internal val requestDecorator = builder.requestDecorator
    internal val domainWhitelist =
        builder.domainWhitelist.let {
            if (it.isEmpty()) {
                listOf(WhitelistedDomain(rootLink.href.host(), true))
            } else {
                it.toList()
            }
        }

    private var topLevelResources: CacheableResult<TopLevelResources>? = null
    internal suspend fun getTopLevelResources(context: Context): TopLevelResources {
        withContext(Dispatchers.Main) {
            topLevelResources?.cachedValueIfValid
        }?.let {
            return it
        }

        val newInstance = rootLink.get(context, this)
        withContext(Dispatchers.Main) {
            topLevelResources.let {
                if (it == null || newInstance.isValidLongerThan(it)) {
                    topLevelResources = newInstance
                }
            }
        }

        return newInstance.value
    }
}