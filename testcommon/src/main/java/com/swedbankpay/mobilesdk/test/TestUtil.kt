package com.swedbankpay.mobilesdk.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.ViewConsumerIdentificationInfo
import com.swedbankpay.mobilesdk.ViewPaymentOrderInfo
import org.mockito.stubbing.OngoingStubbing

// Kotlin has no checked exceptions, but mocking a throw of a Java checked exception
// results in org.mockito.exceptions.base.MockitoException: Checked exception is invalid for this method!
// doAnswer provides a workaround
// https://github.com/mockito/mockito/issues/1166
// https://github.com/mockito/mockito/issues/1166#issuecomment-399008021
// https://github.com/mockito/mockito/issues/1293#issuecomment-390921430
infix fun <T> OngoingStubbing<T>.throwKt(t: Throwable) = doAnswer { throw t }

fun <T> observing(liveData: LiveData<T>, f: (Observer<T>) -> Unit) {
    val observer = mock<Observer<T>>()
    liveData.observeForever(observer)
    f(observer)
    liveData.removeObserver(observer)
}

fun KStubbing<Configuration>.onPostConsumers(info: ViewConsumerIdentificationInfo) {
    onBlocking { postConsumers(any(), anyOrNull(), anyOrNull()) } doReturn info
}

fun KStubbing<Configuration>.onPostPaymentorders(info: ViewPaymentOrderInfo) {
    onBlocking { postPaymentorders(any(), anyOrNull(), anyOrNull(), anyOrNull()) } doReturn info
}
