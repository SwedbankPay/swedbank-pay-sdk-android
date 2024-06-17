package com.swedbankpay.mobilesdk.paymentsession.api.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class PaymentSessionModel(
    @SerializedName("culture")
    val culture: String?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("methods")
    val methods: List<MethodBaseModel?>? = listOf(),
    @SerializedName("payeeName")
    val payeeName: String?,
    @SerializedName("settings")
    val settings: Settings?,
    @SerializedName("urls")
    val urls: UrlsModel?
) {
    val allMethodOperations: List<OperationOutputModel> =
        methods?.flatMap { it?.operations ?: listOf() } ?: listOf()
}