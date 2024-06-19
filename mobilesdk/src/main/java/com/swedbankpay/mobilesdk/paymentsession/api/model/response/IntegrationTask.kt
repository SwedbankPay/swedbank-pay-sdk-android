package com.swedbankpay.mobilesdk.paymentsession.api.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class IntegrationTask(
    @SerializedName("contentType")
    val contentType: String?,
    @SerializedName("expects")
    val expects: List<ExpectationModel>?,
    @SerializedName("href")
    val href: String?,
    @SerializedName("method")
    val method: RequestMethod?,
    @SerializedName("rel")
    val rel: IntegrationTaskRel?
) {
    fun getExpectValuesFor(name: String): ExpectationModel? {
        return expects?.firstOrNull { it.name == name }
    }
}

internal const val threeDSMethodData = "threeDSMethodData"
internal const val creq = "creq"

@Keep
data class ExpectationModel(
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("value")
    val value: Any?
)

@Keep
enum class IntegrationTaskRel {
    @SerializedName("launch-client-app")
    LAUNCH_CLIENT_APP,

    @SerializedName("sca-method-request")
    SCA_METHOD_REQUEST,

    @SerializedName("sca-redirect")
    SCA_REDIRECT,
}


