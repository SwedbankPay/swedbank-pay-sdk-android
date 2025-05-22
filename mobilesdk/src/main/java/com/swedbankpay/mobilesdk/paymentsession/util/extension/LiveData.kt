package com.swedbankpay.mobilesdk.paymentsession.util.extension

import androidx.lifecycle.MutableLiveData
import com.swedbankpay.mobilesdk.paymentsession.ScaResult

fun MutableLiveData<ScaResult?>.setValueIfChanged(newValue: ScaResult) {
    if (newValue != value) {
        value = newValue
    }
}