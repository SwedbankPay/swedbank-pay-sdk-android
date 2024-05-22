package com.swedbankpay.mobilesdk.nativepayments

import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.AvailableInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.toInstrument
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.util.RequestUtil.getRequestDataIfAny
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.Instrument
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.MethodBaseModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.Operation
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.Problem
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.Rel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.RequestMethod
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.Session
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.mapper.toAvailableInstrument
import java.net.URL

internal object SessionOperationHandler {

    private val alreadyUsedProblemUrls: MutableList<String> = mutableListOf()
    private val alreadyUsedSwishUrls: MutableList<String> = mutableListOf()

    /**
     * This method will figure out what the next step in the payment session will be
     *
     * @param session Current session
     * @param instrument Chosen [Instrument] for the payment.
     */
    fun getNextStep(
        session: Session?,
        instrument: PaymentAttemptInstrument? = null,
    ): OperationStep {
        if (session == null) {
            return OperationStep(
                instructions = listOf(StepInstruction.SessionNotFound())
            )
        }

        val instructions = mutableListOf<StepInstruction>()

        // Extract every operation we have on the session object
        var operations: List<Operation> =
            session.operations + (session.paymentSession.methods?.flatMap {
                it?.operations ?: listOf()
            } ?: listOf())

        // If we find a problem that we haven't used return it. Otherwise just acknowledge it and
        // add it to the instructions without informing the merchant app
        if (session.problem != null) {
            val problemOperation = session.problem.operation
            if (problemOperation.rel == Rel.ACKNOWLEDGE_FAILED_ATTEMPT) {
                if (problemOperation.href != null) {
                    if (alreadyUsedProblemUrls.contains(problemOperation.href)) {
                        instructions.add(StepInstruction.ProblemOccurred(session.problem, false))
                    } else {
                        alreadyUsedProblemUrls.add(problemOperation.href)
                        return OperationStep(
                            requestMethod = problemOperation.method,
                            url = URL(problemOperation.href),
                            instructions = listOf(
                                StepInstruction.ProblemOccurred(
                                    session.problem,
                                    true
                                )
                            )
                        )
                    }
                }
            }
        }

        // If we find a operation where next is true use that
        val op = operations.firstOrNull { it.next ?: false }
        if (op != null) {
            operations = listOf(op)
        }

        // 1. Search for Rel.PREPARE_PAYMENT
        val preparePayment = operations.firstOrNull { it.rel == Rel.PREPARE_PAYMENT }
        if (preparePayment != null) {
            return OperationStep(
                requestMethod = preparePayment.method,
                url = URL(preparePayment.href),
                data = preparePayment.rel?.getRequestDataIfAny(),
                instructions = instructions
            )
        }

        // 2. Search for Rel.START_PAYMENT_ATTEMPT
        val startPaymentAttempt = operations.firstOrNull { it.rel == Rel.START_PAYMENT_ATTEMPT }
        if (startPaymentAttempt != null) {
            return OperationStep(
                requestMethod = startPaymentAttempt.method,
                url = URL(startPaymentAttempt.href),
                data = startPaymentAttempt.rel?.getRequestDataIfAny(instrument),
                instructions = instructions
            )
        }

        // 3. Search for Rel.LAUNCH_CLIENT_APP
        // Only return this if swishUrl hasn't been used yet
        val launchClientApp = operations.flatMap { it.tasks ?: listOf() }
            .firstOrNull { task -> task.rel == Rel.LAUNCH_CLIENT_APP }
        if (launchClientApp != null && !alreadyUsedSwishUrls.contains(launchClientApp.href) && launchClientApp.href != null) {
            alreadyUsedSwishUrls.add(launchClientApp.href)
            instructions.add(0, StepInstruction.LaunchClientAppStep(launchClientApp.href))
            return OperationStep(
                instructions = instructions
            )
        }

        // 4. Search for Rel.REDIRECT_PAYER
        val redirectPayer = operations.firstOrNull { it.rel == Rel.REDIRECT_PAYER }
        if (redirectPayer?.href != null) {
            instructions.add(0, StepInstruction.PaymentSessionCompleted(redirectPayer.href))
            return OperationStep(
                instructions = instructions
            )
        }

        // 5. Search for Rel.EXPAND_METHOD
        // If we find something here extract every instrument with Rel.EXPAND_METHOD
        // and show for the merchant app
        val expandMethod = operations.firstOrNull { it.rel == Rel.EXPAND_METHOD }
        if (expandMethod != null) {
            val availableInstruments = arrayListOf<MethodBaseModel>()

            session.paymentSession.methods?.forEach { method ->
                if (method?.operations?.firstOrNull { op -> op.rel == Rel.EXPAND_METHOD } != null) {
                    availableInstruments.add(method)
                }
            }

            instructions.add(
                0, StepInstruction
                    .AvailableInstrumentStep(availableInstruments = availableInstruments.mapNotNull { it.toAvailableInstrument() })
            )

            return OperationStep(
                requestMethod = expandMethod.method,
                url = URL(expandMethod.href),
                instructions = instructions
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
                data = getPayment.rel?.getRequestDataIfAny(instrument),
                delayRequestDuration = 2000,
                instructions = instructions
            )
        }

        instructions.add(0, StepInstruction.StepNotFound())
        return OperationStep(
            instructions = instructions
        )
    }

