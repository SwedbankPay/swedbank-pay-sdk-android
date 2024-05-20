package com.swedbankpay.mobilesdk.nativepayments.exposedmodel.mapper

import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.AvailableInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.CreditCardPrefill
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.SwishPrefill
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.CreditCardMethodModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.CreditCardPrefillModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.MethodBaseModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.SwishMethodModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.SwishPrefillModel

fun MethodBaseModel.toAvailableInstrument(): AvailableInstrument? = when (this) {
    is SwishMethodModel -> AvailableInstrument.Swish(
        prefills = this.prefills.map { model ->
            SwishPrefill(
                rank = (model as SwishPrefillModel).rank,
                msisdn = model.msisdn
            )
        }
    )

    is CreditCardMethodModel -> AvailableInstrument.CreditCard(
        prefills = this.prefills.map { model ->
            CreditCardPrefill(
                rank = (model as CreditCardPrefillModel).rank,
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