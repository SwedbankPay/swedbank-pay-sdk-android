package com.swedbankpay.mobilesdk.nativepayments

import com.swedbankpay.mobilesdk.PaymentViewModel
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.AvailableInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.Mapper.toAvailableInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.toInstrument
import com.swedbankpay.mobilesdk.nativepayments.model.request.util.RequestDataUtil.getRequestDataIfAny
import com.swedbankpay.mobilesdk.nativepayments.model.response.Instrument
import com.swedbankpay.mobilesdk.nativepayments.model.response.MethodBaseModel
import com.swedbankpay.mobilesdk.nativepayments.model.response.Operation
import com.swedbankpay.mobilesdk.nativepayments.model.response.Problem
import com.swedbankpay.mobilesdk.nativepayments.model.response.Rel
import com.swedbankpay.mobilesdk.nativepayments.model.response.RequestMethod
import com.swedbankpay.mobilesdk.nativepayments.model.response.Session
import java.net.URL

internal object SessionOperationHandler {

    /**
     * This method will figure out what next step in the payment session will be
     *
     * @param session Current session
     * @param instrument Chosen [Instrument] for the payment.
     */
    fun getNextStep(
        session: Session?,
        paymentState: PaymentViewModel.State,
        instrument: PaymentAttemptInstrument? = null,
    ): OperationStep {
        if (session == null) {
            return OperationStep(
                instruction = StepInstruction.SessionNotFound()
            )
        }

        // Extract every operation we have on the session object
        var operations: List<Operation> =
            session.operations + session.paymentSession.methods.flatMap {
                it?.operations ?: listOf()
            }

        // If we find a problem. Use that
        if (session.problem != null) {
            val problemOperation = session.problem.operation
            operations = listOf(problemOperation)

            val acknowledgeFailedAttempt =
                operations.firstOrNull() { it.rel == Rel.ACKNOWLEDGE_FAILED_ATTEMPT }
            if (acknowledgeFailedAttempt != null) {
                return OperationStep(
                    requestMethod = acknowledgeFailedAttempt.method,
                    url = URL(acknowledgeFailedAttempt.href),
                    instruction = StepInstruction.ProblemOccurred(session.problem)
                )
            }
        } else {
            // If we find a operation where next is true use that
            val op = operations.firstOrNull { it.next }
            if (op != null) {
                operations = listOf(op)
            }
        }

        // 1. Search for Rel.PREPARE_PAYMENT
        val preparePayment = operations.firstOrNull { it.rel == Rel.PREPARE_PAYMENT }
        if (preparePayment != null) {
            return OperationStep(
                requestMethod = preparePayment.method,
                url = URL(preparePayment.href),
                data = preparePayment.rel.getRequestDataIfAny()
            )
        }

        // 2. Search for Rel.START_PAYMENT_ATTEMPT
        val startPaymentAttempt = operations.firstOrNull { it.rel == Rel.START_PAYMENT_ATTEMPT }
        if (startPaymentAttempt != null) {
            return OperationStep(
                requestMethod = startPaymentAttempt.method,
                url = URL(startPaymentAttempt.href),
                data = startPaymentAttempt.rel.getRequestDataIfAny(instrument)
            )
        }

        // 3. Search for Rel.LAUNCH_CLIENT_APP
        // Only do this step if payment is not complete
        // We don't want to try open for example swish if we already have done that
        if (paymentState != PaymentViewModel.State.COMPLETE) {
            val launchClientApp = operations.flatMap { it.tasks }
                .firstOrNull { task -> task.rel == Rel.LAUNCH_CLIENT_APP }
            if (launchClientApp != null) {
                return OperationStep(
                    instruction = StepInstruction.LaunchSwishAppStep(launchClientApp.href)
                )
            }
        }

        // 4. Search for Rel.REDIRECT_PAYER
        val redirectPayer = operations.firstOrNull { it.rel == Rel.REDIRECT_PAYER }
        if (redirectPayer != null) {
            return OperationStep(
                instruction = StepInstruction.PaymentSessionCompleted(redirectPayer.href)
            )
        }

        // 5. Search for Rel.EXPAND_METHOD
        // If we find something here extract every instrument with Rel.EXPAND_METHOD
        // and show for the merchant app
        val expandMethod = operations.firstOrNull { it.rel == Rel.EXPAND_METHOD }
        if (expandMethod != null) {
            val availableInstruments = arrayListOf<MethodBaseModel>()

            session.paymentSession.methods.forEach { method ->
                if (method?.operations?.firstOrNull { op -> op.rel == Rel.EXPAND_METHOD } != null) {
                    availableInstruments.add(method)
                }
            }

            return OperationStep(
                requestMethod = expandMethod.method,
                url = URL(expandMethod.href),
                instruction = StepInstruction
                    .AvailableInstrumentStep(availableInstruments = availableInstruments.mapNotNull { it.toAvailableInstrument() })
            )
        }

        // 6. Search for Rel.GET_PAYMENT
        // If we come here and find Rel.GET_PAYMENT we want to start polling for a result we can
        // do something with
        val getPayment = operations.firstOrNull { it.rel == Rel.GET_PAYMENT }
        if (getPayment != null) {
            return OperationStep(
                requestMethod = getPayment.method,
                url = URL(getPayment.href),
                data = getPayment.rel.getRequestDataIfAny(instrument),
                delayRequestDuration = 2000
            )
        }

        return OperationStep(
            instruction = StepInstruction.StepNotFound()
        )
    }

