package com.swedbankpay.mobilesdk.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.internal.remote.json.*
import okhttp3.HttpUrl

fun <T> observing(liveData: LiveData<T>, f: (Observer<T>) -> Unit) {
    val observer = mock<Observer<T>>()
    liveData.observeForever(observer)
    f(observer)
    liveData.removeObserver(observer)
}

internal fun mockTopLevelResources(consumers: Link.Consumers?, paymentorders: Link.PaymentOrders?): TopLevelResources {
    return TopLevelResources().apply {
        consumers?.let { this.consumers = it }
        paymentorders?.let { this.paymentorders = it }
    }
}

internal fun mockConsumers(): Link.Consumers = mock {
    onPost(ConsumerSession().apply {
        operations = operationsWith(
            "view-consumer-identification",
            TestConstants.viewConsumerSessionLink
        )
    })
}

internal fun mockPaymentOrders(failureReason: String? = null): Link.PaymentOrders {
    val operations = operationsWith(
        "view-paymentorder",
        TestConstants.viewPaymentorderLink
    )

    val href = HttpUrl.get(TestConstants.paymentOrderUrl)
    val url = failureReason?.let {
        val getResult = PaymentOrder().apply {
            this.failureReason = failureReason
            this.operations = operations
        }
        mock<Link.PaymentOrder> {
            onGet(getResult)
        }.also {
            getResult.url = it
        }
    } ?: Link.PaymentOrder(href)

    return mock {
        onPost(PaymentOrder().apply {
            this.url = url
            this.operations = operations
        })
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

internal fun KStubbing<Link.PaymentOrder>.onGet(paymentOrder: PaymentOrder) {
    onBlocking { get(any(), any()) } doReturn paymentOrder
}

internal fun operationsWith(rel: String, href: String) = Operations().apply {
    add(Operation().apply {
        this.rel = rel
        this.href = href
    })
}
