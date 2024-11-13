package com.swedbankpay.mobilesdk.paymentsession.api.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.paymentsession.util.extension.safeLet

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
    val urls: UrlsModel?,
    @SerializedName("instrumentModePaymentMethod")
    val instrumentModePaymentMethod: String?,
    @SerializedName("paymentMethod")
    val paymentMethod: String?
) {
    val allMethodOperations: List<OperationOutputModel> =
        methods?.flatMap { it?.operations ?: listOf() }?.filterNotNull() ?: listOf()

    val allPaymentMethods: List<String> =
        methods?.mapNotNull { it?.paymentMethod?.name } ?: listOf()

    val restrictedToInstruments: List<String>?
        get() {
            return safeLet(allPaymentMethods, settings) { allMethods, settings ->
                if (allMethods.sorted() == settings.enabledPaymentMethods.sorted()) {
                    return null
                } else return allMethods
            }
        }
}