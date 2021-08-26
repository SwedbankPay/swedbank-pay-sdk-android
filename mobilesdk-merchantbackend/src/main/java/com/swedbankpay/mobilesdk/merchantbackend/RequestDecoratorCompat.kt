package com.swedbankpay.mobilesdk.merchantbackend

import androidx.annotation.WorkerThread
import com.swedbankpay.mobilesdk.Consumer
import com.swedbankpay.mobilesdk.PaymentOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Java compatibility wrapper for [RequestDecorator].
 *
 * For each callback defined in [RequestDecorator], this class
 * contains a corresponding callback but without the suspend modifier.
 * The suspending methods of [RequestDecorator] invoke the corresponding
 * regular methods using the
 * [IO Dispatcher](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-i-o.html).
 * This means your callbacks run in a background thread, so be careful with synchronization.
 */
@WorkerThread
@Suppress("unused")
open class RequestDecoratorCompat : RequestDecorator() {
    /**
     * Override this method to add custom headers to all backend requests.
     *
     * @param userHeaders headers added to this will be sent with the request
     * @param method the HTTP method of the request
     * @param url the URL of the request
     * @param body the body of the request, if any
     */
    open fun decorateAnyRequestCompat(
        userHeaders: UserHeaders,
        method: String,
        url: String,
        body: String?
    ) {}

    /**
     * Override this method to add custom headers to the backend entry point request.
     */
    open fun decorateGetTopLevelResourcesCompat(userHeaders: UserHeaders) {}

    /**
     * Override this method to add custom headers to the POST {consumers} request.
     *
     * The default implementation does nothing.
     *
     * @param userHeaders headers added to this will be sent with the request
     * @param body the body of the request
     */
    open fun decorateInitiateConsumerSessionCompat(
        userHeaders: UserHeaders,
        body: String,
        consumer: Consumer
    ) {}

    /**
     * Override this method to add custom headers to the POST {paymentorders} request.
     *
     * The default implementation does nothing.
     *
     * @param userHeaders headers added to this will be sent with the request
     * @param body the body of the request
     * @param paymentOrder the payment order used to create the request body
     */
    open fun decorateCreatePaymentOrderCompat(
        userHeaders: UserHeaders,
        body: String,
        paymentOrder: PaymentOrder
    ) {}

    /**
     * Override this method to add custom headers to the PATCH {setInstrument} request of a payment order.
     *
     * The default implementation does nothing.
     *
     * @param userHeaders headers added to this will be sent with the request
     * @param url the url of the request
     * @param body the body of the request
     * @param instrument the instrument used to create the request body
     */
    open fun decoratePaymentOrderSetInstrumentCompat(
        userHeaders: UserHeaders,
        url: String,
        body: String,
        instrument: String
    ) {}

    private suspend fun decorateCompat(f: suspend CoroutineScope.() -> Unit) {
        withContext(Dispatchers.IO, f)
    }

    final override suspend fun decorateAnyRequest(
        userHeaders: UserHeaders,
        method: String,
        url: String,
        body: String?
    ) = decorateCompat {
        decorateAnyRequestCompat(userHeaders, method, url, body)
    }

    final override suspend fun decorateGetTopLevelResources(
        userHeaders: UserHeaders
    ) = decorateCompat {
        decorateGetTopLevelResourcesCompat(userHeaders)
    }

    final override suspend fun decorateInitiateConsumerSession(
        userHeaders: UserHeaders,
        body: String,
        consumer: Consumer
    ) = decorateCompat {
        decorateInitiateConsumerSessionCompat(userHeaders, body, consumer)
    }

    final override suspend fun decorateCreatePaymentOrder(
        userHeaders: UserHeaders,
        body: String,
        paymentOrder: PaymentOrder
    ) = decorateCompat {
        decorateCreatePaymentOrderCompat(userHeaders, body, paymentOrder)
    }

    final override suspend fun decoratePaymentOrderSetInstrument(
        userHeaders: UserHeaders,
        url: String,
        body: String,
        instrument: String
    ) = decorateCompat {
        decoratePaymentOrderSetInstrumentCompat(userHeaders, url, body, instrument)
    }
}
