package com.payex.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.payex.mobilesdk.internal.makeCreator
import com.payex.mobilesdk.internal.readProblem
import com.payex.mobilesdk.internal.writeProblem

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
 *
 * There are also several interfaces defined for related problem types.
 * They are:
 *
 *  - [ProperProblem]: problems actually parsed from a application/problem+json object
 *  - [PayExProblem]: problems originating from Swedbank Pay backends
 *  - [UnknownProblem]: problems of an unknown [type][https://tools.ietf.org/html/rfc7807#section-3.1]
 *  - [UnexpectedContentProblem]: (pseudo-)problems where the response was not application/problem+json
 */
@Suppress("unused")
sealed class Problem : Parcelable {
    /**
     * Base class for [Problems][Problem] caused by the service refusing or
     * not understanding a request sent to it by the client.
     *
     * A Client Problem always implies a HTTP status in 400-499.
     */
    sealed class Client : Problem() {
        /**
         * Base class for [Client] Problems defined by the example backend.
         */
        sealed class MobileSDK(
            override val raw: JsonObject,
            override val status: Int
        ) : Client(), ProperProblem {
            /**
             * The merchant backend rejected the request because its authentication headers were invalid.
             */
            class Unauthorized(raw: JsonObject, status: Int,
                               /**
                                * An optional message describing the reason for the rejection.
                                */
                               val message: String?
            ) : MobileSDK(raw, status)

            /**
             * The merchant backend did not understand the request.
             */
            class InvalidRequest(raw: JsonObject, status: Int,
                                 /**
                                  * An optional message describing the error
                                  */
                                 val message: String?
            ) : MobileSDK(raw, status)
        }

        /**
         * Base class for [Client] problems defined by the Swedbank Pay backend.
         * [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems]
         */
        sealed class PayEx(
            override val raw: JsonObject,
            override val title: String?,
            override val status: Int,
            override val detail: String?,
            override val instance: String?,
            override val action: PayexAction?,
            override val problems: List<PayExSubproblem>
        ) : Client(), PayExProblem {
            /**
             * The request could not be handled because the request was malformed somehow (e.g. an invalid field value)
             */
            class InputError(raw: JsonObject, title: String?, status: Int, detail: String?, instance: String?, action: PayexAction?, problems: List<PayExSubproblem>)
                : PayEx(raw, title, status, detail, instance, action, problems)
            /**
             * The request was understood, but the service is refusing to fulfill it. You may not have access to the requested resource.
             */
            class Forbidden(raw: JsonObject, title: String?, status: Int, detail: String?, instance: String?, action: PayexAction?, problems: List<PayExSubproblem>)
                : PayEx(raw, title, status, detail, instance, action, problems)
            /**
             * The requested resource was not found.
             */
            class NotFound(raw: JsonObject, title: String?, status: Int, detail: String?, instance: String?, action: PayexAction?, problems: List<PayExSubproblem>)
                : PayEx(raw, title, status, detail, instance, action, problems)
        }

        /**
         * [Client] problem with an unrecognized type.
         */
        class Unknown(
            override val raw: JsonObject,
            override val type: String,
            override val title: String?,
            override val status: Int,
            override val detail: String?,
            override val instance: String?
        ) : Client(), UnknownProblem

        /**
         * Pseudo-problem, not actually parsed from an application/problem+json response.
         * This problem is emitted if the server response is in an unexpected format and the
         * HTTP status is in the Client Error range (400-499).
         */
        class UnexpectedContent(
            override val status: Int,
            override val contentType: String?,
            override val body: String?
        ) : Client(), UnexpectedContentProblem
    }

    /**
     * Base class for [Problems][Problem] caused by the service backend.
     *
     * Any unexpected response where the HTTP status is outside 400-499
     * results in a Server Problem; usually it means the status was in 500-599.
     */
    sealed class Server : Problem() {
        /**
         * Base class for [Server] Problems defined by the example backend.
         */
        sealed class MobileSDK(
            override val raw: JsonObject,
            override val status: Int
        ) : Server(), ProperProblem {
            /**
             * The merchant backend timed out trying to connect to the Swedbank Pay backend.
             */
            class BackendConnectionTimeout(raw: JsonObject, status: Int,
                                           /**
                                            * An optional message describing the error
                                            */
                                           val message: String?
            ) : MobileSDK(raw, status)

            /**
             * The merchant backend failed to connect to the Swedbank Pay backend.
             */
            class BackendConnectionFailure(raw: JsonObject, status: Int,
                                           /**
                                            * An optional message describing the error
                                            */
                                           val message: String?
            ) : MobileSDK(raw, status)

            /**
             * The merchant backend received an invalid response from the Swedbank Pay backend.
             */
            class InvalidBackendResponse(raw: JsonObject, status: Int,
                                         /**
                                          * The HTTP status code received from the Swedback Pay backend
                                          */
                                         val backendStatus: Int,
                                         /**
                                          * The unrecognized body received from the Swedback Pay backend
                                          */
                                         val body: String?
            ) : MobileSDK(raw, status)
        }

        /**
         * Base class for [Server] problems defined by the Swedbank Pay backend.
         * [https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/technical-reference/#HProblems]
         */
        sealed class PayEx(
            override val raw: JsonObject,
            override val title: String?,
            override val status: Int,
            override val detail: String?,
            override val instance: String?,
            override val action: PayexAction?,
            override val problems: List<PayExSubproblem>
        ) : Server(), PayExProblem {
            class SystemError(raw: JsonObject, title: String?, status: Int, detail: String?, instance: String?, action: PayexAction?, problems: List<PayExSubproblem>)
                : PayEx(raw, title, status, detail, instance, action, problems)

            class ConfigurationError(raw: JsonObject, title: String?, status: Int, detail: String?, instance: String?, action: PayexAction?, problems: List<PayExSubproblem>)
                : PayEx(raw, title, status, detail, instance, action, problems)
        }

        /**
         * [Server] problem with an unrecognized type.
         */
        class Unknown(
            override val raw: JsonObject,
            override val type: String,
            override val title: String?,
            override val status: Int,
            override val detail: String?,
            override val instance: String?
        ) : Server(), UnknownProblem

        /**
         * Pseudo-problem, not actually parsed from an application/problem+json response.
         * This problem is emitted if the server response is in an unexpected format and the
         * HTTP status is not in the Client Error range.
         */
        class UnexpectedContent(
            override val status: Int,
            override val contentType: String?,
            override val body: String?
        ) : Server(), UnexpectedContentProblem
    }

    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeProblem(this)
    }
    companion object {
        @Suppress("unused")
        @JvmField val CREATOR = makeCreator { parcel ->
            parcel.readProblem()
        }
    }
}