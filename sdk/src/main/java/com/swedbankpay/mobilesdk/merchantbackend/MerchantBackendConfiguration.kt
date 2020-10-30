package com.swedbankpay.mobilesdk.merchantbackend

import android.content.Context
import com.swedbankpay.mobilesdk.*
import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration.Builder
import com.swedbankpay.mobilesdk.internal.remote.CacheableResult
import com.swedbankpay.mobilesdk.internal.remote.WhitelistedDomain
import com.swedbankpay.mobilesdk.internal.remote.json.Link
import com.swedbankpay.mobilesdk.internal.remote.json.TopLevelResources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.CertificatePinner
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.io.IOException
import kotlin.Exception

/**
 * A [Configuration] class for the Merchant Backend API.
 *
 * Get an instance using [Builder].
 */
class MerchantBackendConfiguration private constructor(builder: Builder) : Configuration() {
    /**
     * The URL of the Merchant Backend.
     *
     * This is the value passed to [Builder].
     */
    val backendUrl get() = rootLink.href.toString()

    internal val rootLink = Link.Root(builder.backendUrl.toHttpUrl())
    internal val certificatePinner = builder.pinnerBuilder?.build()
    internal val requestDecorator = builder.requestDecorator
    internal val domainWhitelist = builder.domainWhitelist.let {
        if (it.isEmpty()) {
            listOf(WhitelistedDomain(rootLink.href.host, true))
        } else {
            it.toList()
        }
    }
    private val enabledInstruments = builder.enabledInstruments ?: mapOf(
        PaymentOrderOperation.PURCHASE to listOf(
            PaymentInstruments.CREDIT_CARD,
            PaymentInstruments.SWISH,
            PaymentInstruments.INVOICE
        ),
        PaymentOrderOperation.VERIFY to listOf(
            PaymentInstruments.CREDIT_CARD,
            PaymentInstruments.INVOICE
        )
    )

    private var topLevelResources: CacheableResult<TopLevelResources>? = null

    override fun getErrorMessage(context: Context, exception: Exception): String? {
        return when (exception) {
            is RequestProblemException ->
                context.getProblemErrorMessage(exception.problem)

            else -> super.getErrorMessage(context, exception)
        }
    }

    override suspend fun postConsumers(
        context: Context,
        consumer: Consumer?,
        userData: Any?
    ): ViewConsumerIdentificationInfo {
        val operations = getTopLevelResources(context)
            .consumers
            .post(context, this, checkNotNull(consumer) {
                "MerchantBackend Configuration requires use of Consumer for checkin"
            })
            .operations
        val viewConsumerIdentification = operations.find("view-consumer-identification")?.href
            ?: throw IOException("Missing required operation")
        return object : ViewConsumerIdentificationInfo {
            override val webViewBaseUrl = backendUrl
            override val viewConsumerIdentification = viewConsumerIdentification
        }
    }

    override suspend fun shouldRetryAfterPostConsumersException(exception: Exception): Boolean {
        return when (exception) {
            is RequestProblemException -> exception.problem.let {
                it is MerchantBackendProblem.Server && it !is MerchantBackendProblem.Server.SwedbankPay.ConfigurationError
            }

            else -> super.shouldRetryAfterPostConsumersException(exception)
        }
    }

    override suspend fun postPaymentorders(
        context: Context,
        paymentOrder: PaymentOrder?,
        userData: Any?,
        consumerProfileRef: String?
    ): ViewPaymentOrderInfo {
        checkNotNull(paymentOrder) {
            "MerchantBackend Configuration requires use of PaymentOrder"
        }
        val paymentOrderOut = consumerProfileRef?.let {
            paymentOrder.copy(
                payer = PaymentOrderPayer(consumerProfileRef = it)
            )
        } ?: paymentOrder
        val paymentOrderIn = getTopLevelResources(context)
            .paymentorders
            .post(context, this, paymentOrderOut)

        val viewPaymentOrder =
            paymentOrderIn.operations.find("view-paymentorder")?.href
                ?: throw IOException("Missing required operation")
        val urls = paymentOrder.urls

        val setInstrument = paymentOrderIn.mobileSDK?.setInstrument
        val validInstruments = setInstrument?.let { _ ->
            enabledInstruments[paymentOrder.operation]
        }
        val instrument = validInstruments?.let { _ ->
            paymentOrder.instrument
        }

        return ViewPaymentOrderInfo(
            webViewBaseUrl = backendUrl,
            viewPaymentOrder = viewPaymentOrder,
            completeUrl = urls.completeUrl,
            cancelUrl = urls.cancelUrl,
            paymentUrl = urls.paymentUrl,
            termsOfServiceUrl = urls.termsOfServiceUrl,
            instrument = instrument,
            validInstruments = validInstruments,
            userData = setInstrument?.href?.toString()
        )
    }

    override suspend fun shouldRetryAfterPostPaymentordersException(exception: Exception): Boolean {
        return when (exception) {
            is RequestProblemException -> exception.problem.let {
                it is MerchantBackendProblem.Server && it !is MerchantBackendProblem.Server.SwedbankPay.ConfigurationError
            }

            else -> super.shouldRetryAfterPostPaymentordersException(exception)
        }
    }

