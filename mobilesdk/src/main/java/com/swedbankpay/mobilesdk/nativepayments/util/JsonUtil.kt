package com.swedbankpay.mobilesdk.nativepayments.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.nativepayments.util.gson.PrefillBaseModelDeserializer
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.NativePaymentError
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.PrefillBaseModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.PaymentOutputModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.methodBaseModelFactory

internal object JsonUtil {

    private val gson: Gson =
        GsonBuilder()
            .registerTypeAdapterFactory(methodBaseModelFactory)
            .registerTypeAdapter(PrefillBaseModel::class.java, PrefillBaseModelDeserializer())
            .create()

    fun String.toPaymentOutputModel(): PaymentOutputModel {
        val paymentOutputModel = gson.fromJson(this, PaymentOutputModel::class.java)
        // Remove nulls from operations
        // This is done because of how GSON serialize things
        return paymentOutputModel.copy(
            paymentSession = paymentOutputModel.paymentSession.copy(
                methods = paymentOutputModel.paymentSession.methods?.filterNotNull()
            )
        )
    }

    fun String.toPaymentErrorModel(): NativePaymentError =
        gson.fromJson(this, NativePaymentError::class.java)


}