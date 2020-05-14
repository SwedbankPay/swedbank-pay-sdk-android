package com.swedbankpay.mobilesdk.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.internal.remote.json.*
import org.mockito.stubbing.OngoingStubbing

// Kotlin has no checked exceptions, but mocking a throw of a Java checked exception
// results in org.mockito.exceptions.base.MockitoException: Checked exception is invalid for this method!
// doAnswer provides a workaround
// https://github.com/mockito/mockito/issues/1166
// https://github.com/mockito/mockito/issues/1166#issuecomment-399008021
// https://github.com/mockito/mockito/issues/1293#issuecomment-390921430
internal infix fun <T> OngoingStubbing<T>.throwKt(t: Throwable) = doAnswer { throw t }

internal fun <T> observing(liveData: LiveData<T>, f: (Observer<T>) -> Unit) {
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

internal fun mockPaymentOrders(): Link.PaymentOrders {
    val operations = operationsWith(
        "view-paymentorder",
        TestConstants.viewPaymentorderLink
    )

    return mock {
        onPost(PaymentOrderIn().apply {
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

internal fun KStubbing<Link.PaymentOrders>.onPost(paymentOrder: PaymentOrderIn) {
    onBlocking { post(any(), any(), any()) } doReturn paymentOrder
}

internal fun operationsWith(rel: String, href: String) = Operations().apply {
    add(Operation().apply {
        this.rel = rel
        this.href = href
    })
}
