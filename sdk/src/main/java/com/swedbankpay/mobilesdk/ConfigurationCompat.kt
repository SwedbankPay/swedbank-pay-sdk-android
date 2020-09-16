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

    final override suspend fun postConsumers(
        context: Context,
        consumer: Consumer?,
        userData: Any?
    ) = requestCompat {
        postConsumersCompat(context, consumer, userData)
    }

    final override suspend fun postPaymentorders(
        context: Context,
        paymentOrder: PaymentOrder?,
        userData: Any?,
        consumerProfileRef: String?
    ) = requestCompat {
        postPaymentordersCompat(context, paymentOrder, userData, consumerProfileRef)
    }

    private suspend fun <T> requestCompat(f: suspend CoroutineScope.() -> T): T {
        return withContext(Dispatchers.IO, f)
    }
}