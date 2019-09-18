package com.payex.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.payex.mobilesdk.internal.makeCreator

/**
 * Describes a client error response that caused payment initialization to fail.
 *
 * @see PaymentViewModel.initializationErrorDescription
 */
class BadRequestDescription(
    /** The http status code. Will be in the range 400-499. */
    val code: Int,
    /** The Content-Type of the response body, if any. */
    val contentType: String?,
    /**
     *  The response body.
     *
     *  N.B! If the body could not be read into a [String], this will be `null`.
     */
    val body: String?
) : Parcelable {
    companion object { @JvmField val CREATOR = makeCreator(::BadRequestDescription) }

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
        parcel.writeString(contentType)
        parcel.writeString(body)
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )
}