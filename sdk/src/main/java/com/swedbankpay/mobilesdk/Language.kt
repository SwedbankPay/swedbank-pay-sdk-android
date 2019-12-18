package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

@Suppress("unused")
enum class Language {
    @SerializedName("en-US") ENGLISH,
    @SerializedName("nb-NO") NORWEGIAN,
    @SerializedName("sv-SE") SWEDISH
}
