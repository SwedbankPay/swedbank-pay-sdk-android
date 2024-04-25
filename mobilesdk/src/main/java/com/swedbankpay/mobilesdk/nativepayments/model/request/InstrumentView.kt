package com.swedbankpay.mobilesdk.nativepayments.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class InstrumentView(
    @SerializedName("instrumentName")
    val instrumentName: String
)