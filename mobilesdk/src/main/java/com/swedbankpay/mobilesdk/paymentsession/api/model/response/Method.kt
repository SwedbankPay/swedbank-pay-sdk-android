package com.swedbankpay.mobilesdk.paymentsession.api.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
open class MethodBaseModel {
    @SerializedName("amount")
    val amount: Int? = null

    @SerializedName("autoClick")
    val autoClick: Boolean = false

    @SerializedName("paymentMethod")
    val paymentMethod: PaymentMethod? = null

    @SerializedName("operations")
    val operations: List<OperationOutputModel?> = listOf()

    override fun toString(): String {
        return "amount=$amount, autoClick=$autoClick, paymentMethod=$paymentMethod, operations=$operations"
    }
}

@Keep
internal class SwishMethodModel : MethodBaseModel() {
    @SerializedName("isMobileDevice")
    val isMobileDevice: Boolean = false

    @SerializedName("prefills")
    val prefills: List<PrefillBaseModel> = listOf()
    override fun toString(): String {
        return "SwishMethodModel(isMobileDevice=$isMobileDevice, prefills=$prefills, ${super.toString()})"
    }
}

@Keep
internal class CreditCardMethodModel : MethodBaseModel() {
    @SerializedName("cardBrands")
    val cardBrands: List<String> = listOf()

    @SerializedName("prefills")
    val prefills: List<PrefillBaseModel> = listOf()

    override fun toString(): String {
        return "CreditCardMethodModel(cardBrands=$cardBrands, prefills=$prefills, ${super.toString()})"
    }
}

@Keep
internal class WebBasedMethodModel : MethodBaseModel() {
    override fun toString(): String {
        return "WebBasedMethodModel(${super.toString()})"
    }
}

@Keep
internal class GooglePayMethodModel : MethodBaseModel() {
    @SerializedName("cardBrands")
    val cardBrands: List<String> = listOf()

    @SerializedName("allowedCardAuthMethods")
    val allowedCardAuthMethods: List<String> = listOf()

    @SerializedName("environment")
    val environment: String = "TEST"

    override fun toString(): String {
        return "GooglePayMethodModel(cardBrands=$cardBrands, ${super.toString()})"
    }
}

@Keep
sealed class PaymentMethod {
    abstract val name: String

    data class Swish(
        override val name: String
    ) : PaymentMethod()

    data class CreditCard(
        override val name: String
    ) : PaymentMethod()

    data class GooglePay(
        override val name: String
    ) : PaymentMethod()

    data class WebBased(
        override val name: String
    ) : PaymentMethod()
}

