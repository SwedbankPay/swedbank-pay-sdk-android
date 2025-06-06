package com.swedbankpay.mobilesdk.paymentsession


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.swedbankpay.mobilesdk.PaymentFragment
import com.swedbankpay.mobilesdk.PaymentViewModel
import com.swedbankpay.mobilesdk.ViewPaymentOrderInfo
import com.swedbankpay.mobilesdk.internal.CallbackActivity
import com.swedbankpay.mobilesdk.internal.PaymentFragmentStateBridge
import com.swedbankpay.mobilesdk.logging.BeaconService
import com.swedbankpay.mobilesdk.logging.model.EventAction
import com.swedbankpay.mobilesdk.logging.model.ExtensionsModel
import com.swedbankpay.mobilesdk.logging.model.MethodModel
import com.swedbankpay.mobilesdk.paymentsession.api.PaymentSessionAPIClient
import com.swedbankpay.mobilesdk.paymentsession.api.model.SwedbankPayAPIError
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.FailPaymentAttempt
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.FailPaymentAttemptProblemType
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.util.TimeOutUtil
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ApiErrorType
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.GooglePayMethodModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.IntegrationTask
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.OperationRel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PaymentOutputModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.PaymentSessionResponse
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ProblemDetails
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.RequestMethod
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.translatedType
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentSessionProblem
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.SwedbankPayPaymentSessionSDKControllerMode
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.isSwishLocalDevice
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.toInstrument
import com.swedbankpay.mobilesdk.paymentsession.googlepay.GooglePayService
import com.swedbankpay.mobilesdk.paymentsession.sca.ScaRedirectFragment
import com.swedbankpay.mobilesdk.paymentsession.util.UriCallbackUtil.addCallbackUrl
import com.swedbankpay.mobilesdk.paymentsession.util.clientAppCallbackExtensionsModel
import com.swedbankpay.mobilesdk.paymentsession.util.configuration.AutomaticConfiguration
import com.swedbankpay.mobilesdk.paymentsession.util.extension.safeLet
import com.swedbankpay.mobilesdk.paymentsession.util.extension.setValueIfChanged
import com.swedbankpay.mobilesdk.paymentsession.util.googlePayPaymentReadinessExtensionModel
import com.swedbankpay.mobilesdk.paymentsession.util.launchClientAppExtensionsModel
import com.swedbankpay.mobilesdk.paymentsession.util.livedata.QueuedMutableLiveData
import com.swedbankpay.mobilesdk.paymentsession.util.toExtensionModel
import com.swedbankpay.mobilesdk.paymentsession.util.toExtensionsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

