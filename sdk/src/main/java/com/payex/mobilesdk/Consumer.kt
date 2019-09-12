package com.payex.mobilesdk

import com.google.gson.Gson
import com.payex.mobilesdk.Consumer.Companion.ANONYMOUS
import com.payex.mobilesdk.Consumer.Identified
import com.payex.mobilesdk.Consumer.Stored
import com.payex.mobilesdk.internal.remote.json.InitiateConsumerSessionArguments

/**
 * A Consumer making a payment. You need a Consumer to use [PaymentFragment].
 *
 * A Consumer can be either [ANONYMOUS] or known.
 * A known Consumer will initially be [Identified] interactively.
 * You may optionally store the consumer reference after the identification
 * and use a [Stored] for further payments.
 */
abstract class Consumer private constructor() {
    companion object {
        /**
         * Specifies an anonymous, i.e. unidentified consumer.
         */
        @JvmField
        val ANONYMOUS: Consumer = object : Consumer() {
            override fun getIdMode() = PaymentFragment.ID_MODE_ANONYMOUS
            override fun getIdData(): String? = null
        }
    }

    /**
     * Specifies a consumer using a stored reference from a previous identification.
     *
     * @constructor
     * @param consumerProfileRef the stored consumer reference
     */
    class Stored(private val consumerProfileRef: String) : Consumer() {
        override fun getIdMode() = PaymentFragment.ID_MODE_STORED
        override fun getIdData() = consumerProfileRef
    }

    /**
     * Specifies an interactively identified consumer.
     *
     * The customer's country of residence must be initially known.
     * Other parameters may also be set to allow for prepopulated UI.
     *
     * @constructor
     * @param consumerCountryCode consumer's country of residence. Should be one of "NO", "SE"
     */
    class Identified(consumerCountryCode: String) : Consumer() {
        /**
         * Provides the consumer's mobile phone number for the identification process.
         * @param mobilePhoneNumber consumer's mobile phone number in international format
         */
        fun mobilePhoneNumber(mobilePhoneNumber: String) = apply {
            apiArguments.msisdn = mobilePhoneNumber
        }

        /**
         * Provides the consumer's e-mail address for the identification process.
         * @param email consumer's e-mail address
         */
        fun email(email: String) = apply {
            apiArguments.email = email
        }

        /**
         * Provides the consumer's national identifier for the identification process.
         * @param socialSecurityNumber consumer's social security number
         * @param countryCode country code of the issuer of the social security number. Should be one of "NO", "SE"
         */
        fun nationalIdentifier(socialSecurityNumber: String, countryCode: String) = apply {
            apiArguments.nationalIdentifier = InitiateConsumerSessionArguments.NationalIdentifier(socialSecurityNumber, countryCode)
        }

        private val apiArguments = InitiateConsumerSessionArguments(consumerCountryCode)

        override fun getIdMode() = PaymentFragment.ID_MODE_ONLINE
        override fun getIdData() = Gson().toJson(apiArguments)
    }

    internal abstract fun getIdMode(): Int
    internal abstract fun getIdData(): String?
}