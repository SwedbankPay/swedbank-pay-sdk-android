package com.swedbankpay.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName

@Suppress("unused")
internal class InitiateConsumerSessionArguments(
    @SerializedName("consumerCountryCode") private val consumerCountryCode: String
) {
    class NationalIdentifier(
        @SerializedName("socialSecurityNumber") private val socialSecurityNumber: String,
        @SerializedName("countryCode") private val countryCode: String
    )

    @SerializedName("msisdn")
    var msisdn: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("nationalIdentifier")
    var nationalIdentifier: NationalIdentifier? = null
}