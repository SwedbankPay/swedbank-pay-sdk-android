package se.vettefors.googlepaytest.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GooglePayResult(
    @SerializedName("apiVersion")
    val apiVersion: Int?,
    @SerializedName("apiVersionMinor")
    val apiVersionMinor: Int?,
    @SerializedName("paymentMethodData")
    val paymentMethodData: PaymentMethodData?,
    @SerializedName("shippingAddress")
    val shippingAddress: ShippingAddress?
)

@Keep
data class PaymentMethodData(
    @SerializedName("description")
    val description: String?,
    @SerializedName("info")
    val info: Info?,
    @SerializedName("tokenizationData")
    val tokenizationData: TokenizationData?,
    @SerializedName("type")
    val type: String?
)

@Keep
data class Info(
    @SerializedName("assuranceDetails")
    val assuranceDetails: AssuranceDetails?,
    @SerializedName("billingAddress")
    val billingAddress: BillingAddress?,
    @SerializedName("cardDetails")
    val cardDetails: String?,
    @SerializedName("cardNetwork")
    val cardNetwork: String?
)

@Keep
data class AssuranceDetails(
    @SerializedName("accountVerified")
    val accountVerified: Boolean?,
    @SerializedName("cardHolderAuthenticated")
    val cardHolderAuthenticated: Boolean?
)

@Keep
data class BillingAddress(
    @SerializedName("countryCode")
    val countryCode: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("postalCode")
    val postalCode: String?
)


@Keep
data class TokenizationData(
    @SerializedName("token")
    val token: String?,
    @SerializedName("type")
    val type: String?
)

@Keep
data class ShippingAddress(
    @SerializedName("address1")
    val address1: String?,
    @SerializedName("address2")
    val address2: String?,
    @SerializedName("address3")
    val address3: String?,
    @SerializedName("administrativeArea")
    val administrativeArea: String?,
    @SerializedName("countryCode")
    val countryCode: String?,
    @SerializedName("locality")
    val locality: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("postalCode")
    val postalCode: String?,
    @SerializedName("sortingCode")
    val sortingCode: String?
)
