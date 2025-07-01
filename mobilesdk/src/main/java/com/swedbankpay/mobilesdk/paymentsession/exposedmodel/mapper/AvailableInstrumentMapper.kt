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
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
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
                expiryMonth = model.expiryDate?.formatUsingPattern("MM") ?: "",
                expiryYear = model.expiryDate?.formatUsingPattern("yy") ?: "",
                expiryString = model.expiryDate?.formatUsingPattern("MM/yy") ?: "",
            )
        }
    )

    is GooglePayMethodModel -> AvailableInstrument.GooglePay(
        paymentMethod = this.paymentMethod?.name ?: "GooglePay",
    )

    else -> AvailableInstrument.WebBased(paymentMethod = this.paymentMethod?.name ?: "WebBased")
}

private fun Date.formatUsingPattern(pattern : String) : String {
    val date = DateTimeUtils.toInstant(this)
    return DateTimeFormatter.ofPattern(pattern).format(date.atZone(ZoneOffset.UTC))
}