package com.swedbankpay.mobilesdk

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * The social security number for a specific person in a specific country.
 */
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
    /**
     * Support for the builder pattern
     */
    @Suppress("unused")
    class Builder {
        private var socialSecurityNumber: String? = null
        private var countryCode: String? = null

        /**
         * The unique identifier of a person in a country
         */
        fun socialSecurityNumber(socialSecurityNumber: String?) = apply { this.socialSecurityNumber = socialSecurityNumber }
        /**
         * The 2 letter country code of the country
         */
        fun countryCode(countryCode: String?) = apply { this.countryCode = countryCode }

        /**
         * Support for the builder pattern
         */
        fun build() = PayerNationalIdentifier(
            socialSecurityNumber = socialSecurityNumber,
            countryCode = countryCode
        )
    }
}