data class ScaResult(
    val cRes: String? = null,
    val cReq: String? = null,
    val error: PaymentSessionProblem? = null
)

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
        private var _paymentSessionState: QueuedMutableLiveData<PaymentSessionState> =
            QueuedMutableLiveData()
        val paymentSessionState: LiveData<PaymentSessionState> = _paymentSessionState

        private var scaResult: QueuedMutableLiveData<ScaResult?> = QueuedMutableLiveData()

        internal fun onScaResult(
            cRes: String? = null,
            cReq: String? = null,
            error: PaymentSessionProblem? = null
        ) {
            scaResult.setValueIfChanged(
                ScaResult(
                    cRes = cRes,
                    cReq = cReq,
                    error = error
                )
            )
        }
    }

    /**
     * When redirected from swish this callback will be triggered
     */
    private val callbackUrlObserver = Observer<Unit> {
        checkCallbacks()
    }

    /**
     * Observing payment fragment state
     */
    private val paymentFragmentObserver = Observer<PaymentViewModel.State?> {
        when (it) {
            PaymentViewModel.State.COMPLETE -> {
                onPaymentSessionComplete()
            }

            PaymentViewModel.State.CANCELED,
            PaymentViewModel.State.FAILURE -> {
                onPaymentSessionCanceled()
            }

            else -> {}
        }
    }

    private var scaRedirectObserver : Observer<ScaResult?>? = null

    private val mainScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var initialSessionURL: String? = null

    /**
     * Is used for various tasks that needs context to work
     */
    private var currentContext: Context? = null

    /**
     * Is used when opening google pay
     */
    private var currentActivity: Activity? = null

    private var currentPaymentOutputModel: PaymentOutputModel? = null

    private var paymentAttemptInstrument: PaymentAttemptInstrument? = null

    private var sdkControllerMode: SwedbankPayPaymentSessionSDKControllerMode? = null

    private var startRequestTimestamp: Long = 0

    private var isPaymentFragmentActive = false

    private var stopExecutingNextStep = false

    /**
     * Styling for the payment menu
     *
     * Styling the payment menu requires a separate agreement with Swedbank Pay.
     */
    var paymentMenuStyle: Map<*, *>? = null

    private val client: PaymentSessionAPIClient by lazy {
        PaymentSessionAPIClient()
    }

    /**
     * Observe callbacks from CallbackActivity only if Swish on local device is used
     */
    private fun startObservingCallbacks() {
        if (paymentAttemptInstrument?.isSwishLocalDevice() == true) {
            CallbackActivity.onNativeCallbackUrlInvoked.observeForever(callbackUrlObserver)
        }
    }

    private fun stopObservingCallbacks() {
        CallbackActivity.onNativeCallbackUrlInvoked.removeObserver(callbackUrlObserver)
    }

    private fun clearState(isStartingSession: Boolean = false) {
        clearPaymentAttemptInstrument()
        clearSdkControllerMode()
        initialSessionURL = null
        currentContext = null
        currentActivity = null
        currentPaymentOutputModel = null
        SessionOperationHandler.clearState()
        stopObservingCallbacks()
        stopObservingPaymentFragmentPaymentProcess()
        stopObservingScaRedirect()
        isPaymentFragmentActive = false

        if (isStartingSession) {
            BeaconService.clearQueue()
        } else {
            orderInfo = null
        }
    }

    private fun checkCallbacks() {
        val callbackUrl = orderInfo?.paymentUrl
        if (callbackUrl != null && CallbackActivity.consumeNativeCallbackUrl(callbackUrl)) {
            if (!isPaymentFragmentActive) {
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
    }

    /**
     * This method will call the api until we need to inform the
     * merchant app to take some kind of action
     */
    private fun executeNextStepUntilFurtherInstructions(
        operationStep: OperationStep,
        automaticRetry: Boolean = true,
        originalStep: OperationStep? = null
    ) {
        stopExecutingNextStep = false
        startRequestTimestamp = System.currentTimeMillis()

        mainScope.launch {
            var stepToExecute: OperationStep = operationStep

            while (stepToExecute.instruction?.waitForAction != true && !stopExecutingNextStep) {

                // This is here for polling purposes
                if (stepToExecute.delayRequestDuration > 0) {
                    delay(stepToExecute.delayRequestDuration)

                    // When we poll we need to reset requestTimestamp so we don't end it to early
                    startRequestTimestamp = System.currentTimeMillis()
                }
                val paymentSessionResponse =
                    client.executeNextRequest(
                        stepToExecute,
                        paymentAttemptInstrument
                    )

                // Check if coroutine scope is still active. If not don't bother do do things
                // Coroutine will not be active if we are waiting for action from merchant
                ensureActive()
                when (paymentSessionResponse) {
                    is PaymentSessionResponse.Retry -> {
                        if ((System.currentTimeMillis() - startRequestTimestamp >
                                    TimeOutUtil.getSessionTimeout(
                                        operationStep.operationRel,
                                        paymentAttemptInstrument?.toInstrument()
                                    )) || !automaticRetry
                        ) {
                            withContext(Dispatchers.Main) {
                                onSdkProblemOccurred(
                                    PaymentSessionProblem.PaymentSessionAPIRequestFailed(
                                        error = paymentSessionResponse.error,
                                        retry = {
                                            startObservingCallbacks()
                                            executeNextStepUntilFurtherInstructions(
                                                originalStep ?: stepToExecute
                                            )
                                        }
                                    )
                                )
                            }
                            break
                        } else {
                            delay(1000)
                        }
                    }

                    is PaymentSessionResponse.Error -> {
                        withContext(Dispatchers.Main) {
                            onSdkProblemOccurred(
                                PaymentSessionProblem.PaymentSessionAPIRequestFailed(
                                    error = paymentSessionResponse.error,
                                    retry = {
                                        startObservingCallbacks()
                                        executeNextStepUntilFurtherInstructions(
                                            originalStep ?: stepToExecute
                                        )
                                    }
                                )
                            )
                        }
                        break
                    }

                    is PaymentSessionResponse.OperationError -> {
                        withContext(Dispatchers.Main) {
                            val apiErrorType = paymentSessionResponse.apiError.translatedType()

                            val problem =
                                when {
                                    apiErrorType == ApiErrorType.OPERATION_NOT_ALLOWED
                                            && operationStep.operationRel == OperationRel.ABORT_PAYMENT -> {
                                        PaymentSessionProblem.AbortPaymentNotAllowed
                                    }

                                    (apiErrorType == ApiErrorType.OPERATION_NOT_ALLOWED
                                            && initialSessionURL != null
                                            && operationStep.operationRel != OperationRel.GET_PAYMENT) ||
                                            (apiErrorType == ApiErrorType.GENERIC_OPERATION_ERROR
                                                    && initialSessionURL != null
                                                    && operationStep.operationRel != OperationRel.GET_PAYMENT) -> {
                                        // If we're getting a 409 server error where Session API can't find or won't allow the operation,
                                        // and it's not a result of a getPayment call, attempt a single getPayment call to recover
                                        executeNextStepUntilFurtherInstructions(
                                            operationStep = OperationStep(
                                                requestMethod = RequestMethod.GET,
                                                url = URL(initialSessionURL)
                                            ),
                                            automaticRetry = false,
                                            originalStep = stepToExecute
                                        )

                                        null
                                    }

                                    else -> {
                                        PaymentSessionProblem.PaymentSessionAPIRequestFailed(
                                            error = SwedbankPayAPIError.Unknown,
                                            retry = {
                                                startObservingCallbacks()
                                                executeNextStepUntilFurtherInstructions(
                                                    originalStep ?: stepToExecute
                                                )
                                            }
                                        )
                                    }
                                }
                            if (problem != null) {
                                onSdkProblemOccurred(problem)
                            }
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
                            paymentAttemptInstrument = paymentAttemptInstrument,
                            sdkControllerMode = sdkControllerMode,
                            currentContext = currentContext,
                            clearPaymentAttemptInstrument = {
                                clearPaymentAttemptInstrument()
                            },
                            onProblemOccurred = { problem, showProblem ->
                                if (showProblem) {
                                    mainScope.launch {
                                        withContext(Dispatchers.Main) {
                                            onSessionProblemOccurred(problem)
                                        }
                                    }
                                }
                                client.postFailedAttemptRequest(problem)
                            }
                        )?.let { step ->
                            stepToExecute = step
                            if (step.instruction != null
                            ) {

                                val instruction = step.instruction

                                withContext(Dispatchers.Main) {
                                    when (instruction) {
                                        is StepInstruction.SessionNotFound -> {
                                            mainScope.coroutineContext.cancelChildren(null)
                                            onSdkProblemOccurred(
                                                PaymentSessionProblem.InternalInconsistencyError
                                            )
                                        }

                                        is StepInstruction.InternalError -> {
                                            mainScope.coroutineContext.cancelChildren(null)
                                            onSdkProblemOccurred(
                                                PaymentSessionProblem.InternalInconsistencyError
                                            )
                                        }

                                        is StepInstruction.StepNotFound -> {
                                            mainScope.coroutineContext.cancelChildren(null)
                                            onSdkProblemOccurred(
                                                PaymentSessionProblem.PaymentSessionEndReached
                                            )
                                        }

                                        else -> {
                                            if (instruction.waitForAction == true) {
                                                mainScope.coroutineContext.cancelChildren(null)
                                                checkWhatTodo(instruction)
                                            }
                                        }
                                    }
                                }
                            }
                        } ?: run {
                            stopExecutingNextStep = true
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
                    it?.rel == OperationRel.VIEW_PAYMENT
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

    private fun clearSdkControllerMode(
    ) {
        sdkControllerMode = null
    }

    private fun checkWhatTodo(instruction: StepInstruction) {
        when (instruction) {
            is StepInstruction.AvailableInstrumentStep -> {
                _paymentSessionState.setValue(PaymentSessionState.PaymentSessionFetched(instruction.availableInstruments))

                BeaconService.logEvent(
                    eventAction = EventAction.SDKCallbackInvoked(
                        method = MethodModel(
                            name = "availableInstrumentsFetched",
                            succeeded = true
                        ),
                        extensions = ExtensionsModel(
                            values = mutableMapOf(
                                "instruments" to instruction.availableInstrumentsForLogging
                            )
                        )
                    )
                )
            }

            is StepInstruction.ScaRedirectStep -> {
                show3DSecure(instruction.task)
            }

            is StepInstruction.GooglePayStep -> {
                launchGooglePay(instruction.task)
            }

            is StepInstruction.LaunchClientAppStep -> {
                launchClientApp(instruction.href)
            }

            is StepInstruction.CreatePaymentFragmentStep -> {
                createPaymentFragment()
            }

            is StepInstruction.PaymentSessionCompleted -> {
                onPaymentComplete(instruction.url)
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
        sessionURL: String
    ) {
        clearState(true)

        initialSessionURL = sessionURL

        BeaconService.logEvent(
            eventAction = EventAction.SDKMethodInvoked(
                method = MethodModel(
                    name = "fetchPaymentSession",
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
     * Fetches users ability to pay with google pay
     *
     * There needs to be an active payment session including Google Pay before this can be use
     *
     * @param context Context is needed to determine users ability to pay with google pay
     */
    fun fetchGooglePayPaymentReadiness(context: Context) {
        currentPaymentOutputModel?.let { paymentOutputModel ->
            val googlePayMethodModel = paymentOutputModel.paymentSession.methods
                ?.firstOrNull { it is GooglePayMethodModel } as GooglePayMethodModel?

            googlePayMethodModel?.let { googlePay ->
                GooglePayService.fetchCanUseGooglePay(
                    context,
                    googlePay.environment,
                    googlePay.allowedCardAuthMethods,
                    googlePay.cardBrands
                ) { isReadyToPay, isReadyToPayWithExistingPaymentMethod ->
                    BeaconService.logEvent(
                        eventAction = EventAction.SDKMethodInvoked(
                            method = MethodModel(
                                name = "fetchGooglePayPaymentReadiness",
                                succeeded = true
                            ),
                            extensions = googlePayPaymentReadinessExtensionModel(
                                isReadyToPay,
                                isReadyToPayWithExistingPaymentMethod
                            )
                        )
                    )
                    _paymentSessionState.setValue(
                        PaymentSessionState.GooglePayPaymentReadinessFetched(
                            isReadyToPay = isReadyToPay,
                            isReadyToPayWithExistingPaymentMethod = isReadyToPayWithExistingPaymentMethod
                        )
                    )
                }
            }


        } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)

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
        clearSdkControllerMode()
        isPaymentFragmentActive = false

        currentPaymentOutputModel?.let {
            paymentAttemptInstrument = instrument
            currentContext = instrument.context
            currentActivity = (instrument as? PaymentAttemptInstrument.GooglePay)?.activity

            startObservingCallbacks()

            executeNextStepUntilFurtherInstructions(
                OperationStep(
                    instruction = StepInstruction.OverrideApiCall(it)
                )
            )

            BeaconService.logEvent(
                eventAction = EventAction.SDKMethodInvoked(
                    method = MethodModel(
                        name = "makeNativePaymentAttempt",
                        succeeded = true
                    ),
                    extensions = instrument.toExtensionsModel()
                )
            )

        } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
    }

    /**
     * Creates a payment fragment with the supplied mode applied
     */
    fun createPaymentFragment(mode: SwedbankPayPaymentSessionSDKControllerMode) {
        currentPaymentOutputModel?.let {
            sdkControllerMode = if (mode is SwedbankPayPaymentSessionSDKControllerMode.Menu
                && mode.restrictedToInstruments?.isEmpty() == true
            ) {
                mode.copy(restrictedToInstruments = null)
            } else {
                mode
            }

            executeNextStepUntilFurtherInstructions(
                OperationStep(
                    instruction = StepInstruction.OverrideApiCall(it)
                )
            )

            BeaconService.logEvent(
                eventAction = EventAction.SDKMethodInvoked(
                    method = MethodModel(
                        name = "createPaymentFragment",
                        succeeded = true
                    ),
                    extensions = mode.toExtensionModel()
                )
            )
        } ?: run {
            onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
        }
    }

    /**
     * Creates a payment fragment
     */
    private fun createPaymentFragment() {
        clearSdkControllerMode()
        orderInfo?.let {
            val paymentFragment = PaymentFragment()
            PaymentFragment.defaultConfiguration = AutomaticConfiguration(it)

            val argsBuilder = PaymentFragment.ArgumentsBuilder()
                .checkoutV3(true)
                .useBrowser(false)

            paymentMenuStyle?.let {
                argsBuilder.style(it)
            }

            paymentFragment.arguments = argsBuilder.build()

            startObservingPaymentFragmentPaymentProcess()
            isPaymentFragmentActive = true

            _paymentSessionState.setValue(PaymentSessionState.ShowPaymentFragment(paymentFragment))

        } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
    }

    private fun startObservingPaymentFragmentPaymentProcess() {
        PaymentFragmentStateBridge.paymentFragmentState.observeForever(paymentFragmentObserver)
    }

    private fun stopObservingPaymentFragmentPaymentProcess() {
        PaymentFragmentStateBridge.paymentFragmentState.removeObserver(paymentFragmentObserver)
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
                        succeeded = abortPaymentOperation != null
                    )
                )
            )
        } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
    }

    private fun show3DSecure(task: IntegrationTask) {
        scaRedirectObserver = Observer<ScaResult?> { result ->
            if (result != null) {
                if (result.error != null) {
                    onSdkProblemOccurred(result.error)
                } else {
                    safeLet(
                        currentPaymentOutputModel,
                        result.cRes,
                        result.cReq
                    ) { session, cres, creq ->
                        SessionOperationHandler.scaRedirectComplete(creq, cres)
                        executeNextStepUntilFurtherInstructions(
                            operationStep = OperationStep(
                                instruction = StepInstruction.OverrideApiCall(session)
                            )
                        )
                        _paymentSessionState.setValue(PaymentSessionState.Dismiss3DSecureFragment)

                        BeaconService.logEvent(
                            eventAction = EventAction.SDKCallbackInvoked(
                                method = MethodModel(
                                    name = "dismiss3DSecureFragment",
                                    succeeded = true
                                )
                            )
                        )
                    } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
                }
            }
        }

        scaResult.observeForever(scaRedirectObserver!!)

        val scaRedirectFragment = ScaRedirectFragment.newInstance(task)

        _paymentSessionState.setValue(PaymentSessionState.Show3DSecureFragment(scaRedirectFragment))

        BeaconService.logEvent(
            eventAction = EventAction.SDKCallbackInvoked(
                method = MethodModel(
                    name = "show3DSecureFragment",
                    succeeded = true
                )
            )
        )
    }

    private fun stopObservingScaRedirect() {
        scaRedirectObserver?.let {
            scaResult.setValueIfChanged(null)
            scaResult.removeObserver(it)
            scaRedirectObserver = null
        }
    }

    private fun launchClientApp(href: String) {
        val uriWithCallback = href.addCallbackUrl(orderInfo)

        safeLet(uriWithCallback, currentContext) { uri, context ->
            launchSwish(uri, context)
        } ?: run {
            onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
        }
    }

    private fun launchSwish(uri: Uri, context: Context?) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
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
            BeaconService.logEvent(
                eventAction = EventAction.LaunchClientApp(
                    extensions = launchClientAppExtensionsModel(
                        paymentUrl = orderInfo?.paymentUrl,
                        launchUrl = uri.toString(),
                        succeeded = false
                    )
                )
            )

            currentPaymentOutputModel?.let { paymentOutputModel ->
                val failPaymentAttemptOperation =
                    SessionOperationHandler.getOperationStepForFailPaymentAttempt(
                        paymentOutputModel,
                        FailPaymentAttempt(
                            problemType = FailPaymentAttemptProblemType.CLIENT_APP_LAUNCH_FAILED.identifier,
                        )
                    )
                failPaymentAttemptOperation?.let {
                    executeNextStepUntilFurtherInstructions(it)
                } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)

            } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
        }
    }

    private fun launchGooglePay(task: IntegrationTask) {
        safeLet(currentActivity, task.expects) { activity, expectsModels ->
            GooglePayService.launchGooglePay(
                expectsModels.filterNotNull(),
                activity,
            ) { googlePayResult, error ->
                currentPaymentOutputModel?.let { paymentOutputModel ->
                    val operation = when {
                        googlePayResult != null -> {
                            SessionOperationHandler.getOperationStepForAttemptPayload(
                                paymentOutputModel,
                                googlePayResult
                            )
                        }

                        else -> {
                            SessionOperationHandler.getOperationStepForFailPaymentAttempt(
                                paymentOutputModel,
                                FailPaymentAttempt(
                                    problemType = if (error?.userCancelled == true) {
                                        FailPaymentAttemptProblemType.USER_CANCELLED.identifier
                                    } else {
                                        FailPaymentAttemptProblemType.TECHNICAL_ERROR.identifier
                                    },
                                    errorCode = error?.message
                                )
                            )
                        }
                    }

                    operation?.let {
                        executeNextStepUntilFurtherInstructions(operation)
                    } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)

                } ?: onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)

            }
        } ?: run {
            onSdkProblemOccurred(PaymentSessionProblem.InternalInconsistencyError)
        }

        BeaconService.logEvent(
            eventAction = EventAction.SDKMethodInvoked(
                method = MethodModel(
                    name = "launchGooglePay",
                    succeeded = true
                )
            )
        )

    }

    private fun onPaymentComplete(url: String) {
        when (url) {
            orderInfo?.completeUrl -> {
                onPaymentSessionComplete()
            }

            orderInfo?.cancelUrl -> {
                onPaymentSessionCanceled()
            }

            else -> {
                onSdkProblemOccurred(PaymentSessionProblem.PaymentSessionEndReached)
            }
        }
    }

    private fun onPaymentSessionComplete() {
        _paymentSessionState.setValue(PaymentSessionState.PaymentSessionComplete)
        BeaconService.logEvent(
            eventAction = EventAction.SDKCallbackInvoked(
                method = MethodModel(
                    name = "paymentComplete",
                    succeeded = true
                )
            )
        )
        clearState()
    }

    private fun onPaymentSessionCanceled() {
        _paymentSessionState.setValue(PaymentSessionState.PaymentSessionCanceled)
        BeaconService.logEvent(
            eventAction = EventAction.SDKCallbackInvoked(
                method = MethodModel(
                    name = "paymentCanceled",
                    succeeded = true
                )
            )
        )
        clearState()
    }

    private fun onSdkProblemOccurred(paymentSessionProblem: PaymentSessionProblem) {
        // We can't recover from these two problems and because of that we clear the state
        if (paymentSessionProblem is PaymentSessionProblem.PaymentSessionEndReached
            || paymentSessionProblem is PaymentSessionProblem.InternalInconsistencyError
        ) {
            clearState()
        }

        _paymentSessionState.setValue(PaymentSessionState.SdkProblemOccurred(paymentSessionProblem))

        BeaconService.logEvent(
            eventAction = EventAction.SDKCallbackInvoked(
                method = MethodModel(
                    name = "sdkProblemOccurred",
                    succeeded = true
                ),
                extensions = paymentSessionProblem.toExtensionsModel()
            )
        )
    }

    private fun onSessionProblemOccurred(problemDetails: ProblemDetails) {
        _paymentSessionState.setValue(PaymentSessionState.SessionProblemOccurred(problemDetails))

        stopObservingCallbacks()

        BeaconService.logEvent(
            eventAction = EventAction.SDKCallbackInvoked(
                method = MethodModel(
                    name = "sessionProblemOccurred",
                    succeeded = true
                ),
                extensions = problemDetails.toExtensionsModel()
            )
        )
    }

    /**
     * To be able to destruct list where the size can vary
     */
    operator fun <T> List<T>.component1(): T? = if (isNotEmpty()) get(0) else null
    operator fun <T> List<T>.component2(): T? = if (size > 1) get(1) else null

}