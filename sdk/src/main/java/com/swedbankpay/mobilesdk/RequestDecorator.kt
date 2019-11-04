package com.swedbankpay.mobilesdk

/**
 * Callback for adding custom headers to backend requests.
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
    open suspend fun decorateInitiateConsumerSession(userHeaders: UserHeaders, body: String) {}

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
    open suspend fun decorateCreatePaymentOrder(
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
    open suspend fun decorateGetPaymentOrder(userHeaders: UserHeaders, url: String) {}
}
