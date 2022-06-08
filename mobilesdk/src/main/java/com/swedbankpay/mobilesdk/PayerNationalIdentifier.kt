package com.swedbankpay.mobilesdk

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PayerNationalIdentifier(
    /**
     * The unique identifier of a person in a country
     */
    @SerializedName("socialSecurityNumber") val socialSecurityNumber: String? = null,
    /**
     * The 2 letter country code of the country
     */
    @SerializedName("countryCode") val countryCode: String? = null,
) : Parcelable {
    @Suppress("unused")
    class Builder {
        private var socialSecurityNumber: String? = null
        private var countryCode: String? = null

        fun socialSecurityNumber(socialSecurityNumber: String?) = apply { this.socialSecurityNumber = socialSecurityNumber }
        fun countryCode(countryCode: String?) = apply { this.countryCode = countryCode }
        
        fun build() = PayerNationalIdentifier(
            socialSecurityNumber = socialSecurityNumber,
            countryCode = countryCode
        )
    }
}
