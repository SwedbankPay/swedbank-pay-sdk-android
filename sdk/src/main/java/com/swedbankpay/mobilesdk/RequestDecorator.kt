package com.swedbankpay.mobilesdk

/**
 * Callback for adding custom headers to backend requests.
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
    open fun decorateAnyRequest(userHeaders: UserHeaders, method: String, url: String, body: String?) {}

    /**
     * Override this method to add custom headers to the backend entry point request.
     */
    open fun decorateGetTopLevelResources(userHeaders: UserHeaders) {}

    /**
     * Override this method to add custom headers to the POST {consumers} request.
     *
     * The default implementation does nothing.
     *
     * @param userHeaders headers added to this will be sent with the request
     * @param body the body of the request
     */
    open fun decorateInitiateConsumerSession(userHeaders: UserHeaders, body: String) {}

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
    open fun decorateCreatePaymentOrder(
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
    open fun decorateGetPaymentOrder(userHeaders: UserHeaders, url: String) {}
}
