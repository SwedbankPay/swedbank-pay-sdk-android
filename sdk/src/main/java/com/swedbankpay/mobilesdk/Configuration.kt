package com.swedbankpay.mobilesdk

import android.content.Context
import kotlin.Exception

/**
 * The Swedbank Pay configuration for your application.
 *
 * You need a Configuration to use [PaymentFragment].
 * If you want to use a custom way of communicating with your services,
 * you can create a subclass of Configuration.
 * If you wish to use the specified Merchant Backend API,
 * create a [MerchantBackendConfiguration] using [MerchantBackendConfiguration.Builder].
 *
 * In most cases, it is enough to set a
 * [default Configuration][PaymentFragment.defaultConfiguration].
 * However, for more advanced situations, you may override [PaymentFragment.getConfiguration]
 * to provide a Configuration dynamically.
 *
 * N.B! Configuration is specified as `suspend` functions, i.e. Kotlin coroutines.
 * As Java does not support these, a [compatibility class][ConfigurationCompat]
 * is provided.
 */
abstract class Configuration {
    /**
     * Called by [PaymentFragment] when it needs to show an error message
     * because an operation failed.
     *
     * You can return null if you have no further details to provide.
     *
     * @param context an application context
     * @param exception the exception that caused the failure
     * @return an error message
     */
    open fun getErrorMessage(
        context: Context,
        exception: Exception
    ): String? {
        return exception.localizedMessage
    }

    /**
     * Called by [PaymentFragment] when it needs to start a consumer identification
     * session. Your implementation must ultimately make the call to Swedbank Pay API
     * and return a [ViewConsumerIdentificationInfo] describing the result.
     *
     * @param context an application context
     * @param consumer the [Consumer] object set as the PaymentFragment argument
     * @param userData the user data object set as the PaymentFragment argument
     * @return ViewConsumerIdentificationInfo describing the consumer identification session
     */
    abstract suspend fun postConsumers(
        context: Context,
        consumer: Consumer?,
        userData: Any?
    ): ViewConsumerIdentificationInfo

    /**
     * Called by [PaymentFragment] to determine whether it should fail or allow
     * retry after it failed to start a consumer identification session.
     *
     * @param exception the exception that caused the failure
     * @return `true` if retry should be allowed, `false` otherwise
     */
    open suspend fun shouldRetryAfterPostConsumersException(exception: Exception): Boolean {
        return exception !is IllegalStateException
    }

    /**
     * Called by [PaymentFragment] when it needs to create a payment order.
     * Your implementation must ultimately make the call to Swedbank Pay API
     * and return a [ViewPaymentOrderInfo] describing the result.
     *
     * @param context an application context
     * @param paymentOrder the [PaymentOrder] object set as the PaymentFragment argument
     * @param userData the user data object set as the PaymentFragment argument
     * @param consumerProfileRef if a checkin was performed first, the `consumerProfileRef` from checkin
     * @return ViewPaymentOrderInfo describing the payment order
     */
    abstract suspend fun postPaymentorders(
        context: Context,
        paymentOrder: PaymentOrder?,
        userData: Any?,
        consumerProfileRef: String?
    ): ViewPaymentOrderInfo

    /**
     * Called by [PaymentFragment] to determine whether it should fail or allow
     * retry after it failed to create the payment order.
     *
     * @param exception the exception that caused the failure
     * @return `true` if retry should be allowed, `false` otherwise
     */
    open suspend fun shouldRetryAfterPostPaymentordersException(exception: Exception): Boolean {
        return exception !is IllegalStateException
    }

    /**
     * Called by [PaymentFragment] when it needs to update the instrument of a payment order.
     *
     * If you do not use instrument mode payments, you do not need to override this method.
     *
     * @param context an application context
     * @param paymentOrder the [PaymentOrder] object set as the PaymentFragment argument
     * @param userData the user data object set as the PaymentFragment argument
     * @param viewPaymentOrderInfo the current [ViewPaymentOrderInfo] as returned from a call to this or [postPaymentorders]
     * @param instrument the instrument to set
     * @return ViewPaymentOrderInfo describing the payment order with the changed instrument
     */
    open suspend fun patchUpdatePaymentorderSetinstrument(
        context: Context,
        paymentOrder: PaymentOrder?,
        userData: Any?,
        viewPaymentOrderInfo: ViewPaymentOrderInfo,
        instrument: String
    ): ViewPaymentOrderInfo = viewPaymentOrderInfo

    /**
     * Called by [PaymentFragment] when it needs to show an instrument name to the user.
     *
     * If you do not use instrument mode payments, you do not need to override this method.
     *
     * @param context an application context
     * @param instrument the payment instrument
     * @return a human-readable name of `instrument`
     */
    open fun getInstrumentDisplayName(
        context: Context,
        instrument: String
    ): String {
        return when (instrument) {
            PaymentInstruments.CREDIT_CARD -> "Card"
            PaymentInstruments.SWISH -> "Swish"
            PaymentInstruments.INVOICE -> "Invoice"
            else -> instrument
        }
    }

    /**
     * Called by [PaymentFragment] when it needs to show an error message
     * because updating the payment instrument failed
     *
     * If you do not use instrument mode payments, you do not need to override this method.
     *
     * @param context an application context
     * @param instrument the payment instrument that failed
     * @param exception the exception that caused the failure
     */
    open fun getUpdateInstrumentFailureMessage(
        context: Context,
        instrument: String,
        exception: Exception
    ): String {
        return context.getString(R.string.swedbankpaysdk_update_instrument_failed)
    }
}

