package com.payex.mobilesdk.internal

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

internal const val LOG_TAG = "PayEx"

internal inline fun <reified T> makeCreator(crossinline constructor: (Parcel) -> T): Parcelable.Creator<T> {
    return object : Parcelable.Creator<T> {
        override fun createFromParcel(source: Parcel) = constructor(source)
        override fun newArray(size: Int) = arrayOfNulls<T>(size)
    }
}

/**
 * Replacement for Transformations.map that ensures consistency between
 * getValue and observers.
 */
internal fun <T, U> LiveData<T>.getValueConsistentMap(f: (T?) -> U?): LiveData<U> {
    return GetValueConsistentMapLiveData<U>().also {
        it.addSource(this) { t ->
            it.value = f(t)
        }
    }
}

/**
 * Replacement for Transformations.map that ensures consistency between
 * getValue and observers. Also nullifies the value whenever the mapping function
 * returns null.
 */
internal fun <T, U> LiveData<T>.getValueConsistentSwitchMap(f: (T?) -> LiveData<U>?): LiveData<U> {
    val result = GetValueConsistentMapLiveData<U>()

    var currentSource: LiveData<U>? = null
    result.addSource(this) {
        val newSource = f(it)
        if (newSource != currentSource) {
            currentSource?.let(result::removeSource)
            currentSource = newSource
            if (newSource == null) {
                result.value = null
            } else {
                result.addSource(newSource, result::setValue)
            }
        }
    }

    return result
}

/*
 * Helper MediatorLiveData that ensures that a call to getValue
 * returns the same value as an active observer added at the same
 * time would receive. This behaviour helps us provide consistent
 * semantics to the public PaymentViewModel.
 */
private class GetValueConsistentMapLiveData<T> : MediatorLiveData<T>() {
    override fun getValue(): T? {
        if (!hasActiveObservers()) {
            // We have no active observers;
            // value may not correspond to latest value of source.
            // Adding an observer forces re-evaluation.
            val observer = Observer<T> {}
            observeForever(observer)
            removeObserver(observer)
        }
        return super.getValue()
    }
}
