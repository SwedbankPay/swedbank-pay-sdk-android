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
     * Called when Google Pay payment readiness is known
     *
     * @param isReadyToPay A boolean indicating whether the user's device is capable of making payments with Google Pay
     * @param isReadyToPayWithExistingPaymentMethod A boolean indicating whether the user has at least one card available for payment with the payment session
     */
    class GooglePayPaymentReadinessFetched(
        val isReadyToPay: Boolean,
        val isReadyToPayWithExistingPaymentMethod: Boolean
    ) : PaymentSessionState()

    /**
     * Called when a 3D secure view needs to be presented.
     *
     * @param fragment Fragment with 3D secure view.
     */
    class Show3DSecureFragment(val fragment: Fragment) : PaymentSessionState()

    /**
     * Called whenever the 3D secure view can be dismissed.
     */
    object Dismiss3DSecureFragment : PaymentSessionState()

    /**
     * Called when payment fragment has been created and can be shown
     */
    class ShowPaymentFragment(val fragment: Fragment) : PaymentSessionState()

    /**
     * Called whenever the payment session has been completed
     */
    object PaymentSessionComplete : PaymentSessionState()

    /**
     * Called whenever the payment session has been canceled for any reason
     */
    object PaymentSessionCanceled : PaymentSessionState()

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