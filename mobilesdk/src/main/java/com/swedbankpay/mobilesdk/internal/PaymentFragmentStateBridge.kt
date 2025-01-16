package com.swedbankpay.mobilesdk.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.swedbankpay.mobilesdk.PaymentViewModel.State


/**
 * This object acts like a bridge between web based payments and the payment session
 */
internal object PaymentFragmentStateBridge {
    val paymentProcessState: MutableLiveData<State?> = MutableLiveData()
    val paymentFragmentState: LiveData<State?> = paymentProcessState
}