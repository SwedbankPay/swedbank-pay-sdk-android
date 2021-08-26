package com.swedbankpay.mobilesdk.test

import android.os.Parcel
import android.os.Parcelable
import org.junit.Assert

fun testParcelable(parcelable: Parcelable) {
    val parcel = Parcel.obtain()

    parcel.writeParcelable(parcelable, 0)
    parcel.setDataPosition(0)
    val unparceled = parcel.readParcelable<Parcelable>(parcelable.javaClass.classLoader)
    Assert.assertEquals(parcelable, unparceled)

    parcel.recycle()
}
