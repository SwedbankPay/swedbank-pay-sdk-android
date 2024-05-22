package com.swedbankpay.mobilesdk.nativepayments.api.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal open class PrefillBaseModel {
    @SerializedName("rank")
    val rank: Int? = null
    override fun toString(): String {
        return "rank=$rank"
    }


}

@Keep
internal class SwishMethodPrefillModel : PrefillBaseModel() {
    @SerializedName("msisdn")
    val msisdn: String? = null
    override fun toString(): String {
        return "SwishPrefillModel(msisdn=$msisdn, ${super.toString()})"
    }


}

@Keep
internal class CreditCardMethodPrefillModel : PrefillBaseModel() {
    @SerializedName("paymentToken")
    val paymentToken: String? = null

    @SerializedName("cardBrand")
    val cardBrand: String? = null

    @SerializedName("maskedPan")
    val maskedPan: String? = null

    @SerializedName("expiryDate")
    val expiryDate: String? = null
    override fun toString(): String {
        return "CreditCardPrefillModel(paymentToken=$paymentToken, cardBrand=$cardBrand, maskedPan=$maskedPan, expiryDate=$expiryDate, ${super.toString()})"
    }


}




