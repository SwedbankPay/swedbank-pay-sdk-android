package com.payex.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName

internal class InitiateConsumerSessionArguments(
    @SerializedName("consumerCountryCode") private val consumerCountryCode: String
) {
    class NationalIdentifier(
        @SerializedName("socialSecurityNumber") private val socialSecurityNumber: String,
        @SerializedName("countryCode") private val countryCode: String
    )

    //@SerializedName("operation")
    //private val operation = "initiate-consumer-session"
    @SerializedName("msisdn")
    var msisdn: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("nationalIdentifier")
    var nationalIdentifier: NationalIdentifier? = null
}