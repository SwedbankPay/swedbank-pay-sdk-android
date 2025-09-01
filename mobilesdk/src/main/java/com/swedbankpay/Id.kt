package com.swedbankpay

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Id(
    @SerializedName("id")
    val id: String
)