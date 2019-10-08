package com.payex.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.payex.mobilesdk.internal.makeCreator
import com.payex.mobilesdk.internal.readProblem
import com.payex.mobilesdk.internal.writeProblem


sealed class Problem : Parcelable {
    sealed class Client : Problem() {
        sealed class MobileSDK(
            override val raw: JsonObject,
            override val status: Int
        ) : Client(), ProperProblem {
            class Unauthorized(raw: JsonObject, status: Int, val message: String?) : MobileSDK(raw, status)
            class InvalidRequest(raw: JsonObject, status: Int, val message: String?) : MobileSDK(raw, status)
        }
        sealed class PayEx(
            override val raw: JsonObject,
            override val title: String?,
            override val status: Int,
            override val detail: String?,
            override val instance: String?,
            override val action: PayexAction?,
            override val problems: List<PayExSubproblem>
        ) : Client(), PayExProblem {
            class InputError(raw: JsonObject, title: String?, status: Int, detail: String?, instance: String?, action: PayexAction?, problems: List<PayExSubproblem>)
                : PayEx(raw, title, status, detail, instance, action, problems)

            class Forbidden(raw: JsonObject, title: String?, status: Int, detail: String?, instance: String?, action: PayexAction?, problems: List<PayExSubproblem>)
                : PayEx(raw, title, status, detail, instance, action, problems)

            class NotFound(raw: JsonObject, title: String?, status: Int, detail: String?, instance: String?, action: PayexAction?, problems: List<PayExSubproblem>)
                : PayEx(raw, title, status, detail, instance, action, problems)
        }
        class Unknown(
            override val raw: JsonObject,
            override val type: String?,
            override val title: String?,
            override val status: Int,
            override val detail: String?,
            override val instance: String?
        ) : Client(), UnknownProblem
        class UnexpectedContent(
            override val status: Int,
            override val contentType: String?,
            override val body: String?
        ) : Client(), UnexpectedContentProblem
    }

    sealed class Server : Problem() {
        sealed class MobileSDK(
            override val raw: JsonObject,
            override val status: Int
        ) : Server(), ProperProblem {
            class BackendConnectionTimeout(raw: JsonObject, status: Int, val message: String?) : MobileSDK(raw, status)
            class BackendConnectionFailure(raw: JsonObject, status: Int, val message: String?) : MobileSDK(raw, status)
            class InvalidBackendResponse(raw: JsonObject, responseStatus: Int, val body: String?) : MobileSDK(raw, responseStatus)
        }
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
        class Unknown(
            override val raw: JsonObject,
            override val type: String?,
            override val title: String?,
            override val status: Int,
            override val detail: String?,
            override val instance: String?
        ) : Server(), UnknownProblem
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