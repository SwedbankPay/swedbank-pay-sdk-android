package com.swedbankpay.mobilesdk

import android.content.Context

/**
 * The Swedbank Pay configuration for your application.
 *
 * You need a Configuration to use [PaymentFragment].
 * If you want to use a custom way of communicating with your services,
 * you can create a subclass of Configuration.
 * If you wish to use the specified Merchant Backend API,
 * create a
 * [MerchantBackendConfiguration][com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration]
 * using
 * [MerchantBackendConfiguration.Builder][com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration.Builder].
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
     * This is used to create callback url:s for swish payments
     *
     */
     open var packageName: String? = null

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
     * Is used by the native payment flow where we only care about the different urls
     * Your implementation must ultimately make the call to Swedbank Pay API
     * and return a [ViewPaymentOrderInfo] describing the result.
     */
    abstract fun postNativePaymentOrders() : ViewPaymentOrderInfo

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
     * Called by [PaymentFragment] when it needs to update a payment order.
     *
     * If you do not update payment orders after they have been created,
     * you do not need to override this method.
     *
     * @param context an application context
     * @param paymentOrder the [PaymentOrder] object set as the PaymentFragment argument
     * @param userData the user data object set as the PaymentFragment argument
     * @param viewPaymentOrderInfo the current [ViewPaymentOrderInfo] as returned from a call to this or [postPaymentorders]
     * @param updateInfo the updateInfo value from the [PaymentViewModel.updatePaymentOrder] call
     * @return ViewPaymentOrderInfo describing the payment order with the changed instrument
     */
    open suspend fun updatePaymentOrder(
        context: Context,
        paymentOrder: PaymentOrder?,
        userData: Any?,
        viewPaymentOrderInfo: ViewPaymentOrderInfo,
        updateInfo: Any?
    ): ViewPaymentOrderInfo = viewPaymentOrderInfo

    /**
     * Expanding operations is a way to get more detailed information about certain aspects of a payment.
     * Usually expand operations does not interfere in the normal payment flow, and can 
     * because of this be performed outside of UIState-handling by accessing your
     * configuration directly. 
     * 
     * If you do not need to expand anything, you do not need to override this method.
     *
     * @param context an application context
     * @param paymentId the id of the [ViewPaymentOrderInfo] object, of which you want to expand.
     * @param expand the specific aspects of the payment you are interested in
     * @param endpoint the merchant API endpoint to call, defaults to "expand"
     * @return the generic result depending on what information is expanded
     */
    open suspend fun <T: Any> expandOperation(
        context: Context,
        paymentId: String,
        expand: Array<String>,
        endpoint: String,
        entityType: Class<T>
    ): T? = null
}

