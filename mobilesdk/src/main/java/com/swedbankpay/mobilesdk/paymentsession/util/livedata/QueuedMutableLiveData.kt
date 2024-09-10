package com.swedbankpay.mobilesdk.paymentsession.util.livedata

import androidx.lifecycle.MutableLiveData
import java.util.*

/**
 * Live data that contains a queue of posted values and will notify observer for
 * each value when active.
 */
class QueuedMutableLiveData<T>(value: T) : MutableLiveData<T>(value) {

    private val values: Queue<T> = LinkedList()

    private var isActive: Boolean = false

    override fun onActive() {
        isActive = true
        while (values.isNotEmpty()) {
            values.poll()?.let { setValue(it) }
        }
    }

    override fun onInactive() {
        isActive = false
    }

    override fun setValue(value: T) {
        if (isActive) {
            super.setValue(value)
        } else {
            values.add(value)
        }
    }
}