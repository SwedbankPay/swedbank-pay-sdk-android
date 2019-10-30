package com.swedbankpay.mobilesdk.test

import okhttp3.mockwebserver.MockResponse

object MockResponses {
    private fun ok(body: String, contentType: String = "application/json") = MockResponse().apply {
        setResponseCode(200)
        addHeader("Content-Type", contentType)
        setBody(body)
    }

    val getRoot get() = ok("""
        {"consumers":"/consumers", "paymentorders":"/paymentorders"}
    """.trimIndent()).addHeader("Cache-Control","max-age=30000000")

    val postConsumers get() = ok("""
        {"operations":[
        {"rel":"view-consumer-identification","href":"/viewconsumerid"}
        ]}
    """.trimIndent())

    val viewConsumerIdentification get() = ok("""
        (function() {
        function consumer(args) {
            var onConsumerIdentified = args.onConsumerIdentified;
            function open() {
                var event = {consumerProfileRef: 'fake_profile'};
                onConsumerIdentified(event);
            }
            return {open: open};
        }
        var hostedView = {consumer: consumer};
        var payex = {hostedView: hostedView};
        window.payex = payex;
        })();
    """.trimIndent(), "application/javascript")

    val postPaymentOrders get() = ok("""
        {"url":"/paymentorders/1",
        "operations":[
        {"rel":"view-paymentorder","href":"/viewpaymentorder"}
        ]}
    """.trimIndent())
    val viewPaymentOrder get() = ok("""
        (function() {
        function paymentMenu(args) {
            var onPaymentCompleted = args.onPaymentCompleted;
            function open() {
                onPaymentCompleted("");
            }
        }
        var hostedView = {paymentMenu: paymentMenu};
        var payex = {hostedView: hostedView};
        window.payex = payex;
        })();
    """.trimIndent(), "application/javascript")
}