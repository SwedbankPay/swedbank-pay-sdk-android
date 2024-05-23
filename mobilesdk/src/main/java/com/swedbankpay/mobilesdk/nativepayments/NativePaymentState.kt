package com.swedbankpay.mobilesdk.nativepayments

import com.swedbankpay.mobilesdk.nativepayments.api.model.response.ProblemDetails
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.AvailableInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.NativePaymentProblem

/**
 * This class is exposed to the merchant app through live data.
 * Will tell the state of the ongoing payment session
 */
sealed class NativePaymentState {

    /**
     * Called when an list of available instruments is known.
     *
     * @param availableInstruments List of different instruments
     */
    class AvailableInstrumentsFetched(val availableInstruments: List<AvailableInstrument>) :
        NativePaymentState()

    object PaymentComplete : NativePaymentState()

    object PaymentCanceled : NativePaymentState()

    /**
     *  Called if there is a session problem with performing the payment.
     *
     *  @param problem The problem that cause the failure
     */
    class SessionProblemOccurred(val problem: ProblemDetails) : NativePaymentState()

    /**
     *  Called if there is a SDK problem with performing the payment.
     *
     *  @param problem The problem that cause the failure
     */
    class SdkProblemOccurred(val problem: NativePaymentProblem) : NativePaymentState()

}