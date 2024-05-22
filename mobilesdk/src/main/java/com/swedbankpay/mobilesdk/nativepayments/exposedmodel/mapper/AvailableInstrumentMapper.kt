package com.swedbankpay.mobilesdk.nativepayments.exposedmodel.mapper

import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.AvailableInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.CreditCardPrefill
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.SwishPrefill
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.CreditCardMethodModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.CreditCardMethodPrefillModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.MethodBaseModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.SwishMethodModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.SwishMethodPrefillModel

fun MethodBaseModel.toAvailableInstrument(): AvailableInstrument? = when (this) {
    is SwishMethodModel -> AvailableInstrument.Swish(
        prefills = this.prefills.map { model ->
            SwishPrefill(
                rank = (model as SwishMethodPrefillModel).rank,
                msisdn = model.msisdn
            )
        }
    )

    is CreditCardMethodModel -> AvailableInstrument.CreditCard(
        prefills = this.prefills.map { model ->
            CreditCardPrefill(
                rank = (model as CreditCardMethodPrefillModel).rank,
                paymentToken = model.paymentToken,
                cardBrand = model.cardBrand,
                maskedPan = model.maskedPan,
                expiryDate = model.expiryDate
            )
        }
    )

    else -> {
        null
    }
}