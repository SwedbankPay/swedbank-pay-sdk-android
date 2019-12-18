package com.swedbankpay.mobilesdk.internal

import android.os.Parcel
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.swedbankpay.mobilesdk.*
import okhttp3.Response

private inline fun <T> tryOrNull(f: () -> T) = try { f() } catch (_: Exception) { null }
private val JsonElement.asStringOrNull get() = tryOrNull { asString }
private val JsonElement.asIntOrNull get() = tryOrNull { asInt }

// common rfc7807 properties
private val JsonObject.type get() = get("type")?.asStringOrNull ?: "about:blank"
private val JsonObject.title get() = get("title")?.asStringOrNull
private val JsonObject.status get() = get("status")?.asIntOrNull
private val JsonObject.detail get() = get("detail")?.asStringOrNull
private val JsonObject.instance get() = get("instance")?.asStringOrNull

private const val PROBLEM_TYPE_PROPER = 0
private const val PROBLEM_TYPE_UNEXPECTED_CONTENT = 1

internal fun Parcel.writeProblem(problem: Problem) = when (problem) {
    is Problem.Client.MobileSDK -> writeProperProblem(problem)
    is Problem.Client.SwedbankPay -> writeProperProblem(problem)
    is Problem.Client.Unknown -> writeProperProblem(problem)
    is Problem.Client.UnexpectedContent -> writeUnexpectedContentProblem(problem)
    is Problem.Server.MobileSDK -> writeProperProblem(problem)
    is Problem.Server.SwedbankPay -> writeProperProblem(problem)
    is Problem.Server.Unknown -> writeProperProblem(problem)
    is Problem.Server.UnexpectedContent -> writeUnexpectedContentProblem(problem)
}
internal fun Parcel.readProblem() = when (val type = readInt()) {
    PROBLEM_TYPE_PROPER -> readProperProblem()
    PROBLEM_TYPE_UNEXPECTED_CONTENT -> readUnexpectedContentProblem()
    else -> error("Bad problem type in Parcel: $type")
}

private fun Parcel.writeProperProblem(problem: ProperProblem) {
    writeInt(PROBLEM_TYPE_PROPER)
    writeInt(problem.status)
    writeString(problem.raw.toString())
}
private fun Parcel.readProperProblem(): Problem {
    val status = readInt()
    val jsonString = checkNotNull(readString())
    return parseProblem(status, jsonString)
}
private fun Parcel.writeUnexpectedContentProblem(problem: UnexpectedContentProblem) {
    writeInt(PROBLEM_TYPE_UNEXPECTED_CONTENT)
    writeInt(problem.status)
    writeString(problem.contentType)
    writeString(problem.body)
}
private fun Parcel.readUnexpectedContentProblem(): Problem {
    val status = readInt()
    val contentType = readString()
    val body = readString()
    return makeUnexpectedContentProblem(status, contentType, body)
}

internal fun makeUnexpectedContentProblem(response: Response, body: String?): Problem {
    val status = response.code()
    val contentType = response.body()?.contentType()?.toString()
    return makeUnexpectedContentProblem(status, contentType, body)
}

private fun makeUnexpectedContentProblem(status: Int, contentType: String?, body: String?): Problem {
    val isClient = status in 400..499
    return if (isClient) {
        Problem.Client.UnexpectedContent(status, contentType, body)
    } else {
        Problem.Server.UnexpectedContent(status, contentType, body)
    }
}

internal fun parseProblem(response: Response, jsonString: String): Problem {
    return try {
        parseProblem(response.code(), jsonString)
    } catch (e: Exception) {
        makeUnexpectedContentProblem(response, jsonString)
    }
}

private fun parseProblem(status: Int, jsonString: String): Problem {
    val json = JsonParser().parse(jsonString).asJsonObject
    val problemStatus = json.status ?: status
    val problemSpace = when (problemStatus) {
        in 400..499 -> ProblemSpace.Client
        in 500..599 -> ProblemSpace.Server
        else -> throw IllegalArgumentException()
    }
    return problemSpace.parseProblem(problemStatus, json)
}

