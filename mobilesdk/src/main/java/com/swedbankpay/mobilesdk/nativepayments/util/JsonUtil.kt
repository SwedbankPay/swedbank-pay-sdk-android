package com.swedbankpay.mobilesdk.nativepayments.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.nativepayments.model.response.NativePaymentError
import com.swedbankpay.mobilesdk.nativepayments.model.response.Session
import com.swedbankpay.mobilesdk.nativepayments.model.response.methodBaseModelFactory
import com.swedbankpay.mobilesdk.nativepayments.model.response.prefillBaseModelFactory

object JsonUtil {

    private val gson: Gson =
        GsonBuilder()
            .registerTypeAdapterFactory(methodBaseModelFactory)
            .registerTypeAdapterFactory(prefillBaseModelFactory)
            .create()

    fun String.toSessionModel(): Session {
        val session = gson.fromJson(this, Session::class.java)
        // Remove nulls from operations
        // This is done because of how GSON serialize
        return session.copy(paymentSession = session.paymentSession.copy(methods = session.paymentSession.methods.filterNotNull()))
    }

    fun String.toPaymentErrorModel(): NativePaymentError =
        gson.fromJson(this, NativePaymentError::class.java)

}