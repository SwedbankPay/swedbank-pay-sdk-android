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

internal inline fun <reified T : Parcelable> Parcel.readParcelable() = readParcelable<T>(T::class.java.classLoader)

internal fun Parcel.writeEnum(e: Enum<*>?) = writeInt(e?.ordinal ?: -1)
internal inline fun <reified E : Enum<E>> Parcel.readEnum() = readInt().takeIf { it > 0 }?.let {
    enumValues<E>()[it]
}

internal fun Parcel.writeBooleanCompat(b: Boolean) = writeInt(if (b) 1 else 0)
internal fun Parcel.readBooleanCompat() = readInt() != 0

private inline fun <T> Parcel.writeOptional(write: (T) -> Unit, value: T?) {
    writeBooleanCompat(value != null)
    value?.let(write)
}
private inline fun <T> Parcel.readOptional(read: () -> T) = if (readBooleanCompat()) {
    read()
} else {
    null
}

internal fun Parcel.writeOptionalBoolean(b: Boolean?) = writeOptional(::writeBooleanCompat, b)
internal fun Parcel.readOptionalBoolean() = readOptional(::readBooleanCompat)

internal fun Parcel.writeOptionalLong(l: Long?) = writeOptional(::writeLong, l)
internal fun Parcel.readOptionalLong() = readOptional(::readLong)


internal fun <T : Any> checkBuilderNotNull(value: T?, fieldName: String) =
    checkNotNull(value) { "$fieldName not set" }