    /**
     * This method is used to get an operation to start the payment with chosen instrument.
     *
     * Payment can be in two states when calling this method.
     * First time calling this method will result in an operation containing [Rel.EXPAND_METHOD].
     * If problem occurs and you call this method again with the same instrument you will have
     * an operation containing [Rel.START_PAYMENT_ATTEMPT] or if a polling has occurred it will have [Rel.GET_PAYMENT].
     * If you call it again after the problem with a different instrument you will again have [Rel.EXPAND_METHOD]
     * on that operation.
     */
    fun getOperationStepForPaymentAttempt(
        session: Session?,
        paymentAttemptInstrument: PaymentAttemptInstrument
    ): OperationStep {
        if (session == null) {
            return OperationStep(
                instruction = StepInstruction.SessionNotFound()
            )
        }

        val op = session.paymentSession.methods
            .firstOrNull { it?.instrument == paymentAttemptInstrument.toInstrument() }?.operations
            ?.firstOrNull {
                it.rel == Rel.EXPAND_METHOD
                        || it.rel == Rel.START_PAYMENT_ATTEMPT
                        || it.rel == Rel.GET_PAYMENT
            }

        return if (op != null) {
            OperationStep(
                requestMethod = op.method,
                url = URL(op.href),
                data = op.rel.getRequestDataIfAny(paymentAttemptInstrument)
            )
        } else {
            OperationStep(
                instruction = StepInstruction.StepNotFound()
            )
        }
    }
}

sealed class StepInstruction(
    val informMerchantApp: Boolean = false,
    val errorMessage: String? = null,
) {
    class AvailableInstrumentStep(val availableInstruments: List<AvailableInstrument>) :
        StepInstruction(true)

    class LaunchSwishAppStep(val uri: String) : StepInstruction(true)

    class PaymentSessionCompleted(val url: String) : StepInstruction(true)

    class StepNotFound(message: String = "Step not found") : StepInstruction(true, message)

    class SessionNotFound(message: String = "Session not found") : StepInstruction(true, message)

    class ProblemOccurred(val problem: Problem) : StepInstruction(true)

}

data class OperationStep(
    val requestMethod: RequestMethod = RequestMethod.GET,
    val url: URL? = null,
    val data: String? = null,
    val instruction: StepInstruction? = null,
    val delayRequestDuration: Long = 0
)