    /**
     * This method is used to get an operation to start the payment with chosen instrument.
     *
     * Payment session can be in different states when calling this method.
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
                instructions = listOf(StepInstruction.SessionNotFound())
            )
        }

        val op = session.paymentSession.methods
            ?.firstOrNull { it?.instrument == paymentAttemptInstrument.toInstrument() }?.operations
            ?.firstOrNull {
                it.rel == Rel.EXPAND_METHOD
                        || it.rel == Rel.START_PAYMENT_ATTEMPT
                        || it.rel == Rel.GET_PAYMENT
            }

        return if (op != null) {
            OperationStep(
                requestMethod = op.method,
                url = URL(op.href),
                data = op.rel?.getRequestDataIfAny(paymentAttemptInstrument)
            )
        } else {
            OperationStep(
                instructions = listOf(StepInstruction.StepNotFound())
            )
        }
    }

    fun getOperationStepForAbortPayment(session: Session?): OperationStep {
        if (session == null) {
            return OperationStep(
                instructions = listOf(StepInstruction.SessionNotFound())
            )
        }

        val abortPayment = session.operations.firstOrNull {
            it.rel == Rel.ABORT_PAYMENT
        }

        return if (abortPayment != null) {
            OperationStep(
                requestMethod = abortPayment.method,
                url = URL(abortPayment.href),
            )
        } else {
            OperationStep(
                instructions = listOf(StepInstruction.StepNotFound())
            )
        }
    }

    fun clearUsedUrls() {
        alreadyUsedSwishUrls.clear()
        alreadyUsedProblemUrls.clear()
    }
}


internal sealed class StepInstruction(
    val waitForAction: Boolean = false,
    val errorMessage: String? = null,
) {
    class AvailableInstrumentStep(val availableInstruments: List<AvailableInstrument>) :
        StepInstruction(true)

    class LaunchClientAppStep(val href: String) : StepInstruction(true)

    class PaymentSessionCompleted(val url: String) : StepInstruction(true)

    class StepNotFound(message: String = "Step not found") : StepInstruction(true, message)

    class SessionNotFound(message: String = "Session not found") : StepInstruction(true, message)

    class ProblemOccurred(val problem: Problem, informMerchantApp: Boolean) :
        StepInstruction(informMerchantApp)

}

/**
 * This class holds what the next step would be in the payment session
 * [instructions] can hold zero, one or at most two [StepInstruction]
 * If two [StepInstruction] is present in the list the second one
 * will always be a [StepInstruction.ProblemOccurred] with waitForAction set to false
 */
internal data class OperationStep(
    val requestMethod: RequestMethod? = RequestMethod.GET,
    val url: URL? = null,
    val data: String? = null,
    val instructions: List<StepInstruction> = listOf(),
    val delayRequestDuration: Long = 0
)