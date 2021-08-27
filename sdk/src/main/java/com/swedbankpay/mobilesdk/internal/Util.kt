package com.swedbankpay.mobilesdk.internal

import android.os.Parcel
import android.os.Parcelable

internal const val LOG_TAG = "SwedbankPay"

internal inline fun <reified T> makeCreator(crossinline constructor: (Parcel) -> T): Parcelable.Creator<T> {
    return object : Parcelable.Creator<T> {
        override fun createFromParcel(source: Parcel) = constructor(source)
        override fun newArray(size: Int) = arrayOfNulls<T>(size)
    }
}

internal fun <T : Any> checkBuilderNotNull(value: T?, fieldName: String) =
    checkNotNull(value) { "$fieldName not set" }
