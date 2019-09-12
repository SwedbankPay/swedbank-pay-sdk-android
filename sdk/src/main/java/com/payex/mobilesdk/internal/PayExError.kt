package com.payex.mobilesdk.internal

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import com.payex.mobilesdk.R
import com.payex.mobilesdk.TerminalFailure

internal sealed class PayExError : Parcelable {
    abstract val retryable: Boolean
    abstract fun getMessage(context: Context): String
    open suspend fun getDetails(context: Context): String? = null

    override fun describeContents() = 0
}

internal class ConnectionError(@StringRes private val messageId: Int) : PayExError() {
    override val retryable get() = true
    override fun getMessage(context: Context) = context.getString(messageId)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(messageId)
    }
    constructor(parcel: Parcel) : this(
        parcel.readInt()
    )
    companion object CREATOR : Parcelable.Creator<ConnectionError> {
        override fun createFromParcel(parcel: Parcel) = ConnectionError(parcel)
        override fun newArray(size: Int) = arrayOfNulls<ConnectionError>(size)
    }
}

internal class JavascriptError(val terminalFailure: TerminalFailure) : PayExError() {
    override val retryable get() = false
    override fun getMessage(context: Context) = terminalFailure.details ?: context.getString(R.string.unknown_error, terminalFailure.messageId)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(terminalFailure, flags)

    }
    constructor(parcel: Parcel) : this(
        checkNotNull(parcel.readParcelable<TerminalFailure>(TerminalFailure::class.java.classLoader))
    )
    companion object CREATOR : Parcelable.Creator<JavascriptError> {
        override fun createFromParcel(parcel: Parcel) = JavascriptError(parcel)
        override fun newArray(size: Int) = arrayOfNulls<JavascriptError>(size)
    }
}

internal class PaymentFailed(private val paymentId: String) : PayExError() {
    override val retryable get() = false
    override fun getMessage(context: Context) = context.getString(R.string.payment_failed)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(paymentId)
    }
    constructor(parcel: Parcel) : this(
        checkNotNull(parcel.readString())
    )
    companion object CREATOR : Parcelable.Creator<PaymentFailed> {
        override fun createFromParcel(parcel: Parcel) = PaymentFailed(parcel)
        override fun newArray(size: Int) = arrayOfNulls<PaymentFailed>(size)
    }
}