    override suspend fun updatePaymentOrder(
        context: Context,
        paymentOrder: PaymentOrder?,
        userData: Any?,
        viewPaymentOrderInfo: ViewPaymentOrderInfo,
        updateInfo: Any?
    ): ViewPaymentOrderInfo {
        val instrument = requireNotNull(updateInfo as? String) {
            "Unexpected updateInfo $updateInfo (expected String)"
        }
        val linkHref = checkNotNull(viewPaymentOrderInfo.userData as? String) {
            "Payment order is not in instrument mode"
        }
        val link = Link.PaymentOrderSetInstrument(linkHref.toHttpUrl())
        val paymentOrderIn = try {
            link.patch(context, this, instrument)
        } catch (e: RequestProblemException) {
            throw when (e.problem) {
                is MerchantBackendProblem.Client -> InvalidInstrumentException(instrument, e)
                else -> e
            }
        }

        val viewPaymentOrder =
            paymentOrderIn.operations.find("view-paymentorder")?.href
        val setInstrument = paymentOrderIn.mobileSDK?.setInstrument

        return viewPaymentOrderInfo.copy(
            viewPaymentOrder = viewPaymentOrder ?: viewPaymentOrderInfo.viewPaymentOrder,
            instrument = instrument,
            userData = setInstrument?.href?.toString() ?: linkHref
        )
    }

    private suspend fun getTopLevelResources(context: Context): TopLevelResources {
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

    /**
     * A builder object for [MerchantBackendConfiguration].
     *
     * @param backendUrl the URL of your merchant backend
     */
    @Suppress("unused")
    class Builder(val backendUrl: String) {
        internal var pinnerBuilder: CertificatePinner.Builder? = null
        internal var requestDecorator: RequestDecorator? = null
        internal val domainWhitelist = ArrayList<WhitelistedDomain>()
        internal var enabledInstruments: Map<PaymentOrderOperation, List<String>>? = null

        /**
         * Creates a [Configuration] object using the current values of the Builder.
         * @return a new [Configuration] object with values copied from this Builder
         */
        fun build() = MerchantBackendConfiguration(this)

        /**
         * Pins certificates for a hostname pattern.
         *
         * The pattern may contain an asterisk (*) as the left-most
         * part. The asterisk will only match one part of the hostname,
         * so `*.foo.com` will match `bar.foo.com`, but not `baz.bar.foo.com`.
         *
         * The certificates are [HPKP](https://tools.ietf.org/html/rfc7469) SHA-256 hashes.
         *
         * Please see [okhttp](https://square.github.io/okhttp/3.x/okhttp/okhttp3/CertificatePinner.html)
         * documentation for discussion on how to do certificate pinning and
         * its consequences.
         *
         * @param pattern the hostname pattern to pin
         * @param certificates the certificates to require for the pattern
         * @return this
         */
        fun pinCertificates(pattern: String, vararg certificates: String) = apply {
            val pinnerBuilder = pinnerBuilder ?: CertificatePinner.Builder().also {
                pinnerBuilder = it
            }
            pinnerBuilder.add(pattern, *certificates)
        }

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

        /**
         * Set instruments available for instrument mode payments.
         *
         * This API will be removed when we can get this info from the backend instead.
         */
        fun enabledInstruments(enabledInstruments: Map<PaymentOrderOperation, List<String>>) = apply {
            this.enabledInstruments = enabledInstruments
        }
    }
}

private fun Context.getProblemErrorMessage(problem: Problem) = when (problem) {
    is MerchantBackendProblem -> getFriendlyDescription(problem)
    else -> problem.title ?: problem.detail ?: getString(R.string.swedbankpaysdk_problem_unknown)
}

private fun Context.getFriendlyDescription(merchantBackendProblem: MerchantBackendProblem): String {
    val resId = when (merchantBackendProblem) {
        is MerchantBackendProblem.Client.MobileSDK.Unauthorized -> R.string.swedbankpaysdk_problem_unauthorized
        is MerchantBackendProblem.Client.MobileSDK.InvalidRequest -> R.string.swedbankpaysdk_problem_invalid_request
        is MerchantBackendProblem.Client.SwedbankPay.InputError -> R.string.swedbankpaysdk_problem_input_error
        is MerchantBackendProblem.Client.SwedbankPay.Forbidden -> R.string.swedbankpaysdk_problem_forbidden
        is MerchantBackendProblem.Client.SwedbankPay.NotFound -> R.string.swedbankpaysdk_problem_not_found
        is MerchantBackendProblem.Client.Unknown -> R.string.swedbankpaysdk_problem_unknown
        is MerchantBackendProblem.Server.MobileSDK.BackendConnectionTimeout -> R.string.swedbankpaysdk_problem_backend_connection_timeout
        is MerchantBackendProblem.Server.MobileSDK.BackendConnectionFailure -> R.string.swedbankpaysdk_problem_backend_connection_failure
        is MerchantBackendProblem.Server.MobileSDK.InvalidBackendResponse -> R.string.swedbankpaysdk_problem_invalid_backend_response
        is MerchantBackendProblem.Server.SwedbankPay.SystemError -> R.string.swedbankpaysdk_problem_system_error
        is MerchantBackendProblem.Server.SwedbankPay.ConfigurationError -> R.string.swedbankpaysdk_problem_configuration_error
        is MerchantBackendProblem.Server.Unknown -> R.string.swedbankpaysdk_problem_unknown
    }
    return getString(resId)
}