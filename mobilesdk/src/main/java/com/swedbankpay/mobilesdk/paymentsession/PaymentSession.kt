package com.swedbankpay.mobilesdk.paymentsession


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.swedbankpay.mobilesdk.PaymentFragment
import com.swedbankpay.mobilesdk.ViewPaymentOrderInfo
import com.swedbankpay.mobilesdk.internal.CallbackActivity
import com.swedbankpay.mobilesdk.logging.BeaconService
import com.swedbankpay.mobilesdk.logging.model.EventAction
import com.swedbankpay.mobilesdk.logging.model.MethodModel
import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIClient
import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIConstants
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.util.TimeOutUtil
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.IntegrationTask
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PaymentOutputModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PaymentSessionResponse
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ProblemDetails
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.RequestMethod
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentSessionProblem
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.toInstrument
import com.swedbankpay.mobilesdk.paymentsession.sca.ScaRedirectFragment
import com.swedbankpay.mobilesdk.paymentsession.util.UriCallbackUtil.addCallbackUrl
import com.swedbankpay.mobilesdk.paymentsession.util.clientAppCallbackExtensionsModel
import com.swedbankpay.mobilesdk.paymentsession.util.configuration.AutomaticConfiguration
import com.swedbankpay.mobilesdk.paymentsession.util.extension.safeLet
import com.swedbankpay.mobilesdk.paymentsession.util.launchClientAppExtensionsModel
import com.swedbankpay.mobilesdk.paymentsession.util.toExtensionsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


/**
 * Class that handles payment sessions
 *
 * @param orderInfo information that provides `PaymentSession` with callback URLs.
 *                  if not provided the session will try to use automatic configuration
 */
class PaymentSession(private var orderInfo: ViewPaymentOrderInfo? = null) {
    companion object {
        /**
         * Contains the state of the payment process
         */
        private var _paymentSessionState: MutableLiveData<PaymentSessionState> =
            MutableLiveData(PaymentSessionState.Idle)
        val paymentSessionState: LiveData<PaymentSessionState> = _paymentSessionState
    }

