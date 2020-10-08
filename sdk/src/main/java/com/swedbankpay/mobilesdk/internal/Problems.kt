package com.swedbankpay.mobilesdk.internal

import android.os.Parcel
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendProblem

internal fun Parcel.writeProblem(merchantBackendProblem: MerchantBackendProblem)  {
    writeString(merchantBackendProblem.raw)
}
private inline fun <T> Parcel.readProblem(parser: (JsonObject) -> T): T {
    val jsonString = checkNotNull(readString())
    val json = JsonParser.parseString(jsonString).asJsonObject
    return parser(json)
}
internal fun Parcel.readClientProblem() = readProblem(::parseClientProblem)
internal fun Parcel.readServerProblem() = readProblem(::parseServerProblem)

internal fun parseProblem(status: Int, jsonString: String): MerchantBackendProblem {
    val json = JsonParser.parseString(jsonString).asJsonObject
    return when (json["status"]?.asIntOrNull ?: status) {
        in 400..499 -> parseClientProblem(json)
        in 500..599 -> parseServerProblem(json)
        else -> throw IllegalArgumentException()
    }
}

private val JsonObject.type get() = get("type")?.asStringOrNull

private fun parseClientProblem(json: JsonObject) = when (json.type) {
    "https://api.payex.com/psp/errordetail/mobilesdk/unauthorized" ->
        MerchantBackendProblem.Client.MobileSDK.Unauthorized(json)
    "https://api.payex.com/psp/errordetail/mobilesdk/badrequest" ->
        MerchantBackendProblem.Client.MobileSDK.InvalidRequest(json)
    "https://api.payex.com/psp/errordetail/inputerror" ->
        MerchantBackendProblem.Client.SwedbankPay.InputError(json)
    "https://api.payex.com/psp/errordetail/forbidden" ->
        MerchantBackendProblem.Client.SwedbankPay.Forbidden(json)
    "https://api.payex.com/psp/errordetail/notfound" ->
        MerchantBackendProblem.Client.SwedbankPay.NotFound(json)
    else -> throw IllegalArgumentException()
}

private fun parseServerProblem(json: JsonObject) = when (json.type) {
    "https://api.payex.com/psp/errordetail/mobilesdk/gatewaytimeout" ->
        MerchantBackendProblem.Server.MobileSDK.BackendConnectionTimeout(json)
    "https://api.payex.com/psp/errordetail/mobilesdk/badgateway" ->
        parseBadGatewayProblem(json)
    "https://api.payex.com/psp/errordetail/systemerror" ->
        MerchantBackendProblem.Server.SwedbankPay.SystemError(json)
    "https://api.payex.com/psp/errordetail/configurationerror" ->
        MerchantBackendProblem.Server.SwedbankPay.ConfigurationError(json)
    else -> throw IllegalArgumentException()
}

// a https://api.payex.com/psp/errordetail/mobilesdk/badgateway
// problem will have a "gatewayStatus" field if and only if it originated from a bogus
// Swedbank response.
private fun parseBadGatewayProblem(json: JsonObject) =
    when (val gatewayStatus = json["gatewayStatus"]?.asIntOrNull) {
        null -> MerchantBackendProblem.Server.MobileSDK.BackendConnectionFailure(json)
        else -> MerchantBackendProblem.Server.MobileSDK.InvalidBackendResponse(json, gatewayStatus)
    }
