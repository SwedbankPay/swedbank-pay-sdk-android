package com.swedbankpay.mobilesdk.paymentsession.api.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OperationOutputModel(
    @SerializedName("contentType")
    val contentType: String?,
    @SerializedName("expects")
    val expects: List<ExpectationModel>? = listOf(),
    @SerializedName("href")
    val href: String?,
    @SerializedName("method")
    val method: RequestMethod?,
    @SerializedName("next")
    val next: Boolean?,
    @SerializedName("rel")
    val rel: OperationRel?,
    @SerializedName("tasks")
    var tasks: List<IntegrationTask?>? = listOf()
)

@Keep
enum class OperationRel {
    @SerializedName("prepare-payment")
    PREPARE_PAYMENT,

    @SerializedName("expand-method")
    EXPAND_METHOD,

    @SerializedName("start-payment-attempt")
    START_PAYMENT_ATTEMPT,

    @SerializedName("get-payment")
    GET_PAYMENT,

    @SerializedName("redirect-payer")
    REDIRECT_PAYER,

    @SerializedName("acknowledge-failed-attempt")
    ACKNOWLEDGE_FAILED_ATTEMPT,

    @SerializedName("abort-payment")
    ABORT_PAYMENT,

    @SerializedName("event-logging")
    EVENT_LOGGING,

    @SerializedName("create-authentication")
    CREATE_AUTHENTICATION,

    @SerializedName("complete-authentication")
    COMPLETE_AUTHENTICATION,

    @SerializedName("view-payment")
    VIEW_PAYMENT,

    @SerializedName("customize-payment")
    CUSTOMIZE_PAYMENT,

    @SerializedName("attempt-payload")
    ATTEMPT_PAYLOAD,

    @SerializedName("fail-payment-attempt")
    FAIL_PAYMENT_ATTEMPT
}

@Keep
enum class RequestMethod {
    @SerializedName("GET")
    GET,

    @SerializedName("POST")
    POST
}