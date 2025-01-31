package com.swedbankpay.mobilesdk.paymentsession

import android.content.Context
import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.FailPaymentAttempt
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.util.RequestUtil.getRequestDataIfAny
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.GooglePayMethodModel
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
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.SwedbankPayPaymentSessionSDKControllerMode
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.instrumentModeRequired
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.mapper.toAvailableInstrument
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.mapper.toSemiColonSeparatedString
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.toInstrument
import com.swedbankpay.mobilesdk.paymentsession.googlepay.GooglePayService
import com.swedbankpay.mobilesdk.paymentsession.googlepay.model.GooglePayResult
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
     * @param sdkControllerMode Chosen [SwedbankPayPaymentSessionSDKControllerMode] for the payment.
     */
    suspend fun getNextStep(
        paymentOutputModel: PaymentOutputModel?,
        paymentAttemptInstrument: PaymentAttemptInstrument? = null,
        sdkControllerMode: SwedbankPayPaymentSessionSDKControllerMode? = null,
        context: Context? = null
    ): OperationStep {
        if (paymentOutputModel == null) {
            return OperationStep(
                instructions = listOf(StepInstruction.SessionNotFound)
            )
        }

        val instructions = mutableListOf<StepInstruction>()

        // Extract every operation we have on the session object
        var operations: List<OperationOutputModel> =
            paymentOutputModel.operations.filterNotNull() +
                    paymentOutputModel.paymentSession.allMethodOperations

        // If we find a problem that we haven't used return it. Otherwise just acknowledge it and
        // add it to the instructions without informing the merchant app
        if (paymentOutputModel.problem != null) {
            val problemOperation = paymentOutputModel.problem.operation
            if (problemOperation?.rel == OperationRel.ACKNOWLEDGE_FAILED_ATTEMPT) {
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

        // Search for OperationRel.PREPARE_PAYMENT
        val preparePayment =
            operations.firstOrNull { it.rel == OperationRel.PREPARE_PAYMENT }
        if (preparePayment != null) {
            // Initial state of payment session, run preparePayment operation
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

        // CUSTOMIZE_PAYMENT
        val customizePayment =
            paymentOutputModel.operations.filterNotNull()
                .firstOrNull { it.rel == OperationRel.CUSTOMIZE_PAYMENT }

        paymentAttemptInstrument?.let { attemptInstrument ->
            if (customizePayment != null && ((paymentOutputModel.paymentSession.instrumentModePaymentMethod != null
                        && paymentOutputModel.paymentSession.instrumentModePaymentMethod != paymentAttemptInstrument.paymentMethod) || !paymentOutputModel.paymentSession.allPaymentMethods.contains(
                    attemptInstrument.paymentMethod
                ))
            ) {
                // Resetting Instrument Mode session to Menu Mode (instrumentModePaymentMethod set to null),
                // if new payment attempt is made with an instrument other than the current
                // Instrument Mode instrument or with an instrument not in the list of methods (restricted menu)
                return OperationStep(
                    requestMethod = customizePayment.method,
                    url = URL(customizePayment.href),
                    operationRel = customizePayment.rel,
                    data = customizePayment.rel?.getRequestDataIfAny(),
                    instructions = instructions
                )
            }

            if (customizePayment != null && attemptInstrument.instrumentModeRequired() && (paymentOutputModel.paymentSession.instrumentModePaymentMethod == null || paymentOutputModel.paymentSession.instrumentModePaymentMethod != attemptInstrument.paymentMethod)
            ) {
                // Switching to Instrument Mode from Menu Mode session (instrumentModePaymentMethod set to null) or from Instrument Mode with other instrument, if new payment attempt is made with an Instrument Mode required instrument (newCreditCard)
                return OperationStep(
                    requestMethod = customizePayment.method,
                    url = URL(customizePayment.href),
                    operationRel = customizePayment.rel,
                    data = customizePayment.rel?.getRequestDataIfAny(
                        paymentAttemptInstrument = attemptInstrument
                    ),
                    instructions = instructions
                )
            }

            if (paymentAttemptInstrument.instrumentModeRequired() && paymentOutputModel.paymentSession.instrumentModePaymentMethod == paymentAttemptInstrument.paymentMethod
            ) {
                // Session is in Instrument Mode, and the set instrument is matching payment attempt, time to create a web based view and send to the merchant app
                instructions.add(0, StepInstruction.CreatePaymentFragmentStep)
                return OperationStep(
                    instructions = instructions
                )
            }
        }


        sdkControllerMode?.let { mode ->
            if (customizePayment != null && mode is SwedbankPayPaymentSessionSDKControllerMode.InstrumentMode && (paymentOutputModel.paymentSession.instrumentModePaymentMethod == null
                        || paymentOutputModel.paymentSession.instrumentModePaymentMethod != mode.instrument.paymentMethod)
            ) {
                // Switching to Instrument Mode from Menu Mode session (instrumentModePaymentMethod set to nul) or from Instrument Mode with other instrument, if a SDK view controller is requested in instrument mode
                return OperationStep(
                    requestMethod = customizePayment.method,
                    url = URL(customizePayment.href),
                    operationRel = customizePayment.rel,
                    data = customizePayment.rel?.getRequestDataIfAny(
                        availableInstrument = mode.instrument
                    ),
                    instructions = instructions
                )
            }

            if (mode is SwedbankPayPaymentSessionSDKControllerMode.InstrumentMode && paymentOutputModel.paymentSession.instrumentModePaymentMethod == mode.instrument.paymentMethod
            ) {
                // Session is in Instrument Mode, and the set SDK view controller mode is matching the instrument, time to create a web based view and send to the merchant app
                instructions.add(0, StepInstruction.CreatePaymentFragmentStep)
                return OperationStep(
                    instructions = instructions
                )
            }

            if (customizePayment != null && mode is SwedbankPayPaymentSessionSDKControllerMode.Menu && (paymentOutputModel.paymentSession.instrumentModePaymentMethod != null
                        || paymentOutputModel.paymentSession.restrictedToInstruments?.sorted()
                        != mode.restrictedToInstruments?.map { it.paymentMethod }
                    ?.sorted())
            ) {
                // Switching to Menu Mode with potential list of restricted instruments from Instrument Mode or when  list of restricted instruments doesn't match (different list of instruments)
                return OperationStep(
                    requestMethod = customizePayment.method,
                    url = URL(customizePayment.href),
                    operationRel = customizePayment.rel,
                    data = customizePayment.rel?.getRequestDataIfAny(
                        restrictToPaymentMethods = mode.restrictedToInstruments?.map { it.paymentMethod }
                    ),
                    instructions = instructions
                )
            }

            if (mode is SwedbankPayPaymentSessionSDKControllerMode.Menu && paymentOutputModel.paymentSession.instrumentModePaymentMethod == null && paymentOutputModel.paymentSession.restrictedToInstruments?.sorted() == mode.restrictedToInstruments?.map { it.paymentMethod }
                    ?.sorted()
            ) {
                // Session is in Menu Mode, and the list of restricted instruments match the set SDK view controller mode, time to create a web based view and send to the merchant app
                instructions.add(0, StepInstruction.CreatePaymentFragmentStep)
                return OperationStep(
                    instructions = instructions
                )
            }
        }

        // Search for OperationRel.START_PAYMENT_ATTEMPT
        val startPaymentAttempt =
            paymentOutputModel.paymentSession.methods
                ?.firstOrNull { it?.paymentMethod == paymentAttemptInstrument?.toInstrument() }
                ?.operations
                ?.firstOrNull { it?.rel == OperationRel.START_PAYMENT_ATTEMPT }

        if (startPaymentAttempt != null) {
            // We have a startPaymentAttempt and it's matching the set instrument, time to make a payment attempt

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

        // Search for IntegrationTaskRel.LAUNCH_CLIENT_APP
        val launchClientApp = operations.flatMap { it.tasks ?: listOf() }
            .firstOrNull { task -> task?.rel == IntegrationTaskRel.LAUNCH_CLIENT_APP }

        if (launchClientApp != null && !alreadyUsedSwishUrls.contains(launchClientApp.href) && launchClientApp.href != null) {
            // We have an active launchClientApp task, and the contained URL isn't in the list of already launched Client App URLs, launch the external app on the device

            alreadyUsedSwishUrls.add(launchClientApp.href)
            instructions.add(0, StepInstruction.LaunchClientAppStep(launchClientApp.href))
            return OperationStep(
                instructions = instructions,
                integrationRel = IntegrationTaskRel.LAUNCH_CLIENT_APP
            )
        }

        // Search for IntegrationTaskRel.SCA_METHOD_REQUEST
        val scaMethodRequest = operations.flatMap { it.tasks ?: listOf() }
            .firstOrNull { task -> task?.rel == IntegrationTaskRel.SCA_METHOD_REQUEST }

        if (scaMethodRequest != null
            && scaMethodRequest.expects?.any { it?.value in scaMethodRequestDataPerformed.keys } == false
        ) {
            // We have an active scaMethodRequest task and we haven't loaded the Method Request URL before (as identified by threeDSMethodData value as key), load the SCA Method Request in the "invisible web view"

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

        // Search for OperationRel.CREATE_AUTHENTICATION
        val createAuth =
            operations.firstOrNull { it.rel == OperationRel.CREATE_AUTHENTICATION }

        val createAuthExpectationModel = createAuth?.tasks
            ?.firstOrNull { it?.rel == IntegrationTaskRel.SCA_METHOD_REQUEST }
            ?.getExpectValuesFor(PaymentSessionAPIConstants.THREE_DS_METHOD_DATA)

        val allowedToExecuteCreateAuthWithSCA =
            createAuthExpectationModel?.value in scaMethodRequestDataPerformed.keys

        if (createAuth != null
            && !createAuth.expects.isNullOrEmpty()
            && createAuthExpectationModel != null
            && allowedToExecuteCreateAuthWithSCA
        ) {
            // We have loaded the Method Request URL in the "invisible web view" before (as identified by threeDSMethodData value as key), so we can use the result and run the createAuthentication operation

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
            // The Session API has already provided us with a pre-defined Method Completion Indicator, so we take that and run the createAuthentication operation

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
        } else if (createAuth != null) {
            // We didn't have a result from a loaded Method Request URL, and we didn't get a pre-defined Method Completion Indicator, so we will have to send in the Unknown (U) indicator

            val trampolineNotificationUrl =
                createAuth.expects?.getValueFor(PaymentSessionAPIConstants.TRAMPOLINE_NOTIFICATION_URL) as String?

            trampolineNotificationUrl?.let { notificationUrl ->
                return OperationStep(
                    requestMethod = createAuth.method,
                    url = URL(createAuth.href),
                    operationRel = createAuth.rel,
                    data = createAuth.rel?.getRequestDataIfAny(
                        culture = paymentOutputModel.paymentSession.culture,
                        completionIndicator = "U",
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

        // Search for IntegrationTaskRel.SCA_REDIRECT
        val scaRedirect = operations.flatMap { it.tasks ?: listOf() }
            .firstOrNull { task -> task?.rel == IntegrationTaskRel.SCA_REDIRECT }

        if (scaRedirect != null
            && scaRedirect.expects?.any { it?.value in scaRedirectDataPerformed.keys } == false
        ) {
            // We have an active scaRedirect task, and the 3D secure page hasn't been shown to the user yet (as identified by creq as key), tell the merchant app to show a 3D Secure Fragment

            instructions.add(0, StepInstruction.ScaRedirectStep(scaRedirect))
            return OperationStep(
                instructions = instructions
            )
        }

        // Search for OperationRel.COMPLETE_AUTHENTICATION
        val completeAuth =
            operations.firstOrNull { it.rel == OperationRel.COMPLETE_AUTHENTICATION }

        val completeAuthExpectationModel = completeAuth?.tasks
            ?.firstOrNull { it?.rel == IntegrationTaskRel.SCA_REDIRECT }
            ?.getExpectValuesFor(PaymentSessionAPIConstants.CREQ)

        val allowedToExecuteCompleteAuth =
            completeAuthExpectationModel?.value in scaRedirectDataPerformed.keys

        if (completeAuth != null
            && completeAuthExpectationModel != null
            && allowedToExecuteCompleteAuth
        ) {
            // We have an active scaRedirect task, and the 3D secure page has been shown to the user (as identified by creq as key), run the completeAuthentication operation with the result

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

        // GooglePay. Search for IntegrationTaskRel.WALLET_SDK
        val walletSdk = operations.flatMap { it.tasks ?: listOf() }
            .firstOrNull { task -> task?.rel == IntegrationTaskRel.WALLET_SDK }

        if (walletSdk != null) {
            // We have an active walletSdk task, this means we should initiate an Google pay Payment Request locally on the device

            instructions.add(0, StepInstruction.GooglePayStep(walletSdk))
            return OperationStep(
                instructions = instructions
            )
        }

        // Search for OperationRel.REDIRECT_PAYER
        val redirectPayer =
            operations.firstOrNull { it.rel == OperationRel.REDIRECT_PAYER }
        if (redirectPayer?.href != null) {
            // We have a redirectPayer operation, this means the payment session has ended and we can look at the URL to determine the result

            instructions.add(0, StepInstruction.PaymentSessionCompleted(redirectPayer.href))
            return OperationStep(
                instructions = instructions
            )
        }

        if (paymentAttemptInstrument != null) {
            val paymentAttemptInstrumentOperation = paymentOutputModel.paymentSession.methods
                ?.firstOrNull { it?.paymentMethod == paymentAttemptInstrument.toInstrument() }
                ?.operations
                ?.firstOrNull {
                    it?.rel == OperationRel.EXPAND_METHOD
                            || it?.rel == OperationRel.START_PAYMENT_ATTEMPT
                }

            if (paymentAttemptInstrumentOperation != null) {
                // We have a method matching the set instrument, and it has one of the three supported method operations (expandMethod, startPaymentAttempt or getPayment)

                return OperationStep(
                    requestMethod = paymentAttemptInstrumentOperation.method,
                    url = URL(paymentAttemptInstrumentOperation.href),
                    operationRel = paymentAttemptInstrumentOperation.rel,
                    data = paymentAttemptInstrumentOperation.rel?.getRequestDataIfAny(
                        paymentAttemptInstrument,
                        paymentOutputModel.paymentSession.culture
                    )
                )
            }
        }

        if (!hasShownAvailableInstruments) {
            // No process has been initiated above, and we haven't sent the available instruments to the merchant app for this session yet, so send the list and wait for action

            val availableMethods = arrayListOf<MethodBaseModel>()

            if (!paymentOutputModel.paymentSession.methods.isNullOrEmpty()) {
                paymentOutputModel.paymentSession.methods.forEach { method ->
                    method?.let {
                        availableMethods.add(it)
                    }
                }

                // Get google pay readiness
                val googlePay =
                    availableMethods.firstOrNull { it is GooglePayMethodModel } as GooglePayMethodModel?
                var isReadyToPayWithGooglePay = false
                var isReadyToPayWithExistingPaymentMethodWithGooglePay = false

                if (googlePay != null && context != null) {
                    isReadyToPayWithGooglePay = GooglePayService.fetchCanUseGooglePay(
                        context = context,
                        existingPaymentMethodRequired = false,
                        allowedCardAuthMethods = googlePay.allowedCardAuthMethods,
                        allowedCardNetworks = googlePay.cardBrands
                    )
                    isReadyToPayWithExistingPaymentMethodWithGooglePay =
                        GooglePayService.fetchCanUseGooglePay(
                            context = context,
                            existingPaymentMethodRequired = true,
                            allowedCardAuthMethods = googlePay.allowedCardAuthMethods,
                            allowedCardNetworks = googlePay.cardBrands
                        )
                }

                val availableInstruments: MutableList<AvailableInstrument> =
                    availableMethods.map {
                        it.toAvailableInstrument(
                            isReadyToPayWithGooglePay,
                            isReadyToPayWithExistingPaymentMethodWithGooglePay
                        )
                    }.toMutableList()

                if (availableInstruments.any { it is AvailableInstrument.CreditCard }) {
                    availableInstruments.add(AvailableInstrument.NewCreditCard("NewCreditCard"))
                }

                instructions.add(
                    0, StepInstruction
                        .AvailableInstrumentStep(
                            availableInstruments = availableInstruments,
                            availableInstrumentsForLogging = availableMethods.toSemiColonSeparatedString()
                        )
                )

                hasShownAvailableInstruments = true

                return OperationStep(
                    instructions = instructions
                )
            }
        }

        // Search for OperationRel.GET_PAYMENT
        val getPayment = operations.firstOrNull { it.rel == OperationRel.GET_PAYMENT }
        if (getPayment != null) {
            // We're told to simply fetch the session again, wait until polling and fetch the session, running the session operation handling once again

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

    fun getOperationStepForAbortPayment(paymentOutputModel: PaymentOutputModel): OperationStep? {
        val abortPayment = paymentOutputModel.operations.firstOrNull {
            it?.rel == OperationRel.ABORT_PAYMENT
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

    fun getOperationStepForAttemptPayload(
        paymentOutputModel: PaymentOutputModel,
        googlePayResult: GooglePayResult
    ): OperationStep? {
        val attemptPayload =
            paymentOutputModel.paymentSession.allMethodOperations.firstOrNull {
                it.rel == OperationRel.ATTEMPT_PAYLOAD
            }

        return if (attemptPayload != null) {
            OperationStep(
                requestMethod = attemptPayload.method,
                url = URL(attemptPayload.href),
                operationRel = attemptPayload.rel,
                data = attemptPayload.rel?.getRequestDataIfAny(googlePayResult = googlePayResult)
            )
        } else {
            null
        }
    }

    fun getOperationStepForFailPaymentAttempt(
        paymentOutputModel: PaymentOutputModel,
        failPaymentAttempt: FailPaymentAttempt
    ): OperationStep? {
        val failPaymentAttemptOperation =
            paymentOutputModel.paymentSession.allMethodOperations.firstOrNull {
                it.rel == OperationRel.FAIL_PAYMENT_ATTEMPT
            }

        return if (failPaymentAttemptOperation != null) {
            OperationStep(
                requestMethod = failPaymentAttemptOperation.method,
                url = URL(failPaymentAttemptOperation.href),
                operationRel = failPaymentAttemptOperation.rel,
                data = failPaymentAttemptOperation.rel?.getRequestDataIfAny(failPaymentAttempt = failPaymentAttempt)
            )
        } else {
            null
        }

    }

    fun getBeaconUrl(paymentOutputModel: PaymentOutputModel?): String? =
        paymentOutputModel?.operations?.firstOrNull {
            it?.rel == OperationRel.EVENT_LOGGING
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
    class AvailableInstrumentStep(
        val availableInstruments: List<AvailableInstrument>,
        val availableInstrumentsForLogging: String
    ) :
        StepInstruction(true)

    class LaunchClientAppStep(val href: String) : StepInstruction(true)

    object CreatePaymentFragmentStep : StepInstruction(true)

    class ScaRedirectStep(val task: IntegrationTask) : StepInstruction(true)

    class GooglePayStep(val task: IntegrationTask) : StepInstruction(true)

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