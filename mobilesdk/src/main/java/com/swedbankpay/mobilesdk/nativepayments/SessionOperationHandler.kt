package com.swedbankpay.mobilesdk.nativepayments

import com.swedbankpay.mobilesdk.nativepayments.api.model.request.util.RequestUtil.getRequestDataIfAny
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.IntegrationTaskRel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.MethodBaseModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.OperationOutputModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.OperationRel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.PaymentOutputModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.ProblemDetails
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.RequestMethod
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.AvailableInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.mapper.toAvailableInstrument
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.toInstrument
import java.net.URL

internal object SessionOperationHandler {

    private val alreadyUsedProblemUrls: MutableList<String> = mutableListOf()
    private val alreadyUsedSwishUrls: MutableList<String> = mutableListOf()
    private var hasShownAvailableInstruments: Boolean = false

    /**
     * This method will figure out what the next step in the payment session will be
     *
     * @param paymentOutputModel Current payment model
     * @param paymentAttemptInstrument Chosen [PaymentAttemptInstrument] for the payment.
     */
    fun getNextStep(
        paymentOutputModel: PaymentOutputModel?,
        paymentAttemptInstrument: PaymentAttemptInstrument? = null,
    ): OperationStep {
        if (paymentOutputModel == null) {
            return OperationStep(
                instructions = listOf(StepInstruction.SessionNotFound)
            )
        }

        val instructions = mutableListOf<StepInstruction>()

        // Extract every operation we have on the session object
        var operations: List<OperationOutputModel> =
            paymentOutputModel.operations + (paymentOutputModel.paymentSession.methods?.flatMap {
                it?.operations ?: listOf()
            } ?: listOf())

        // If we find a problem that we haven't used return it. Otherwise just acknowledge it and
        // add it to the instructions without informing the merchant app
        if (paymentOutputModel.problem != null) {
            val problemOperation = paymentOutputModel.problem.operations
            if (problemOperation.rel == OperationRel.ACKNOWLEDGE_FAILED_ATTEMPT) {
                if (problemOperation.href != null) {
                    if (alreadyUsedProblemUrls.contains(problemOperation.href)) {
                        instructions.add(
                            StepInstruction.ProblemOccurred(
                                paymentOutputModel.problem,
                                false
                            )
                        )
                    } else {
                        alreadyUsedProblemUrls.add(problemOperation.href)
                        return OperationStep(
                            requestMethod = problemOperation.method,
                            url = URL(problemOperation.href),
                            operationRel = problemOperation.rel,
                            instructions = listOf(
                                StepInstruction.ProblemOccurred(
                                    paymentOutputModel.problem,
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
        val preparePayment =
            operations.firstOrNull { it.rel == OperationRel.PREPARE_PAYMENT }
        if (preparePayment != null) {
            return OperationStep(
                requestMethod = preparePayment.method,
                url = URL(preparePayment.href),
                operationRel = preparePayment.rel,
                data = preparePayment.rel?.getRequestDataIfAny(
                    culture = paymentOutputModel.paymentSession.culture
                ),
                instructions = instructions
            )
        }

        // 2. Search for Rel.START_PAYMENT_ATTEMPT
        val startPaymentAttempt =
            paymentOutputModel.paymentSession.methods
                ?.firstOrNull { it?.instrument == paymentAttemptInstrument?.toInstrument() }
                ?.operations
                ?.firstOrNull { it.rel == OperationRel.START_PAYMENT_ATTEMPT }

        if (startPaymentAttempt != null) {
            return OperationStep(
                requestMethod = startPaymentAttempt.method,
                url = URL(startPaymentAttempt.href),
                operationRel = startPaymentAttempt.rel,
                data = startPaymentAttempt.rel?.getRequestDataIfAny(
                    paymentAttemptInstrument,
                    paymentOutputModel.paymentSession.culture
                ),
                instructions = instructions
            )
        }


        // 3. Search for Rel.LAUNCH_CLIENT_APP
        // Only return this if swishUrl hasn't been used yet
        val launchClientApp = operations.flatMap { it.tasks ?: listOf() }
            .firstOrNull { task -> task.rel == IntegrationTaskRel.LAUNCH_CLIENT_APP }
        if (launchClientApp != null && !alreadyUsedSwishUrls.contains(launchClientApp.href) && launchClientApp.href != null) {
            alreadyUsedSwishUrls.add(launchClientApp.href)
            instructions.add(0, StepInstruction.LaunchClientAppStep(launchClientApp.href))
            return OperationStep(
                instructions = instructions,
                integrationRel = IntegrationTaskRel.LAUNCH_CLIENT_APP
            )
        }

        // 4. Search for Rel.REDIRECT_PAYER
        val redirectPayer =
            operations.firstOrNull { it.rel == OperationRel.REDIRECT_PAYER }
        if (redirectPayer?.href != null) {
            instructions.add(0, StepInstruction.PaymentSessionCompleted(redirectPayer.href))
            return OperationStep(
                instructions = instructions
            )
        }

        // 5. Search for Rel.EXPAND_METHOD or Rel.START_PAYMENT_ATTEMPT for instruments
        // that doesn't need Rel.EXPAND_METHOD to work.
        if (!hasShownAvailableInstruments) {
            val expandMethod =
                operations.firstOrNull {
                    it.rel == OperationRel.EXPAND_METHOD
                            || it.rel == OperationRel.START_PAYMENT_ATTEMPT
                }
            if (expandMethod != null) {
                val availableInstruments = arrayListOf<MethodBaseModel>()

                paymentOutputModel.paymentSession.methods?.forEach { method ->
                    if (method?.operations?.firstOrNull { op ->
                            op.rel == OperationRel.EXPAND_METHOD ||
                                    op.rel == OperationRel.START_PAYMENT_ATTEMPT
                        } != null) {
                        availableInstruments.add(method)
                    }
                }

                instructions.add(
                    0, StepInstruction
                        .AvailableInstrumentStep(availableInstruments = availableInstruments.mapNotNull { it.toAvailableInstrument() })
                )

                hasShownAvailableInstruments = true

                return OperationStep(
                    requestMethod = expandMethod.method,
                    url = URL(expandMethod.href),
                    operationRel = expandMethod.rel,
                    instructions = instructions
                )
            }
        }


        // 6. Search for Rel.GET_PAYMENT
        // If we come here and find Rel.GET_PAYMENT we want to start polling for a result we can
        // do something with
        val getPayment = operations.firstOrNull { it.rel == OperationRel.GET_PAYMENT }
        if (getPayment != null) {
            return OperationStep(
                requestMethod = getPayment.method,
                url = URL(getPayment.href),
                operationRel = getPayment.rel,
                data = getPayment.rel?.getRequestDataIfAny(
                    culture = paymentOutputModel.paymentSession.culture
                ),
                delayRequestDuration = 2000,
                instructions = instructions
            )
        }

        instructions.add(0, StepInstruction.StepNotFound)
        return OperationStep(
            instructions = instructions
        )
    }

    /**
     * This method is used to get an operation to start the payment with chosen instrument.
     *
     * Payment session can be in different states when calling this method.
     * First time calling this method will result in an operation containing [OperationRel.EXPAND_METHOD].
     * If problem occurs and you call this method again with the same instrument you will have
     * an operation containing [OperationRel.START_PAYMENT_ATTEMPT] or if a polling has occurred it will have [OperationRel.GET_PAYMENT].
     * If you call it again after the problem with a different instrument you will again have [OperationRel.EXPAND_METHOD]
     * on that operation.
     */
    fun getOperationStepForPaymentAttempt(
        paymentOutputModel: PaymentOutputModel,
        paymentAttemptInstrument: PaymentAttemptInstrument
    ): OperationStep? {
        val op = paymentOutputModel.paymentSession.methods
            ?.firstOrNull { it?.instrument == paymentAttemptInstrument.toInstrument() }
            ?.operations
            ?.firstOrNull {
                it.rel == OperationRel.EXPAND_METHOD
                        || it.rel == OperationRel.START_PAYMENT_ATTEMPT
                        || it.rel == OperationRel.GET_PAYMENT
            }

        return if (op != null) {
            OperationStep(
                requestMethod = op.method,
                url = URL(op.href),
                operationRel = op.rel,
                data = op.rel?.getRequestDataIfAny(
                    paymentAttemptInstrument,
                    paymentOutputModel.paymentSession.culture
                )
            )
        } else {
            return null
        }
    }

    fun getOperationStepForAbortPayment(paymentOutputModel: PaymentOutputModel): OperationStep? {
        val abortPayment = paymentOutputModel.operations.firstOrNull {
            it.rel == OperationRel.ABORT_PAYMENT
        }

        return if (abortPayment != null) {
            OperationStep(
                requestMethod = abortPayment.method,
                url = URL(abortPayment.href),
                operationRel = abortPayment.rel
            )
        } else {
            return null
        }
    }

    fun getBeaconUrl(paymentOutputModel: PaymentOutputModel?): String? =
        paymentOutputModel?.operations?.firstOrNull {
            it.rel == OperationRel.EVENT_LOGGING
        }?.href

    fun clearState() {
        alreadyUsedSwishUrls.clear()
        alreadyUsedProblemUrls.clear()
        hasShownAvailableInstruments = false
    }
}


internal sealed class StepInstruction(
    val waitForAction: Boolean = false,
) {
    class AvailableInstrumentStep(val availableInstruments: List<AvailableInstrument>) :
        StepInstruction(true)

    class LaunchClientAppStep(val href: String) : StepInstruction(true)

    class PaymentSessionCompleted(val url: String) : StepInstruction(true)

    object StepNotFound : StepInstruction(true)

    object SessionNotFound : StepInstruction(true)

    class ProblemOccurred(
        val problemDetails: ProblemDetails,
        waitForAction: Boolean
    ) :
        StepInstruction(waitForAction)

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
    val operationRel: OperationRel? = null,
    val integrationRel: IntegrationTaskRel? = null,
    val data: String? = null,
    val instructions: List<StepInstruction> = listOf(),
    val delayRequestDuration: Long = 0
)