private fun parseSwedbankPayProblem(
    constructor: (JsonObject, String?, Int, String?, String?, SwedbankPayAction?, List<SwedbankPaySubproblem>) -> Problem,
    status: Int,
    json: JsonObject
): Problem {
    val problems = (json["problems"] as? JsonArray)
        ?.asSequence().orEmpty()
        .filterIsInstance<JsonObject>()
        .map {
            SwedbankPaySubproblem(
                it["name"].asStringOrNull,
                it["description"].asStringOrNull
            )
        }
        .toList()
    return constructor(
        json,
        json.title,
        status,
        json.detail,
        json.instance,
        json["action"]?.asStringOrNull,
        problems
    )
}

private sealed class ProblemSpace {
    object Client : ProblemSpace() {
        override fun parseKnownProblem(type: String, status: Int, json: JsonObject) = when (type) {
            "https://api.payex.com/psp/errordetail/mobilesdk/unauthorized" ->
                Problem.Client.MobileSDK.Unauthorized(json, status, json.detail)
            "https://api.payex.com/psp/errordetail/mobilesdk/badrequest" ->
                Problem.Client.MobileSDK.InvalidRequest(json, status, json.detail)
            "https://api.payex.com/psp/errordetail/inputerror" ->
                parseSwedbankPayProblem(Problem.Client.SwedbankPay::InputError, status, json)
            "https://api.payex.com/psp/errordetail/forbidden" ->
                parseSwedbankPayProblem(Problem.Client.SwedbankPay::Forbidden, status, json)
            "https://api.payex.com/psp/errordetail/notfound" ->
                parseSwedbankPayProblem(Problem.Client.SwedbankPay::NotFound, status, json)
            else -> null
        }

        override fun makeUnknownProblem(
            raw: JsonObject, type: String, title: String?, status: Int, detail: String?, instance: String?
        ) = Problem.Client.Unknown(raw, type, title, status, detail, instance)
    }

    object Server : ProblemSpace() {
        override fun parseKnownProblem(type: String, status: Int, json: JsonObject) = when (type) {
            "https://api.payex.com/psp/errordetail/mobilesdk/gatewaytimeout" ->
                Problem.Server.MobileSDK.BackendConnectionTimeout(json, status, json.detail)
            "https://api.payex.com/psp/errordetail/mobilesdk/badgateway" ->
                parseBadGatewayProblem(status, json)
            "https://api.payex.com/psp/errordetail/systemerror" ->
                parseSwedbankPayProblem(Problem.Server.SwedbankPay::SystemError, status, json)
            "https://api.payex.com/psp/errordetail/configurationerror" ->
                parseSwedbankPayProblem(Problem.Server.SwedbankPay::ConfigurationError, status, json)
            else -> null
        }

        override fun makeUnknownProblem(
            raw: JsonObject, type: String, title: String?, status: Int, detail: String?, instance: String?
        ) = Problem.Server.Unknown(raw, type, title, status, detail, instance)

        private fun parseBadGatewayProblem(status: Int, json: JsonObject): Problem {
            // a https://api.payex.com/psp/errordetail/mobilesdk/badgateway
            // problem will have a "gatewayStatus" field if and only if it originated from a bogus
            // Swedbank response.
            val gatewayStatus = json["gatewayStatus"]?.asIntOrNull
            return if (gatewayStatus != null) {
                Problem.Server.MobileSDK.InvalidBackendResponse(json, status, gatewayStatus, json["body"]?.asStringOrNull)
            } else {
                Problem.Server.MobileSDK.BackendConnectionFailure(json, status, json.detail)
            }
        }
    }

    fun parseProblem(status: Int, json: JsonObject): Problem {
        val type = json.type
        return parseKnownProblem(type, status, json)
            ?: makeUnknownProblem(
                json,
                type,
                json.title,
                status,
                json.detail,
                json.instance
            )
    }
    protected abstract fun parseKnownProblem(type: String, status: Int, json: JsonObject): Problem?
    protected abstract fun makeUnknownProblem(
        raw: JsonObject,
        type: String,
        title: String?,
        status: Int,
        detail: String?,
        instance: String?
    ): Problem
}
