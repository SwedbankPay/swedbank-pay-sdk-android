package com.swedbankpay.mobilesdk.test

import com.nhaarman.mockitokotlin2.*
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.internal.remote.json.*
import okhttp3.HttpUrl

internal fun mockTopLevelResources(stubConsumers: Boolean, stubPaymentorders: Boolean): TopLevelResources {
    return TopLevelResources().apply {
        if (stubConsumers) {
            consumers = mock {
                onPost(ConsumerSession().apply {
                    operations = operationsWith(
                        "view-consumer-identification",
                        TestConstants.viewConsumerSessionLink
                    )
                })
            }
        }
        if (stubPaymentorders) {
            paymentorders = mock {
                onPost(PaymentOrder().apply {
                    url = Link.PaymentOrder(HttpUrl.get(TestConstants.paymentOrderUrl))
                    operations = operationsWith(
                        "view-paymentorder",
                        TestConstants.viewPaymentorderLink
                    )
                })
            }
        }
    }
}

internal fun KStubbing<Configuration>.onTopLevelResources(topLevelResources: TopLevelResources) {
    onBlocking { getTopLevelResources(any()) } doReturn topLevelResources
}

internal fun KStubbing<Link.Consumers>.onPost(consumerSession: ConsumerSession) {
    onBlocking { post(any(), any(), any()) } doReturn consumerSession
}

internal fun KStubbing<Link.PaymentOrders>.onPost(paymentOrder: PaymentOrder) {
    onBlocking { post(any(), any(), anyOrNull(), anyOrNull()) } doReturn paymentOrder
}

internal fun operationsWith(rel: String, href: String) = Operations().apply {
    add(Operation().apply {
        this.rel = rel
        this.href = href
    })
}
