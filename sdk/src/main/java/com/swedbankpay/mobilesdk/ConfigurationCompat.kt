package com.swedbankpay.mobilesdk

import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Java compatibility wrapper for [Configuration].
 *
 * For each callback defined in [Configuration], this class
 * contains a corresponding callback but without the suspend modifier.
 * The suspending methods of [Configuration] invoke the corresponding
 * regular methods using the
 * [IO Dispatcher](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-i-o.html).
 * This means your callbacks run in a background thread, so be careful with synchronization.
 */
@Suppress("unused")
abstract class ConfigurationCompat : Configuration() {
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
    @WorkerThread
    abstract fun postConsumersCompat(
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
    @WorkerThread
    open fun shouldRetryAfterPostConsumersExceptionCompat(exception: Exception): Boolean {
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
    @WorkerThread
    abstract fun postPaymentordersCompat(
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
    @WorkerThread
    open fun shouldRetryAfterPostPaymentordersExceptionCompat(exception: Exception): Boolean {
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
    @WorkerThread
    open fun patchUpdatePaymentorderSetinstrumentCompat(
        context: Context,
        paymentOrder: PaymentOrder?,
        userData: Any?,
        viewPaymentOrderInfo: ViewPaymentOrderInfo,
        instrument: String
    ): ViewPaymentOrderInfo = viewPaymentOrderInfo

    final override suspend fun postConsumers(
        context: Context,
        consumer: Consumer?,
        userData: Any?
    ) = requestCompat {
        postConsumersCompat(context, consumer, userData)
    }

    final override suspend fun shouldRetryAfterPostConsumersException(
        exception: Exception
    ) = requestCompat {
        shouldRetryAfterPostConsumersExceptionCompat(exception)
    }

    final override suspend fun postPaymentorders(
        context: Context,
        paymentOrder: PaymentOrder?,
        userData: Any?,
        consumerProfileRef: String?
    ) = requestCompat {
        postPaymentordersCompat(context, paymentOrder, userData, consumerProfileRef)
    }

    final override suspend fun shouldRetryAfterPostPaymentordersException(
        exception: Exception
    ) = requestCompat {
        shouldRetryAfterPostPaymentordersExceptionCompat(exception)
    }

    final override suspend fun patchUpdatePaymentorderSetinstrument(
        context: Context,
        paymentOrder: PaymentOrder?,
        userData: Any?,
        viewPaymentOrderInfo: ViewPaymentOrderInfo,
        instrument: String
    ) = requestCompat {
        patchUpdatePaymentorderSetinstrumentCompat(
            context,
            paymentOrder,
            userData,
            viewPaymentOrderInfo,
            instrument
        )
    }

    private suspend fun <T> requestCompat(f: suspend CoroutineScope.() -> T): T {
        return withContext(Dispatchers.IO, f)
    }
}