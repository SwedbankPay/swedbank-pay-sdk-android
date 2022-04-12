package com.swedbankpay.mobilesdk.merchantbackend

import com.swedbankpay.mobilesdk.Consumer
import com.swedbankpay.mobilesdk.PaymentOrder
import com.swedbankpay.mobilesdk.merchantbackend.RequestDecorator.Companion.withHeaders

/**
 * Callback for adding custom headers to backend requests.
 *
 * For simple use-cases, see the [withHeaders] factory methods.
 *
 * All requests made to the merchant backend will call back to
 * the [decorateAnyRequest] method. This is a good place to add
 * API keys and session tokens and the like. Afterwards each request
 * will call back to its specific decoration method, where you can
 * add request-specific headers if such is relevant to your use-case.
 *
 * The sequence of operations is this:
 *  - SDK prepares a request
 *  - decorateAnyRequest is called
 *  - decorateAnyRequest returns
 *  - decorate<SpecificRequest> is called
 *  - decorate<SpecificRequest> returns
 *  - the request is executed
 *
 * Note that the methods in this class are Kotlin coroutines
 * (suspending functions). This way you can include long-running
 * tasks (e.g. network I/O) in your custom header creation. You must
 * not return from the callback until you have set all your headers;
 * indeed you must not call any methods on the passed UserHeaders object
 * after returning from the method.
 *
 * There is a [Java compatibility class][RequestDecoratorCompat] where the callbacks
 * are regular methods running in a background thread.
 *
 * @constructor
 */
abstract class RequestDecorator {
    @Suppress("unused")
    companion object {
        /**
         * Create a RequestDecorator that attaches the specified headers to all SDK requests.
         *
         * @param namesAndValues the header names and values, alternating
         */
        @JvmStatic
        fun withHeaders(vararg namesAndValues: String): RequestDecorator {
            require(namesAndValues.size % 2 == 0) { "namesAndValues must contain alternating header names and values" }
            return SimpleDecorator(namesAndValues)
        }

        /**
         * Create a RequestDecorator that attaches the specified headers to all SDK requests.
         *
         * @param headers map of header names to corresponding values
         */
        @JvmStatic
        fun withHeaders(headers: Map<String, String>): RequestDecorator {
            val namesAndValues = arrayOfNulls<String>(2 * headers.size)
            var i = 0
            for ((name, value) in headers) {
                namesAndValues[i++] = name
                namesAndValues[i++] = value
            }
            @Suppress("UNCHECKED_CAST")
            return SimpleDecorator(namesAndValues as Array<out String>)
        }

        private class SimpleDecorator(
            private val namesAndValues: Array<out String>
        ) : RequestDecorator() {
            override suspend fun decorateAnyRequest(
                userHeaders: UserHeaders,
                method: String,
                url: String,
                body: String?
            ) {
                for (i in namesAndValues.indices step 2) {
                    val name = namesAndValues[i]
                    val value = namesAndValues[i + 1]
                    userHeaders.add(name, value)
                }
            }
        }
    }

    /**
     * Override this method to add custom headers to all backend requests.
     *
     * @param userHeaders headers added to this will be sent with the request
     * @param method the HTTP method of the request
     * @param url the URL of the request
     * @param body the body of the request, if any
     */
    open suspend fun decorateAnyRequest(userHeaders: UserHeaders, method: String, url: String, body: String?) {}

    /**
     * Override this method to add custom headers to the backend entry point request.
     */
    open suspend fun decorateGetTopLevelResources(userHeaders: UserHeaders) {}

    /**
     * Override this method to add custom headers to the POST {consumers} request.
     *
     * The default implementation does nothing.
     *
     * @param userHeaders headers added to this will be sent with the request
     * @param body the body of the request
     */
    open suspend fun decorateInitiateConsumerSession(
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
    open suspend fun decorateCreatePaymentOrder(
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
    open suspend fun decoratePaymentOrderSetInstrument(
        userHeaders: UserHeaders,
        url: String,
        body: String,
        instrument: String
    ) {}

    open suspend fun decorateExpandRequest(
        userHeaders: UserHeaders,
        url: String,
        body: String
    ) {}
}
