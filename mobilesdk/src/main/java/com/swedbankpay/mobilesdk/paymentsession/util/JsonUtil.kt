package com.swedbankpay.mobilesdk.paymentsession.util

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ApiError
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.Instrument
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.MethodBaseModel
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
            .registerTypeAdapter(Instrument::class.java, InstrumentDeserializer())
            .create()

    fun String.toPaymentOutputModel(): PaymentOutputModel {
        v("helloproblem", this)
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

    var _charLimit = 1000

    @JvmStatic
    fun v(tag: String?, message: String): Int {
        // If the message is less than the limit just show
        if (message.length < _charLimit) {
            return Log.v(tag, message)
        }
        val sections = message.length / _charLimit
        for (i in 0..sections) {
            val max = _charLimit * (i + 1)
            if (max >= message.length) {
                Log.v(tag, message.substring(_charLimit * i))
            } else {
                Log.v(tag, message.substring(_charLimit * i, max))
            }
        }
        return 1
    }

}