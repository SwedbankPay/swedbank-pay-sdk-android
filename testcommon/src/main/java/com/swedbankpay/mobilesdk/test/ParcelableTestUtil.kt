package com.swedbankpay.mobilesdk.test

import android.os.Parcel
import android.os.Parcelable
//import androidx.annotation.RequiresApi
import org.junit.Assert

//@RequiresApi(33)
fun testParcelable(parcelable: Parcelable) {
    val parcel = Parcel.obtain()

    parcel.writeParcelable(parcelable, 0)
    parcel.setDataPosition(0)
    
    val unParseled = parcel.readParcelable<Parcelable>(parcelable.javaClass.classLoader)    //, Parcelable::class.java
    Assert.assertEquals(parcelable, unParseled)

    parcel.recycle()
}
