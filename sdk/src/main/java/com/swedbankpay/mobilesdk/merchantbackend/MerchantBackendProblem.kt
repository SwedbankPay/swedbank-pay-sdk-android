package com.swedbankpay.mobilesdk.merchantbackend

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.swedbankpay.mobilesdk.Problem
import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendProblem.Client
import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendProblem.Server
import com.swedbankpay.mobilesdk.internal.*

/**
 * Base class for any problems encountered in the payment.
 *
 * Problems always result from communication with the backend;
 * lower-level network errors are not represented by Problems,
 * but rather by IOExceptions as is usual.
 *
 * Swedbank interfaces, as well as the example merchant backend,
 * report problems using the Problem Details for HTTP APIs
 * protocol (https://tools.ietf.org/html/rfc7807), specifically the
 * json representation. Your custom merchant backend is enouraged to
 * do so as well. These classes provide a convenient java representation
 * of the problems so your client code does not need to deal with the raw
 * json. Any custom problem cases you add to your merchant backend will
 * be reported as "Unknown" problems, and you will have to implement
 * parsing for those in your client, of course.
 *
 * All problems are either [Client] or [Server] problems. A Client
 * problem is one where there was something wrong with the request
 * the client app sent to the service. A Client problem always implies an
 * HTTP response status in the Client Error range, 400-499.
 *
 * A Server problem in one where the service understood the request, but
 * could not fulfill it. If the backend responds in an unexpected
 * manner, the situation will be interpreted as a Server error, unless
 * the response status is in 400-499, in which case it is still considered a
 * Client error.
 *
 * This separation to Client and Server errors provides a crude but often
 * effective way of distinguishing between temporary service unavailability
 * and permanent configuration errors. Indeed, the PaymentFragment will internally
 * consider any Client errors to be fatal, but most Server errors to be retryable.
 *
 * Client and Server errors are further divided to specific types. See individual
 * class documentation for details.
 */
@Suppress("unused")
sealed class MerchantBackendProblem(jsonObject: JsonObject) : Problem(jsonObject) {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeProblem(this)
    }

    /**
     * Base class for [Problems][MerchantBackendProblem] caused by the service refusing or
     * not understanding a request sent to it by the client.
     *
     * A Client Problem always implies a HTTP status in 400-499.
     */
    sealed class Client(jsonObject: JsonObject) : MerchantBackendProblem(jsonObject) {
        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Client> = makeCreator { parcel ->
                parcel.readClientProblem()
            }
        }

        /**
         * Base class for [Client] Problems defined by the example backend.
         */
        sealed class MobileSDK(jsonObject: JsonObject) : Client(jsonObject) {
            /**
             * The merchant backend rejected the request because its authentication headers were invalid.
             *
             * The [detail][Problem.detail] property may optionally contain a message describing the error.
             */
            class Unauthorized internal constructor(jsonObject: JsonObject) : MobileSDK(jsonObject)

            /**
             * The merchant backend did not understand the request.
             *
             * The [detail][Problem.detail] property may optionally contain a message describing the error.
             */
            class InvalidRequest internal constructor(jsonObject: JsonObject) : MobileSDK(jsonObject)
        }

        /**
         * Base class for [Client] problems defined by the Swedbank Pay backend.
         * [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems]
         */
        sealed class SwedbankPay(jsonObject: JsonObject) : Client(jsonObject), SwedbankPayProblem {
            override val action: SwedbankPayAction?
                get() = jsonObject["action"]?.asStringOrNull

            override val problems = (this@SwedbankPay.jsonObject["problems"] as? JsonArray)
                    ?.asSequence().orEmpty()
                    .filterIsInstance<JsonObject>()
                    .map {
                        SwedbankPaySubproblem(
                            it["name"]?.asStringOrNull,
                            it["description"]?.asStringOrNull
                        )
                    }
                    .toList()

            /**
             * The request could not be handled because the request was malformed somehow (e.g. an invalid field value)
             */
            class InputError(jsonObject: JsonObject) : SwedbankPay(jsonObject)
            /**
             * The request was understood, but the service is refusing to fulfill it. You may not have access to the requested resource.
             */
            class Forbidden(jsonObject: JsonObject) : SwedbankPay(jsonObject)
            /**
             * The requested resource was not found.
             */
            class NotFound(jsonObject: JsonObject) : SwedbankPay(jsonObject)
        }

        /**
         * [Client] problem with an unrecognized type.
         */
        class Unknown(jsonObject: JsonObject) : Client(jsonObject)
    }

    /**
     * Base class for [Problems][MerchantBackendProblem] caused by the service backend.
     *
     * Any unexpected response where the HTTP status is outside 400-499
     * results in a Server Problem; usually it means the status was in 500-599.
     */
    sealed class Server(jsonObject: JsonObject) : MerchantBackendProblem(jsonObject) {
        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Server> = makeCreator { parcel ->
                parcel.readServerProblem()
            }
        }

        /**
         * Base class for [Server] Problems defined by the example backend.
         */
        sealed class MobileSDK(jsonObject: JsonObject) : Server(jsonObject) {
            /**
             * The merchant backend timed out trying to connect to the Swedbank Pay backend.
             *
             * The [detail][Problem.detail] property may optionally contain a message describing the error.
             */
            class BackendConnectionTimeout(jsonObject: JsonObject) : MobileSDK(jsonObject)

            /**
             * The merchant backend failed to connect to the Swedbank Pay backend.
             *
             * The [detail][Problem.detail] property may optionally contain a message describing the error
             */
            class BackendConnectionFailure(jsonObject: JsonObject) : MobileSDK(jsonObject)

            /**
             * The merchant backend received an invalid response from the Swedbank Pay backend.
             */
            class InvalidBackendResponse(
                jsonObject: JsonObject,
                /**
                 * The HTTP status code received from the Swedback Pay backend
                 */
                val backendStatus: Int
            ) : MobileSDK(jsonObject) {
                /**
                 * The unrecognized body received from the Swedback Pay backend
                 */
                val body: String?
                    get() = jsonObject["body"]?.asStringOrNull
            }
        }

        /**
         * Base class for [Server] problems defined by the Swedbank Pay backend.
         * [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems]
         */
        sealed class SwedbankPay(jsonObject: JsonObject) : Server(jsonObject), SwedbankPayProblem {
            override val action: SwedbankPayAction?
                get() = jsonObject["action"]?.asStringOrNull

            override val problems = (this@SwedbankPay.jsonObject["problems"] as? JsonArray)
                ?.asSequence().orEmpty()
                .filterIsInstance<JsonObject>()
                .map {
                    SwedbankPaySubproblem(
                        it["name"]?.asStringOrNull,
                        it["description"]?.asStringOrNull
                    )
                }
                .toList()

            /**
             * General internal error in Swedbank Pay systems.
             */
            class SystemError(jsonObject: JsonObject) : SwedbankPay(jsonObject)

            /**
             * There is a problem with your merchant configuration.
             */
            class ConfigurationError(jsonObject: JsonObject) : SwedbankPay(jsonObject)
        }

        /**
         * [Server] problem with an unrecognized type.
         */
        class Unknown(jsonObject: JsonObject) : Server(jsonObject)
    }
}