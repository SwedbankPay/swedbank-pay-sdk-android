package com.payex.mobilesdk.internal

import android.os.Parcel
import android.os.Parcelable

internal sealed class PayExResult<T : Parcelable> : Parcelable {
    open val value: T? get() = null
    open val error: PayExError? get() = null

    override fun describeContents() = 0

    class Success<T : Parcelable>(override val value: T) : PayExResult<T>() {
        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(value, flags)
        }
        constructor(parcel: Parcel) : this(
            checkNotNull(parcel.readParcelable<T>(Success::class.java.classLoader))
        )
        companion object CREATOR : Parcelable.Creator<Success<*>> {
            override fun createFromParcel(parcel: Parcel) =
                Success<Parcelable>(parcel)
            override fun newArray(size: Int) = arrayOfNulls<Success<*>>(size)
        }
    }
    class Failure<T : Parcelable>(override val error: PayExError) : PayExResult<T>() {
        constructor(parcel: Parcel) : this(
            checkNotNull(parcel.readParcelable<PayExError>(PayExError::class.java.classLoader))
        )
        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(error, flags)
        }
        companion object CREATOR : Parcelable.Creator<Failure<*>> {
            override fun createFromParcel(parcel: Parcel) =
                Failure<Parcelable>(parcel)
            override fun newArray(size: Int) = arrayOfNulls<Failure<*>>(size)
        }
    }
}
