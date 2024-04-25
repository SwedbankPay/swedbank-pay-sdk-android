package com.swedbankpay.mobilesdk.nativepayments.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.RuntimeTypeAdapterFactory

@Keep
open class PrefillBaseModel {
    @SerializedName("rank")
    val rank: Int? = null
}

@Keep
class SwishPrefillModel : PrefillBaseModel() {
    @SerializedName("msisdn")
    val msisdn: String? = null
}

@Keep
class CreditCardPrefillModel : PrefillBaseModel() {
    @SerializedName("paymentToken")
    val paymentToken: String? = null

    @SerializedName("cardBrand")
    val cardBrand: String? = null

    @SerializedName("maskedPen")
    val maskedPen: String? = null

    @SerializedName("expiryDate")
    val expiryDate: String? = null
}

// Type factory is used to be able to parse the different method models
// If the method is not present in the registerSubtype. It will not show up in the parsed response
val prefillBaseModelFactory: RuntimeTypeAdapterFactory<PrefillBaseModel>? =
    RuntimeTypeAdapterFactory
        .of(PrefillBaseModel::class.java, "instrument", true)
        .registerSubtype(SwishPrefillModel::class.java, "Swish")
        .registerSubtype(CreditCardPrefillModel::class.java, "CreditCard")



