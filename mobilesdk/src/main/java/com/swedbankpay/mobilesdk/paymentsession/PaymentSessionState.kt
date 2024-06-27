package com.swedbankpay.mobilesdk.paymentsession

import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ProblemDetails
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.AvailableInstrument
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentSessionProblem

/**
 * This class is exposed to the merchant app through live data.
 * Will tell the state of the ongoing payment session
 */
@Keep
sealed class PaymentSessionState {

    /**
     * Called when an list of available instruments is known.
     *
     * @param availableInstruments List of different [AvailableInstrument] that can be used fo the payment session
     */
    class PaymentSessionFetched(val availableInstruments: List<AvailableInstrument>) :
        PaymentSessionState()

    /**
     * Called when 3d-secure should be shown
     */
    class Show3dSecure(val fragment: Fragment) : PaymentSessionState()

    /**
     * Called when 3d-secure can be dismissed
     */
    object Dismiss3dSecure : PaymentSessionState()

    /**
     * Called whenever the payment has been completed
     */
    object PaymentComplete : PaymentSessionState()

    /**
     * Called whenever the payment has been canceled for any reason
     */
    object PaymentCanceled : PaymentSessionState()

    /**
     *  Called if there is a session problem with performing the payment.
     *
     *  @param problem [ProblemDetails] that caused the failure
     */
    class SessionProblemOccurred(val problem: ProblemDetails) : PaymentSessionState()

    /**
     *  Called if there is a SDK problem with performing the payment.
     *
     *  @param problem [PaymentSessionProblem] that caused the failure
     */
    class SdkProblemOccurred(val problem: PaymentSessionProblem) : PaymentSessionState()

    /**
     * Default state
     */
    object Idle : PaymentSessionState()

}