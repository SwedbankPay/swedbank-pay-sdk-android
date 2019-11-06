package com.swedbankpay.mobilesdk

import androidx.annotation.WorkerThread
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
    open fun decorateInitiateConsumerSessionCompat(userHeaders: UserHeaders, body: String) {}

    /**
     * Override this method to add custom headers to the POST {paymentorders} request.
     *
     * The default implementation does nothing.
     *
     * @param userHeaders headers added to this will be sent with the request
     * @param body the body of the request
     * @param consumerProfileRef the consumer profile reference used in the request
     * @param merchantData the serialized merchant data used in the request
     */
    open fun decorateCreatePaymentOrderCompat(
        userHeaders: UserHeaders,
        body: String,
        consumerProfileRef: String?,
        merchantData: String?
    ) {}

    /**
     * Override this method to add custom headers to the GET {paymentorder} request.
     *
     * The default implementation does nothing.
     *
     * @param userHeaders headers added to this will be sent with the request
     * @param url the URL being requested
     */
    open fun decorateGetPaymentOrderCompat(userHeaders: UserHeaders, url: String) {}

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
        body: String
    ) = decorateCompat {
        decorateInitiateConsumerSessionCompat(userHeaders, body)
    }

    final override suspend fun decorateCreatePaymentOrder(
        userHeaders: UserHeaders,
        body: String,
        consumerProfileRef: String?,
        merchantData: String?
    ) = decorateCompat {
        decorateCreatePaymentOrderCompat(userHeaders, body, consumerProfileRef, merchantData)
    }

    final override suspend fun decorateGetPaymentOrder(
        userHeaders: UserHeaders,
        url: String
    ) = decorateCompat {
        decorateGetPaymentOrderCompat(userHeaders, url)
    }
}