    /**
     * When redirected from swish this callback will be triggered
     */
    private val callbackUrlObserver = Observer<Unit> {
        checkCallbacks()
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var currentPaymentOutputModel: PaymentOutputModel? = null

    private var paymentAttemptInstrument: PaymentAttemptInstrument? = null

    private var startRequestTimestamp: Long = 0

    private val client: PaymentSessionAPIClient by lazy {
        PaymentSessionAPIClient()
    }

    private fun startObservingCallbacks() {
        CallbackActivity.onNativeCallbackUrlInvoked.observeForever(callbackUrlObserver)
    }

    private fun stopObservingCallbacks() {
        CallbackActivity.onNativeCallbackUrlInvoked.removeObserver(callbackUrlObserver)
    }

    private fun clearState(isStartingSession: Boolean = false) {
        clearPaymentAttemptInstrument()
        currentPaymentOutputModel = null
        SessionOperationHandler.clearState()
        stopObservingCallbacks()
        if (isStartingSession) {
            BeaconService.clearQueue()
        } else {
            orderInfo = null
        }
    }

    private fun checkCallbacks() {
        val callbackUrl = orderInfo?.paymentUrl
        if (callbackUrl != null && CallbackActivity.consumeNativeCallbackUrl(callbackUrl)) {
            clearPaymentAttemptInstrument()
            BeaconService.logEvent(
                EventAction.ClientAppCallback(
                    extensions = clientAppCallbackExtensionsModel(
                        callbackUrl
                    )
                )
            )

            val getPayment =
                currentPaymentOutputModel
                    ?.paymentSession
                    ?.allMethodOperations
                    ?.firstOrNull { it.rel == OperationRel.GET_PAYMENT }

            if (getPayment != null) {
                executeNextStepUntilFurtherInstructions(
                    operationStep = OperationStep(
                        requestMethod = RequestMethod.GET,
                        url = URL(getPayment.href)
                    )
                )
            } else {
                onSdkProblemOccurred(PaymentSessionProblem.PaymentSessionEndReached)
            }
        }
    }

    /**
     * This method will call the api until we need to inform the
     * merchant app to take some kind of action
     */
    private fun executeNextStepUntilFurtherInstructions(
        operationStep: OperationStep
    ) {
        startRequestTimestamp = System.currentTimeMillis()
        // So we don't launch multiple jobs when calling this method again
        scope.coroutineContext.cancelChildren()
        scope.launch {
            var stepToExecute = operationStep

            while (stepToExecute.instructions.firstOrNull { it.waitForAction } == null) {
                // This is here for polling purposes
                if (stepToExecute.delayRequestDuration > 0) {
                    delay(stepToExecute.delayRequestDuration)
                    // When we poll we need to reset requestTimestamp so we don't end it to early
                    startRequestTimestamp = System.currentTimeMillis()
                }

                when (val paymentSessionResponse =
                    client.executeNextRequest(stepToExecute, paymentAttemptInstrument)) {
                    is PaymentSessionResponse.Retry -> {
                        if (System.currentTimeMillis() - startRequestTimestamp >
                            TimeOutUtil.getSessionTimeout(
                                operationStep.operationRel,
                                paymentAttemptInstrument?.toInstrument()
                            )
                        ) {
                            withContext(Dispatchers.Main) {
                                onSdkProblemOccurred((
                                        PaymentSessionProblem.PaymentSessionAPIRequestFailed(
                                            error = paymentSessionResponse.error,
                                            retry = {
                                                executeNextStepUntilFurtherInstructions(
                                                    stepToExecute
                                                )
                                            }
                                        )
                                        ))
                            }
                            break
                        } else {
                            delay(1000)
                        }
                    }

                    is PaymentSessionResponse.Error -> {
                        withContext(Dispatchers.Main) {
                            onSdkProblemOccurred((
                                    PaymentSessionProblem.PaymentSessionAPIRequestFailed(
                                        error = paymentSessionResponse.error,
                                        retry = {
                                            executeNextStepUntilFurtherInstructions(
                                                stepToExecute
                                            )
                                        }
                                    )
                                    ))
                        }
                        break
                    }

                    is PaymentSessionResponse.Success -> {
                        currentPaymentOutputModel = paymentSessionResponse.paymentOutputModel

                        val paymentSessionProblem = createOrderInfo()

                        if (paymentSessionProblem != null) {
                            withContext(Dispatchers.Main) {
                                onSdkProblemOccurred(paymentSessionProblem)
                            }
                            break
                        }

                        BeaconService.setBeaconUrl(
                            SessionOperationHandler.getBeaconUrl(
                                currentPaymentOutputModel
                            )
                        )
                        SessionOperationHandler.getNextStep(
                            paymentOutputModel = currentPaymentOutputModel,
                            paymentAttemptInstrument = paymentAttemptInstrument
                        ).let { step ->

                            if (operationStep.operationRel == OperationRel.REDIRECT_PAYER) {
                                clearPaymentAttemptInstrument()
                            }

                            stepToExecute = step
                            if (step.instructions.isNotEmpty()
                            ) {
                                val (instruction, problem) = step.instructions

                                if (instruction is StepInstruction.ProblemOccurred) {
                                    client.postFailedAttemptRequest(instruction.problemDetails)
                                }

                                if (problem != null && problem is StepInstruction.ProblemOccurred) {
                                    client.postFailedAttemptRequest(problem.problemDetails)
                                }

                                withContext(Dispatchers.Main) {
                                    when (instruction) {
                                        is StepInstruction.SessionNotFound -> {
                                            onSdkProblemOccurred(
                                                PaymentSessionProblem.InternalInconsistencyError
                                            )
                                        }

                                        is StepInstruction.InternalError -> {
                                            onSdkProblemOccurred(
                                                PaymentSessionProblem.InternalInconsistencyError
                                            )
                                        }

                                        is StepInstruction.StepNotFound -> {
                                            onSdkProblemOccurred(
                                                PaymentSessionProblem.PaymentSessionEndReached
                                            )
                                        }

                                        else -> {
                                            if (instruction?.waitForAction == true) {
                                                checkWhatTodo(instruction)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createOrderInfo(): PaymentSessionProblem? {
        if (orderInfo == null) {
            currentPaymentOutputModel?.let { paymentOutputModel ->
                val viewPayment = paymentOutputModel.operations.firstOrNull {
                    it.rel == OperationRel.VIEW_PAYMENT
                }

                val urls = paymentOutputModel.paymentSession.urls

                safeLet(
                    viewPayment?.href,
                    urls?.paymentUrl,
                    urls?.completeUrl,
                    urls?.cancelUrl
                ) { viewPaymentLink, paymentUrl, completeUrl, cancelUrl ->
                    orderInfo = ViewPaymentOrderInfo(
                        viewPaymentLink = viewPaymentLink,
                        webViewBaseUrl = urls?.hostUrls?.getOrNull(0),
                        completeUrl = completeUrl,
                        cancelUrl = cancelUrl,
                        paymentUrl = paymentUrl,
                        isV3 = true
                    )
                } ?: return PaymentSessionProblem.AutomaticConfigurationFailed
            } ?: return PaymentSessionProblem.PaymentSessionEndReached
        }

        return null
    }

    /**
     * Clearing of [PaymentAttemptInstrument] will be done in different places
     * during the payment process depending on [PaymentAttemptInstrument] used
     */
    private fun clearPaymentAttemptInstrument(
    ) {
        paymentAttemptInstrument = null
    }

    private fun checkWhatTodo(instruction: StepInstruction) {
        when (instruction) {
            is StepInstruction.AvailableInstrumentStep -> {
                _paymentSessionState.value =
                    PaymentSessionState.PaymentSessionFetched(instruction.availableInstruments)
                setStateToIdle()

                BeaconService.logEvent(
                    eventAction = EventAction.SDKCallbackInvoked(
                        method = MethodModel(
                            name = "availableInstrumentsFetched",
                            sdk = true,
                            succeeded = true
                        ),
                        extensions = instruction.availableInstruments.toExtensionsModel()
                    )
                )
            }

            is StepInstruction.ScaRedirectStep -> {
                show3DSecure(instruction.task)
            }

            is StepInstruction.LaunchClientAppStep -> {
                launchClientApp(instruction.href)
            }

            is StepInstruction.PaymentSessionCompleted -> {
                onPaymentComplete(instruction.url)
            }

            is StepInstruction.ProblemOccurred -> {
                onSessionProblemOccurred(instruction.problemDetails)
            }

            else -> {
                onSdkProblemOccurred(PaymentSessionProblem.PaymentSessionEndReached)
            }
        }
    }

    /**
     * Fetches a new payment session
     *
     * @param sessionURL URL needed to fetch the payment session
     *
     */
    fun fetchPaymentSession(
        sessionURL: String,
    ) {
        clearState(true)
        startObservingCallbacks()

        BeaconService.logEvent(
            eventAction = EventAction.SDKMethodInvoked(
                method = MethodModel(
                    name = "fetchPaymentSession",
                    sdk = true,
                    succeeded = true
                )
            )
        )

        executeNextStepUntilFurtherInstructions(
            operationStep = OperationStep(
                requestMethod = RequestMethod.GET,
                url = URL(sessionURL)
            )
        )
    }

    /**
     * Make a native payment attempt with a specific instrument
     *
     * There needs to be an active payment session before a payment attempt can be made
     *
     * @param instrument [PaymentAttemptInstrument]
     */
    fun makeNativePaymentAttempt(
        instrument: PaymentAttemptInstrument,
    ) {
        currentPaymentOutputModel?.let {
            paymentAttemptInstrument = instrument

            val paymentAttemptOperation = SessionOperationHandler.getOperationStepForPaymentAttempt(
                it,
                instrument
            )

            if (paymentAttemptOperation != null) {
                executeNextStepUntilFurtherInstructions(paymentAttemptOperation)
            }

            BeaconService.logEvent(
                eventAction = EventAction.SDKMethodInvoked(
                    method = MethodModel(
                        name = "makeNativePaymentAttempt",
                        sdk = true,
                        succeeded = paymentAttemptOperation != null
                    ),
                    extensions = instrument.toExtensionsModel()
                )
            )

        } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
    }

    /**
     * Creates a payment fragment
     */
    fun createPaymentFragment() {
        orderInfo?.let {
            val paymentFragment = PaymentFragment()
            PaymentFragment.defaultConfiguration = AutomaticConfiguration(it)

            paymentFragment.arguments = PaymentFragment.ArgumentsBuilder()
                .checkoutV3(true)
                .useBrowser(false)
                .build()

            BeaconService.logEvent(
                eventAction = EventAction.SDKMethodInvoked(
                    method = MethodModel(
                        name = "createPaymentFragment",
                        sdk = true,
                        succeeded = true
                    )
                )
            )

            _paymentSessionState.value = PaymentSessionState.PaymentFragmentCreated(paymentFragment)
            setStateToIdle()

        } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
    }

    /**
     * Abort an active payment session
     */
    fun abortPaymentSession() {
        currentPaymentOutputModel?.let {
            val abortPaymentOperation =
                SessionOperationHandler.getOperationStepForAbortPayment(it)

            if (abortPaymentOperation != null) {
                executeNextStepUntilFurtherInstructions(abortPaymentOperation)
            }

            BeaconService.logEvent(
                eventAction = EventAction.SDKMethodInvoked(
                    method = MethodModel(
                        name = "abortPaymentSession",
                        sdk = true,
                        succeeded = abortPaymentOperation != null
                    )
                )
            )
        } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
    }

    private fun show3DSecure(task: IntegrationTask) {

        val scaRedirectFragment = ScaRedirectFragment(
            task,
            errorHandler = ::onSdkProblemOccurred
        ) { cRes ->
            currentPaymentOutputModel?.let { session ->
                when {
                    cRes != null -> {
                        clearPaymentAttemptInstrument()

                        SessionOperationHandler.scaRedirectComplete(
                            task.getExpectValuesFor(PaymentSessionAPIConstants.CREQ)?.value as String,
                            cRes
                        )
                        executeNextStepUntilFurtherInstructions(
                            operationStep = OperationStep(
                                instructions = listOf(StepInstruction.OverrideApiCall(session))
                            )
                        )
                        _paymentSessionState.value = PaymentSessionState.Dismiss3dSecureFragment
                        setStateToIdle()

                        BeaconService.logEvent(
                            eventAction = EventAction.SDKCallbackInvoked(
                                method = MethodModel(
                                    name = "dismiss3DSecureFragment",
                                    sdk = true,
                                    succeeded = true
                                )
                            )
                        )
                    }
                }
            } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
        }

        _paymentSessionState.value = PaymentSessionState.Show3dSecureFragment(scaRedirectFragment)
        setStateToIdle()

        BeaconService.logEvent(
            eventAction = EventAction.SDKCallbackInvoked(
                method = MethodModel(
                    name = "show3DSecureFragment",
                    sdk = true,
                    succeeded = true
                )
            )
        )
    }

    private fun launchClientApp(href: String) {
        val uriWithCallback = href.addCallbackUrl(orderInfo)

        safeLet(uriWithCallback, paymentAttemptInstrument) { uri, paymentInstrument ->
            when (paymentInstrument) {
                is PaymentAttemptInstrument.Swish -> {
                    launchSwish(uri, paymentInstrument.localStartContext)
                    clearPaymentAttemptInstrument()
                }

                else -> {
                    onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
                }
            }
        } ?: kotlin.run {
            onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
        }
    }

    private fun launchSwish(uri: Uri, context: Context?) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context?.startActivity(intent)
            BeaconService.logEvent(
                eventAction = EventAction.LaunchClientApp(
                    extensions = launchClientAppExtensionsModel(
                        paymentUrl = orderInfo?.paymentUrl,
                        launchUrl = uri.toString(),
                        succeeded = true
                    )
                )
            )
        } catch (e: Exception) {
            onSdkProblemOccurred(PaymentSessionProblem.ClientAppLaunchFailed)
            BeaconService.logEvent(
                eventAction = EventAction.LaunchClientApp(
                    extensions = launchClientAppExtensionsModel(
                        paymentUrl = orderInfo?.paymentUrl,
                        launchUrl = uri.toString(),
                        succeeded = false
                    )
                )
            )
        }
    }

    private fun onPaymentComplete(url: String) {
        when (url) {
            orderInfo?.completeUrl -> {
                _paymentSessionState.value = PaymentSessionState.PaymentSessionComplete
                setStateToIdle()
                BeaconService.logEvent(
                    eventAction = EventAction.SDKCallbackInvoked(
                        method = MethodModel(
                            name = "paymentComplete",
                            sdk = true,
                            succeeded = true
                        )
                    )
                )
                clearState()
            }

            orderInfo?.cancelUrl -> {
                _paymentSessionState.value = PaymentSessionState.PaymentSessionCanceled
                setStateToIdle()

                BeaconService.logEvent(
                    eventAction = EventAction.SDKCallbackInvoked(
                        method = MethodModel(
                            name = "paymentCanceled",
                            sdk = true,
                            succeeded = true
                        )
                    )
                )
                clearState()
            }

            else -> {
                onSdkProblemOccurred(PaymentSessionProblem.PaymentSessionEndReached)
            }
        }

    }


    private fun onSdkProblemOccurred(paymentSessionProblem: PaymentSessionProblem) {
        // We can't recover from these two problems and because of that we clear the state
        if (paymentSessionProblem is PaymentSessionProblem.PaymentSessionEndReached
            || paymentSessionProblem is PaymentSessionProblem.InternalInconsistencyError
        ) {
            clearState()
        }

        _paymentSessionState.value = PaymentSessionState.SdkProblemOccurred(paymentSessionProblem)
        setStateToIdle()

        BeaconService.logEvent(
            eventAction = EventAction.SDKCallbackInvoked(
                method = MethodModel(
                    name = "sdkProblemOccurred",
                    sdk = true,
                    succeeded = true
                ),
                extensions = paymentSessionProblem.toExtensionsModel()
            )
        )
    }

    private fun onSessionProblemOccurred(problemDetails: ProblemDetails) {
        _paymentSessionState.value = PaymentSessionState.SessionProblemOccurred(problemDetails)
        setStateToIdle()

        BeaconService.logEvent(
            eventAction = EventAction.SDKCallbackInvoked(
                method = MethodModel(
                    name = "sessionProblemOccurred",
                    sdk = true,
                    succeeded = true
                ),
                extensions = problemDetails.toExtensionsModel()
            )
        )
    }

    private fun setStateToIdle() {
        _paymentSessionState.value = PaymentSessionState.Idle
    }

    /**
     * To be able to destruct list where the size can vary
     */
    operator fun <T> List<T>.component1(): T? = if (isNotEmpty()) get(0) else null
    operator fun <T> List<T>.component2(): T? = if (size > 1) get(1) else null

}