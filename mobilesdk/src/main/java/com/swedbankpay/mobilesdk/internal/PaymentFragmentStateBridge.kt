package com.swedbankpay.mobilesdk.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.swedbankpay.mobilesdk.PaymentViewModel.State
import com.swedbankpay.mobilesdk.internal.InternalPaymentViewModel.UIState


/**
 * This object acts like a bridge between web based payments and the payment session
 */
internal object PaymentFragmentStateBridge {

    val paymentProcessUiState: MutableLiveData<UIState?> = MutableLiveData()
    val paymentMenuState: LiveData<State> = paymentProcessUiState.map {
        when (it) {
            null -> State.IDLE
            UIState.Loading -> State.IN_PROGRESS
            is UIState.ViewConsumerIdentification -> State.IN_PROGRESS
            is UIState.ViewPaymentOrder -> State.IN_PROGRESS
            is UIState.UpdatingPaymentOrder -> State.UPDATING_PAYMENT_ORDER
            is UIState.InitializationError -> State.FAILURE
            is UIState.RetryableError -> State.RETRYABLE_ERROR
            UIState.Complete -> State.COMPLETE
            UIState.Canceled -> State.CANCELED
            is UIState.Failure -> State.FAILURE
        }
    }

}