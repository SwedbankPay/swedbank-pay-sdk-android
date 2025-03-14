package com.swedbankpay.mobilesdk.paymentsession.exposedmodel.mapper

import com.swedbankpay.mobilesdk.paymentsession.api.model.response.CreditCardMethodModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.CreditCardMethodPrefillModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.GooglePayMethodModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.MethodBaseModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.SwishMethodModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.SwishMethodPrefillModel
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.AvailableInstrument
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.CreditCardPrefill
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.SwishPrefill
import java.text.SimpleDateFormat
import java.util.*

/**
 * For logging purposes
 */
fun List<MethodBaseModel>.toSemiColonSeparatedString() = this.joinToString(separator = ";") {
    it.paymentMethod?.name ?: "Unknown"
}

fun MethodBaseModel.toAvailableInstrument(
): AvailableInstrument = when (this) {
    is SwishMethodModel -> AvailableInstrument.Swish(
        paymentMethod = this.paymentMethod?.name ?: "Swish",
        prefills = this.prefills.map { model ->
            SwishPrefill(
                rank = (model as SwishMethodPrefillModel).rank,
                msisdn = model.msisdn
            )
        }
    )

    is CreditCardMethodModel -> AvailableInstrument.CreditCard(
        paymentMethod = this.paymentMethod?.name ?: "CreditCard",
        prefills = this.prefills.map { model ->
            CreditCardPrefill(
                rank = (model as CreditCardMethodPrefillModel).rank,
                paymentToken = model.paymentToken,
                cardBrand = model.cardBrand,
                maskedPan = model.maskedPan,
                expiryDate = model.expiryDate,
                expiryMonth = model.expiryDate?.month() ?: "",
                expiryYear = model.expiryDate?.year() ?: "",
                expiryString = model.expiryDate?.getExpiryString() ?: "",
            )
        }
    )

    is GooglePayMethodModel -> AvailableInstrument.GooglePay(
        paymentMethod = this.paymentMethod?.name ?: "GooglePay",
    )

    else -> AvailableInstrument.WebBased(paymentMethod = this.paymentMethod?.name ?: "WebBased")
}

fun Date.month(): String {
    return SimpleDateFormat("MM", Locale.getDefault()).format(this)
}

fun Date.year(): String {
    return SimpleDateFormat("yy", Locale.getDefault()).format(this)
}

fun Date.getExpiryString(): String {
    return SimpleDateFormat("MM/yy", Locale.getDefault()).format(this)
}