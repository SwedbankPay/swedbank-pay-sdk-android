package com.swedbankpay.mobilesdk.nativepayments


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.swedbankpay.mobilesdk.ViewPaymentOrderInfo
import com.swedbankpay.mobilesdk.internal.CallbackActivity
import com.swedbankpay.mobilesdk.logging.BeaconService
import com.swedbankpay.mobilesdk.logging.model.EventAction
import com.swedbankpay.mobilesdk.logging.model.MethodModel
import com.swedbankpay.mobilesdk.nativepayments.api.NativePaymentsAPIClient
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.IntegrationTaskRel
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.NativePaymentResponse
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.OperationRel
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.ProblemDetails
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.RequestMethod
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.PaymentOutputModel
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.NativePaymentProblem
import com.swedbankpay.mobilesdk.nativepayments.util.UriCallbackUtil.addCallbackUrl
import com.swedbankpay.mobilesdk.nativepayments.util.clientAppCallbackExtensionsModel
import com.swedbankpay.mobilesdk.nativepayments.util.launchClientAppExtensionsModel
import com.swedbankpay.mobilesdk.nativepayments.util.extension.safeLet
import com.swedbankpay.mobilesdk.nativepayments.util.toExtensionsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.net.URL


/**
 * Class that handles native payments
 *
 * @param orderInfo information that provides `NativePayment` with callback URLs.
 */
