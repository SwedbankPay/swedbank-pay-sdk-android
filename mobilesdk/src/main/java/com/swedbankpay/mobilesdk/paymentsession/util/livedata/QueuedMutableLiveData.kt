package com.swedbankpay.mobilesdk.paymentsession.util.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
class QueuedMutableLiveData<T>() : MutableLiveData<T>() {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val values: Queue<T> = LinkedList()

    private var isActive: Boolean = false

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer { t ->
            if(t != null) {
                observer.onChanged(t)
                postValue(null)
            }
        })
    }

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