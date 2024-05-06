package com.swedbankpay.mobilesdk.nativepayments.exposedmodel.Mapper

import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.AvailableInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.CreditCardPrefill
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.SwishPrefill
import com.swedbankpay.mobilesdk.nativepayments.model.response.CreditCardMethodModel
import com.swedbankpay.mobilesdk.nativepayments.model.response.CreditCardPrefillModel
import com.swedbankpay.mobilesdk.nativepayments.model.response.MethodBaseModel
import com.swedbankpay.mobilesdk.nativepayments.model.response.SwishMethodModel
import com.swedbankpay.mobilesdk.nativepayments.model.response.SwishPrefillModel

internal fun MethodBaseModel.toAvailableInstrument(): AvailableInstrument? = when (this) {
    is SwishMethodModel -> AvailableInstrument.Swish(
        prefill = this.prefills.map { model ->
            SwishPrefill(
                rank = (model as SwishPrefillModel).rank,
                msisdn = model.msisdn
            )
        }
    )

    is CreditCardMethodModel -> AvailableInstrument.CreditCard(
        prefill = this.prefills.map { model ->
            CreditCardPrefill(
                rank = (model as CreditCardPrefillModel).rank,
                paymentToken = model.paymentToken,
                cardBrand = model.cardBrand,
                maskedPen = model.maskedPen,
                expiryDate = model.expiryDate
            )
        }
    )

    else -> {
        null
    }
}