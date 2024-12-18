package com.swedbankpay.mobilesdk.paymentsession.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ApiError
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.MethodBaseModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PaymentMethod
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PaymentOutputModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PrefillBaseModel
import com.swedbankpay.mobilesdk.paymentsession.util.gson.InstrumentDeserializer
import com.swedbankpay.mobilesdk.paymentsession.util.gson.MethodBaseModelDeserializer
import com.swedbankpay.mobilesdk.paymentsession.util.gson.PrefillBaseModelDeserializer

internal object JsonUtil {

    private val gson: Gson =
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .registerTypeAdapter(MethodBaseModel::class.java, MethodBaseModelDeserializer())
            .registerTypeAdapter(PrefillBaseModel::class.java, PrefillBaseModelDeserializer())
            .registerTypeAdapter(PaymentMethod::class.java, InstrumentDeserializer())
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

    fun String.toApiError(): ApiError =
        gson.fromJson(this, ApiError::class.java)

}