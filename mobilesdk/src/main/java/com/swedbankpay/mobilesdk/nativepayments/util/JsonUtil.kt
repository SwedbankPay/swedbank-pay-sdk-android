package com.swedbankpay.mobilesdk.nativepayments.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.nativepayments.util.gson.PrefillBaseModelDeserializer
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.NativePaymentError
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.PrefillBaseModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.Session
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.methodBaseModelFactory

internal object JsonUtil {

    private val gson: Gson =
        GsonBuilder()
            .registerTypeAdapterFactory(methodBaseModelFactory)
            .registerTypeAdapter(PrefillBaseModel::class.java, PrefillBaseModelDeserializer())
            .create()

    fun String.toSessionModel(): Session {
        val session = gson.fromJson(this, Session::class.java)
        // Remove nulls from operations
        // This is done because of how GSON serialize things
        return session.copy(paymentSession = session.paymentSession.copy(methods = session.paymentSession.methods?.filterNotNull()))
    }

    fun String.toPaymentErrorModel(): NativePaymentError =
        gson.fromJson(this, NativePaymentError::class.java)


}