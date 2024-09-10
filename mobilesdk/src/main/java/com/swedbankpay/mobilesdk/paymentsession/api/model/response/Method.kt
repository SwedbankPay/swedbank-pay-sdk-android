package com.swedbankpay.mobilesdk.paymentsession.api.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
open class MethodBaseModel {
    @SerializedName("amount")
    val amount: Int? = null

    @SerializedName("autoClick")
    val autoClick: Boolean = false

    @SerializedName("instrument")
    val instrument: Instrument? = null

    @SerializedName("operations")
    val operations: List<OperationOutputModel?> = listOf()

    override fun toString(): String {
        return "amount=$amount, autoClick=$autoClick, instrument=$instrument, operations=$operations"
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
    @SerializedName("usFormattedAmount")
    val usFormattedAmount: String? = null

    @SerializedName("requestDeliveryInfo")
    val requestDeliveryInfo: Boolean = false

    @SerializedName("cardBrands")
    val cardBrands: List<String> = listOf()

    @SerializedName("gateway")
    val gateWay: String? = null

    @SerializedName("gatewayMerchantId")
    val gatewayMerchantId: String? = null

    @SerializedName("merchantId")
    val merchantId: String? = null

    @SerializedName("countryCode")
    val countryCode: String? = null
    override fun toString(): String {
        return "GooglePayMethodModel(usFormattedAmount=$usFormattedAmount, requestDeliveryInfo=$requestDeliveryInfo, cardBrands=$cardBrands, gateWay=$gateWay, gatewayMerchantId=$gatewayMerchantId, merchantId=$merchantId, countryCode=$countryCode, ${super.toString()})"
    }
}

@Keep
sealed class Instrument {
    abstract val identifier: String

    data class Swish(
        override val identifier: String
    ) : Instrument()

    data class CreditCard(
        override val identifier: String
    ) : Instrument()

    data class WebBased(
        override val identifier: String
    ) : Instrument()
}

