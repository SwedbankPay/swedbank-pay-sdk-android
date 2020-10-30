package com.swedbankpay.mobilesdk.internal

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

private const val USER_DATA_NULL = 0
private const val USER_DATA_PARCELABLE = 1
private const val USER_DATA_STRING = 2
private const val USER_DATA_SERIALIZABLE = 3
internal fun Parcel.writeUserData(userData: Any?, flags: Int) {
    when (userData) {
        null -> writeInt(USER_DATA_NULL)
        is Parcelable -> {
            writeInt(USER_DATA_PARCELABLE)
            writeParcelable(userData, flags)
        }
        is String -> {
            writeInt(USER_DATA_STRING)
            writeString(userData)
        }
        is Serializable -> {
            writeInt(USER_DATA_SERIALIZABLE)
            writeSerializable(userData)
        }
        else -> {
            error("userData must be Parcelable or Serializable")
        }
    }
}
internal fun Parcel.readUserData(): Any? = when (readInt()) {
    USER_DATA_NULL -> null
    USER_DATA_PARCELABLE -> readParcelable(InternalPaymentViewModel::class.java.classLoader)
    USER_DATA_STRING -> readString()
    USER_DATA_SERIALIZABLE -> readSerializable()
    else -> error("invalid userData type")
}
