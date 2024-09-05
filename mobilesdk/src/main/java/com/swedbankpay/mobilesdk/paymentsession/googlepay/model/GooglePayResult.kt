package se.vettefors.googlepaytest.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class GooglePayResult(
    @SerializedName("apiVersion")
    val apiVersion: Int,
    @SerializedName("apiVersionMinor")
    val apiVersionMinor: Int,
    @SerializedName("paymentMethodData")
    val paymentMethodData: PaymentMethodData
)

@Keep
internal data class PaymentMethodData(
    @SerializedName("description")
    val description: String,
    @SerializedName("info")
    val info: Info,
    @SerializedName("tokenizationData")
    val tokenizationData: TokenizationData,
    @SerializedName("type")
    val type: String
)

@Keep
internal data class Info(
    @SerializedName("assuranceDetails")
    val assuranceDetails: AssuranceDetails,
    @SerializedName("cardDetails")
    val cardDetails: String,
    @SerializedName("cardNetwork")
    val cardNetwork: String
)

@Keep
internal data class AssuranceDetails(
    @SerializedName("accountVerified")
    val accountVerified: Boolean,
    @SerializedName("cardHolderAuthenticated")
    val cardHolderAuthenticated: Boolean
)

@Keep
internal data class TokenizationData(
    @SerializedName("token")
    val token: String,
    @SerializedName("type")
    val type: String
)