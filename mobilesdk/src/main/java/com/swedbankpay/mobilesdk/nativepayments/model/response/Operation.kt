package com.swedbankpay.mobilesdk.nativepayments.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Operation(
    @SerializedName("contentType")
    val contentType: String,
    @SerializedName("expects")
    val expects: List<Any>,
    @SerializedName("href")
    val href: String,
    @SerializedName("method")
    val method: RequestMethod,
    @SerializedName("next")
    val next: Boolean,
    @SerializedName("rel")
    val rel: Rel,
    @SerializedName("tasks")
    val tasks: List<Task>
)

enum class Rel {
    @SerializedName("prepare-payment")
    PREPARE_PAYMENT,

    @SerializedName("expand-method")
    EXPAND_METHOD,

    @SerializedName("start-payment-attempt")
    START_PAYMENT_ATTEMPT,

    @SerializedName("launch-client-app")
    LAUNCH_CLIENT_APP,

    @SerializedName("get-payment")
    GET_PAYMENT,

    @SerializedName("redirect-payer")
    REDIRECT_PAYER,

    @SerializedName("failed")
    FAILED
}

enum class RequestMethod {
    @SerializedName("GET")
    GET,

    @SerializedName("POST")
    POST
}