class NativePayment(
    private val orderInfo: ViewPaymentOrderInfo
) {
    companion object {
        /**
         * Contains the state of the native payment process
         */
        private var _nativePaymentState: MutableLiveData<NativePaymentState> =
            MutableLiveData(NativePaymentState.Idle)
        val nativePaymentState: LiveData<NativePaymentState> = _nativePaymentState

        private const val RETRIES_TIME_LIMIT_IN_MS = 20 * 1000
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

    private val client: NativePaymentsAPIClient by lazy {
        NativePaymentsAPIClient()
    }

    private fun startObservingCallbacks() {
        CallbackActivity.onCallbackUrlInvoked.observeForever(callbackUrlObserver)
    }

    private fun stopObservingCallbacks() {
        CallbackActivity.onCallbackUrlInvoked.removeObserver(callbackUrlObserver)
    }

    private fun clearState(clearBeaconQueue: Boolean = false) {
        paymentAttemptInstrument = null
        currentPaymentOutputModel = null
        SessionOperationHandler.clearState()
        if (clearBeaconQueue) {
            BeaconService.clearQueue()
        }
    }

    private fun checkCallbacks() {
        val callbackUrl = orderInfo.paymentUrl
        if (callbackUrl != null && CallbackActivity.consumeCallbackUrl(callbackUrl)) {
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
                onSdkProblemOccurred(NativePaymentProblem.PaymentSessionEndReached)
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
                when (val nativePaymentResponse = client.executeNextRequest(stepToExecute)) {
                    is NativePaymentResponse.Retry -> {
                        // If request timer has tried for more than twenty second. Send an error to merchant
                        // or wait one second then retry
                        if (System.currentTimeMillis() - startRequestTimestamp > RETRIES_TIME_LIMIT_IN_MS) {
                            withContext(Dispatchers.Main) {
                                onSdkProblemOccurred((
                                        NativePaymentProblem.PaymentSessionAPIRequestFailed(
                                            error = nativePaymentResponse.error,
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

                    is NativePaymentResponse.Error -> {
                        withContext(Dispatchers.Main) {
                            onSdkProblemOccurred((
                                    NativePaymentProblem.PaymentSessionAPIRequestFailed(
                                        error = nativePaymentResponse.error,
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

                    is NativePaymentResponse.Success -> {
                        currentPaymentOutputModel = nativePaymentResponse.paymentOutputModel
                        BeaconService.setBeaconUrl(
                            SessionOperationHandler.getBeaconUrl(
                                currentPaymentOutputModel
                            )
                        )
                        SessionOperationHandler.getNextStep(
                            paymentOutputModel = currentPaymentOutputModel,
                            paymentAttemptInstrument = paymentAttemptInstrument
                        ).let { step ->
                            clearPaymentAttemptInstrument(
                                stepToExecute.operationRel,
                                step.integrationRel
                            )

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
                                                NativePaymentProblem.InternalInconsistencyError
                                            )
                                        }

                                        is StepInstruction.StepNotFound -> {
                                            onSdkProblemOccurred(
                                                NativePaymentProblem.PaymentSessionEndReached
                                            )
                                        }

                                        else -> {
                                            // In this case instruction shouldn't be null.
                                            // And if so we want to find out fast
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

    /**
     * Clear payment instrument if step rel is [OperationRel.START_PAYMENT_ATTEMPT]
     * and next step rel is not [IntegrationTaskRel.LAUNCH_CLIENT_APP]
     * then we clear it later in the session
     */
    private fun clearPaymentAttemptInstrument(
        operationRel: OperationRel?,
        integrationTaskRel: IntegrationTaskRel?
    ) {
        if (operationRel == OperationRel.START_PAYMENT_ATTEMPT && integrationTaskRel
            != IntegrationTaskRel.LAUNCH_CLIENT_APP
        ) {
            paymentAttemptInstrument = null
        }
    }

    private fun checkWhatTodo(instruction: StepInstruction) {
        when (instruction) {
            is StepInstruction.AvailableInstrumentStep -> {
                _nativePaymentState.value =
                    NativePaymentState.AvailableInstrumentsFetched(instruction.availableInstruments)
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
                onSdkProblemOccurred(NativePaymentProblem.PaymentSessionEndReached)
            }
        }
    }

    /**
     * Starts a new native payment session
     *
     * @param sessionURL URL needed to start the native payment session
     *
     */
    fun startPaymentSession(
        sessionURL: String,
    ) {
        clearState(true)
        startObservingCallbacks()

        BeaconService.logEvent(
            eventAction = EventAction.SDKMethodInvoked(
                method = MethodModel(
                    name = "startPaymentSession",
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
     * Make a payment attempt with a specific instrument
     *
     * There needs to be an active payment session before a payment attempt can be made
     *
     * @param instrument [PaymentAttemptInstrument]
     */
    fun makePaymentAttempt(
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
                        name = "makePaymentAttempt",
                        sdk = true,
                        succeeded = paymentAttemptOperation != null
                    ),
                    extensions = instrument.toExtensionsModel()
                )
            )

        } ?: onSdkProblemOccurred(NativePaymentProblem.InternalInconsistencyError)


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
        } ?: onSdkProblemOccurred(NativePaymentProblem.InternalInconsistencyError)
    }

    private fun launchClientApp(href: String) {
        val uriWithCallback = href.addCallbackUrl(orderInfo)

        safeLet(uriWithCallback, paymentAttemptInstrument) { uri, paymentInstrument ->
            when (paymentInstrument) {
                is PaymentAttemptInstrument.Swish -> {
                    launchSwish(uri, paymentInstrument.localStartContext)
                    paymentAttemptInstrument = null
                }

                else -> {
                    onSdkProblemOccurred(NativePaymentProblem.InternalInconsistencyError)
                }
            }
        } ?: kotlin.run {
            onSdkProblemOccurred(NativePaymentProblem.InternalInconsistencyError)
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
                        paymentUrl = orderInfo.paymentUrl,
                        launchUrl = uri.toString(),
                        succeeded = true
                    )
                )
            )
        } catch (e: Exception) {
            onSdkProblemOccurred(NativePaymentProblem.ClientAppLaunchFailed)
            BeaconService.logEvent(
                eventAction = EventAction.LaunchClientApp(
                    extensions = launchClientAppExtensionsModel(
                        paymentUrl = orderInfo.paymentUrl,
                        launchUrl = uri.toString(),
                        succeeded = false
                    )
                )
            )
        }
    }

    private fun onPaymentComplete(url: String) {
        clearState()
        stopObservingCallbacks()

        when (url) {
            orderInfo.completeUrl -> {
                _nativePaymentState.value = NativePaymentState.PaymentComplete
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

            }

            orderInfo.cancelUrl -> {
                _nativePaymentState.value = NativePaymentState.PaymentCanceled
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

            }

            else -> {
                onSdkProblemOccurred(NativePaymentProblem.PaymentSessionEndReached)
            }
        }

    }


    private fun onSdkProblemOccurred(nativePaymentProblem: NativePaymentProblem) {
        _nativePaymentState.value = NativePaymentState.SdkProblemOccurred(nativePaymentProblem)
        setStateToIdle()

        BeaconService.logEvent(
            eventAction = EventAction.SDKCallbackInvoked(
                method = MethodModel(
                    name = "sdkProblemOccurred",
                    sdk = true,
                    succeeded = true
                ),
                extensions = nativePaymentProblem.toExtensionsModel()
            )
        )
    }

    private fun onSessionProblemOccurred(problemDetails: ProblemDetails) {
        _nativePaymentState.value = NativePaymentState.SessionProblemOccurred(problemDetails)
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
        _nativePaymentState.value = NativePaymentState.Idle
    }

    /**
     * To be able to destruct list where the size can vary
     */
    operator fun <T> List<T>.component1(): T? = if (isNotEmpty()) get(0) else null
    operator fun <T> List<T>.component2(): T? = if (size > 1) get(1) else null

}