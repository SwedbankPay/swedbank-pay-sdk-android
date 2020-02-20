package com.swedbankpay.mobilesdk

import com.google.gson.annotations.SerializedName

/**
 * Languages supported by checkin and payment menu.
 */
@Suppress("unused")
enum class Language {
    /**
     * English (US)
     */
    @SerializedName("en-US") ENGLISH,
    /**
     * Norwegian (Bokmål)
     */
    @SerializedName("nb-NO") NORWEGIAN,
    /**
     * Swedish
     */
    @SerializedName("sv-SE") SWEDISH
}
