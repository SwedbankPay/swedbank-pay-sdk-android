package com.swedbankpay.mobilesdk.nativepayments.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.swedbankpay.mobilesdk.RuntimeTypeAdapterFactory

@Keep
open class MethodBaseModel {
    @SerializedName("amount")
    val amount: Int? = null

    @SerializedName("autoClick")
    val autoClick: Boolean = false

    @SerializedName("instrument")
    val instrument: Instrument? = null

    @SerializedName("operations")
    val operations: List<Operation> = listOf()

    override fun toString(): String {
        return "amount=$amount, autoClick=$autoClick, instrument=$instrument, operations=$operations"
    }
}

@Keep
class SwishMethodModel : MethodBaseModel() {
    @SerializedName("isMobileDevice")
    val isMobileDevice: Boolean = false

    @SerializedName("prefills")
    val prefills: List<PrefillBaseModel> = listOf()
    override fun toString(): String {
        return "SwishMethodModel(isMobileDevice=$isMobileDevice, prefills=$prefills, ${super.toString()})"
    }
}

@Keep
class CreditCardMethodModel : MethodBaseModel() {
    @SerializedName("cardBrands")
    val cardBrands: List<String> = listOf()

    @SerializedName("prefills")
    val prefills: List<PrefillBaseModel> = listOf()

    override fun toString(): String {
        return "CreditCardMethodModel(cardBrands=$cardBrands, prefills=$prefills, ${super.toString()})"
    }
}

@Keep
class GooglePayMethodModel : MethodBaseModel() {
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

enum class Instrument(name: String) {
    @SerializedName("Swish")
    SWISH("Swish"),

    @SerializedName("CreditCard")
    CREDIT_CARD("CreditCard"),
}

// Type factory is used to be able to parse the different method models
// If the method is not present in the registerSubtype. It will not show up in the parsed response
val methodBaseModelFactory: RuntimeTypeAdapterFactory<MethodBaseModel>? = RuntimeTypeAdapterFactory
    .of(MethodBaseModel::class.java, "instrument", true)
    .registerSubtype(SwishMethodModel::class.java, "Swish")
    .registerSubtype(CreditCardMethodModel::class.java, "CreditCard")

