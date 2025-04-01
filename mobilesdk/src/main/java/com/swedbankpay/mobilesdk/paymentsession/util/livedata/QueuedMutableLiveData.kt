package com.swedbankpay.mobilesdk.paymentsession.util.livedata

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Live data that contains a queue of posted values and will notify observer for
 * each value when active.
 */
class QueuedMutableLiveData<T>(value: T) : MutableLiveData<T>(value) {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val values: Queue<T> = LinkedList()

    private var isActive: Boolean = false

    override fun onActive() {
        isActive = true
        scope.launch {
            while (values.isNotEmpty()) {
                values.poll()?.let {
                    setValue(it)
                    delay(100)
                }
            }
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