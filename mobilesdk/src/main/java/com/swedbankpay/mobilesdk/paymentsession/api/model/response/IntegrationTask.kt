package com.swedbankpay.mobilesdk.paymentsession.api.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class IntegrationTask(
    @SerializedName("contentType")
    val contentType: String?,
    @SerializedName("expects")
    val expects: List<ExpectationModel?>?,
    @SerializedName("href")
    val href: String?,
    @SerializedName("method")
    val method: RequestMethod?,
    @SerializedName("rel")
    val rel: IntegrationTaskRel?
) {
    fun getExpectValuesFor(name: String): ExpectationModel? {
        return expects?.firstOrNull { it?.name == name }
    }
}

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

    @SerializedName("wallet-sdk")
    WALLET_SDK
}

internal fun List<ExpectationModel>.getValueFor(name: String): Any? {
    return this.firstOrNull { it.name == name }?.value
}

internal fun List<ExpectationModel>.getStringValueFor(name: String): String? {
    val model = this.firstOrNull { it.name == name }
    return try {
        model?.let {
            if (it.type == "string") {
                it.value as String
            } else null
        }
    } catch (e: Exception) {
        null
    }
}

@Suppress("UNCHECKED_CAST")
internal fun List<ExpectationModel>.getStringArrayValueFor(name: String): List<String> {
    val model = this.firstOrNull { it.name == name }
    return try {
        model?.let {
            if (it.type == "string[]") {
                return it.value as List<String>
            } else listOf()
        } ?: listOf()
    } catch (e: Exception) {
        listOf()
    }
}

internal fun List<ExpectationModel>.getBooleanValueFor(name: String): Boolean? {
    val model = this.firstOrNull { it.name == name }
    return try {
        model?.let {
            when (it.type) {
                "bool" -> it.value as Boolean
                "string" -> (it.value as String).lowercase().toBooleanStrictOrNull()
                else -> null
            }
        }
    } catch (e: Exception) {
        null
    }
}


