package com.swedbankpay.mobilesdk.paymentsession

import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.util.RequestUtil.getRequestDataIfAny
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.IntegrationTask
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.IntegrationTaskRel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.MethodBaseModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationOutputModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PaymentOutputModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ProblemDetails
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.RequestMethod
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.getValueFor
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.AvailableInstrument
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.mapper.toAvailableInstrument
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.toInstrument
import com.swedbankpay.mobilesdk.paymentsession.sca.ScaMethodService
import com.swedbankpay.mobilesdk.paymentsession.util.extension.safeLet
import java.net.URL

internal object SessionOperationHandler {

    private val alreadyUsedProblemUrls: MutableList<String> = mutableListOf()
    private val alreadyUsedSwishUrls: MutableList<String> = mutableListOf()
    private val scaMethodRequestDataPerformed: MutableMap<String?, String> = mutableMapOf()
    private val scaRedirectDataPerformed: MutableMap<String, String> = mutableMapOf()
    private var hasShownAvailableInstruments: Boolean = false

    /**
     * This method will figure out what the next step in the payment session will be
     *
     * @param paymentOutputModel Current payment model
     * @param paymentAttemptInstrument Chosen [PaymentAttemptInstrument] for the payment.
     */
    suspend fun getNextStep(
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
            paymentOutputModel.operations + paymentOutputModel.paymentSession.allMethodOperations

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

        //region 1. Search for OperationRel.PREPARE_PAYMENT
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
        //endregion

        //region 2. Search for OperationRel.START_PAYMENT_ATTEMPT
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
        //endregion

        //region 3. Search for IntegrationTaskRel.LAUNCH_CLIENT_APP
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
        //endregion

        //region 4. Search for IntegrationTaskRel.SCA_METHOD_REQUEST
        val scaMethodRequest = operations.flatMap { it.tasks ?: listOf() }
            .firstOrNull { task -> task.rel == IntegrationTaskRel.SCA_METHOD_REQUEST }

        if (scaMethodRequest != null
            && scaMethodRequest.expects?.any { it.value in scaMethodRequestDataPerformed.keys } == false
        ) {

            val completionIndicator = ScaMethodService.loadScaMethodRequest(
                task = scaMethodRequest,
                localStartContext = paymentAttemptInstrument?.context
            )

            scaMethodRequestDataPerformed[scaMethodRequest.getExpectValuesFor(
                PaymentSessionAPIConstants.THREE_DS_METHOD_DATA
            )?.value as String?] =
                completionIndicator

            instructions.add(0, StepInstruction.OverrideApiCall(paymentOutputModel))

            return OperationStep(
                integrationRel = IntegrationTaskRel.SCA_METHOD_REQUEST,
                instructions = instructions
            )

        }
        //endregion

        //region 5. Search for OperationRel.CREATE_AUTHENTICATION
        val createAuth =
            operations.firstOrNull { it.rel == OperationRel.CREATE_AUTHENTICATION }

        val createAuthExpectationModel = createAuth?.tasks
            ?.firstOrNull { it.rel == IntegrationTaskRel.SCA_METHOD_REQUEST }
            ?.getExpectValuesFor(PaymentSessionAPIConstants.THREE_DS_METHOD_DATA)

        val allowedToExecuteCreateAuthWithSCA =
            createAuthExpectationModel?.value in scaMethodRequestDataPerformed.keys

        if (createAuth != null
            && !createAuth.expects.isNullOrEmpty()
            && createAuthExpectationModel != null
            && allowedToExecuteCreateAuthWithSCA
        ) {
            (createAuth.expects.getValueFor(PaymentSessionAPIConstants.TRAMPOLINE_NOTIFICATION_URL)
                    as String?)?.let { notificationUrl ->
                return OperationStep(
                    requestMethod = createAuth.method,
                    url = URL(createAuth.href),
                    operationRel = createAuth.rel,
                    data = createAuth.rel?.getRequestDataIfAny(
                        culture = paymentOutputModel.paymentSession.culture,
                        completionIndicator = scaMethodRequestDataPerformed[createAuthExpectationModel.value]
                            ?: "U",
                        notificationUrl = notificationUrl
                    ),
                    instructions = instructions
                )
            } ?: kotlin.run {
                instructions.add(0, StepInstruction.InternalError)
                return OperationStep(
                    instructions = instructions
                )
            }
        } else if (createAuth != null
            && !createAuth.expects.isNullOrEmpty()
        ) {
            val trampolineNotificationUrl =
                createAuth.expects.getValueFor(PaymentSessionAPIConstants.TRAMPOLINE_NOTIFICATION_URL) as String?
            val methodCompletionIndicator =
                createAuth.expects.getValueFor(PaymentSessionAPIConstants.METHOD_COMPLETION_INDICATOR) as String?

            safeLet(
                trampolineNotificationUrl,
                methodCompletionIndicator
            ) { notificationUrl, completionIndicator ->
                return OperationStep(
                    requestMethod = createAuth.method,
                    url = URL(createAuth.href),
                    operationRel = createAuth.rel,
                    data = createAuth.rel?.getRequestDataIfAny(
                        culture = paymentOutputModel.paymentSession.culture,
                        completionIndicator = completionIndicator,
                        notificationUrl = notificationUrl
                    ),
                    instructions = instructions
                )
            } ?: kotlin.run {
                instructions.add(0, StepInstruction.InternalError)
                return OperationStep(
                    instructions = instructions
                )
            }
        }
        //endregion

        //region 6. Search for IntegrationTaskRel.SCA_REDIRECT
        val scaRedirect = operations.flatMap { it.tasks ?: listOf() }
            .firstOrNull { task -> task.rel == IntegrationTaskRel.SCA_REDIRECT }

        if (scaRedirect != null
            && scaRedirect.expects?.any { it.value in scaRedirectDataPerformed.keys } == false
        ) {
            instructions.add(0, StepInstruction.ScaRedirectStep(scaRedirect))
            return OperationStep(
                instructions = instructions
            )
        }
        //endregion

        //region 7 Search for OperationRel.COMPLETE_AUTHENTICATION
        val completeAuth =
            operations.firstOrNull { it.rel == OperationRel.COMPLETE_AUTHENTICATION }

        val completeAuthExpectationModel = completeAuth?.tasks
            ?.firstOrNull { it.rel == IntegrationTaskRel.SCA_REDIRECT }
            ?.getExpectValuesFor(PaymentSessionAPIConstants.CREQ)

        val allowedToExecuteCompleteAuth =
            completeAuthExpectationModel?.value in scaRedirectDataPerformed.keys

        if (completeAuth != null
            && completeAuthExpectationModel != null
            && allowedToExecuteCompleteAuth
        ) {
            return OperationStep(
                requestMethod = completeAuth.method,
                url = URL(completeAuth.href),
                operationRel = completeAuth.rel,
                data = completeAuth.rel?.getRequestDataIfAny(
                    culture = paymentOutputModel.paymentSession.culture,
                    cRes = scaRedirectDataPerformed[completeAuthExpectationModel.value] ?: ""
                ),
                instructions = instructions
            )
        }
        //endregion

        //region 8. Search for OperationRel.REDIRECT_PAYER
        val redirectPayer =
            operations.firstOrNull { it.rel == OperationRel.REDIRECT_PAYER }
        if (redirectPayer?.href != null) {
            instructions.add(0, StepInstruction.PaymentSessionCompleted(redirectPayer.href))
            return OperationStep(
                instructions = instructions
            )
        }
        //endregion

        //region 9. Search for OperationRel.EXPAND_METHOD or OperationRel.START_PAYMENT_ATTEMPT for instruments
        // that doesn't need OperationRel.EXPAND_METHOD to work.
        if (!hasShownAvailableInstruments) {
            val expandMethod =
                operations.firstOrNull {
                    it.rel == OperationRel.EXPAND_METHOD
                            || it.rel == OperationRel.START_PAYMENT_ATTEMPT
                }
            if (expandMethod != null) {
                val availableMethods = arrayListOf<MethodBaseModel>()

                paymentOutputModel.paymentSession.methods?.forEach { method ->
                    if (method?.operations?.firstOrNull { op ->
                            op.rel == OperationRel.EXPAND_METHOD ||
                                    op.rel == OperationRel.START_PAYMENT_ATTEMPT
                        } != null) {
                        availableMethods.add(method)
                    }
                }

                instructions.add(
                    0, StepInstruction
                        .AvailableInstrumentStep(availableInstruments = availableMethods.map { it.toAvailableInstrument() })
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
        //endregion

        //region 10. Search for OperationRel.GET_PAYMENT
        // If we come here and find OperationRel.GET_PAYMENT we want to start polling for a result we can
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
        //endregion

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

    fun scaRedirectComplete(cReq: String, cRes: String) {
        scaRedirectDataPerformed[cReq] = cRes
    }

    fun clearState() {
        alreadyUsedSwishUrls.clear()
        alreadyUsedProblemUrls.clear()
        hasShownAvailableInstruments = false
        scaMethodRequestDataPerformed.clear()
        scaRedirectDataPerformed.clear()
    }
}


internal sealed class StepInstruction(
    val waitForAction: Boolean = false,
) {
    class AvailableInstrumentStep(val availableInstruments: List<AvailableInstrument>) :
        StepInstruction(true)

    class LaunchClientAppStep(val href: String) : StepInstruction(true)

    class ScaRedirectStep(val task: IntegrationTask) : StepInstruction(true)

    class PaymentSessionCompleted(val url: String) : StepInstruction(true)

    object StepNotFound : StepInstruction(true)

    object SessionNotFound : StepInstruction(true)

    object InternalError : StepInstruction(true)

    class ProblemOccurred(
        val problemDetails: ProblemDetails,
        waitForAction: Boolean
    ) :
        StepInstruction(waitForAction)

    class OverrideApiCall(
        val paymentOutputModel: PaymentOutputModel
    ) : StepInstruction(false)

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
    val delayRequestDuration: Long = 0,
)