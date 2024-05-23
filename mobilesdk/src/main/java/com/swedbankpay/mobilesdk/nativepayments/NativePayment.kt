package com.swedbankpay.mobilesdk.nativepayments


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.swedbankpay.mobilesdk.ViewPaymentOrderInfo
import com.swedbankpay.mobilesdk.internal.CallbackActivity
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
import com.swedbankpay.mobilesdk.nativepayments.util.extension.safeLet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class NativePayment(
    private val orderInfo: ViewPaymentOrderInfo
) {
    companion object {
        private var _nativePaymentState: MutableLiveData<NativePaymentState> = MutableLiveData()
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

    private var paymentSessionUrl: URL? = null

    private var startRequestTimestamp: Long = 0

    private val client: NativePaymentsAPIClient by lazy {
        NativePaymentsAPIClient()
    }

    init {
        CallbackActivity.onCallbackUrlInvoked.observeForever(callbackUrlObserver)
    }

    private fun checkCallbacks() {
        val callbackUrl = orderInfo.paymentUrl
        if (callbackUrl != null && CallbackActivity.consumeCallbackUrl(callbackUrl)) {
            getPaymentSession()
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
                                _nativePaymentState.value =
                                    NativePaymentState.SdkProblemOccurred(
                                        NativePaymentProblem.PaymentSessionAPIRequestFailed(
                                            error = nativePaymentResponse.error,
                                            retry = {
                                                executeNextStepUntilFurtherInstructions(
                                                    stepToExecute
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

                    is NativePaymentResponse.Error -> {
                        withContext(Dispatchers.Main) {
                            _nativePaymentState.value =
                                NativePaymentState.SdkProblemOccurred(
                                    NativePaymentProblem.PaymentSessionAPIRequestFailed(
                                        error = nativePaymentResponse.error,
                                        retry = {
                                            executeNextStepUntilFurtherInstructions(
                                                stepToExecute
                                            )
                                        }
                                    )
                                )
                        }
                        break
                    }

                    is NativePaymentResponse.Success -> {
                        currentPaymentOutputModel = nativePaymentResponse.paymentOutputModel
                        SessionOperationHandler.getNextStep(
                            paymentOutputModel = currentPaymentOutputModel,
                            instrument = paymentAttemptInstrument
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
                                    if (instruction is StepInstruction.SessionNotFound
                                        || instruction is StepInstruction.StepNotFound
                                    ) {
                                        _nativePaymentState.value =
                                            NativePaymentState.SdkProblemOccurred(
                                                NativePaymentProblem.PaymentSessionEndReached
                                            )
                                    } else {
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
            is StepInstruction.AvailableInstrumentStep ->
                _nativePaymentState.value =
                    NativePaymentState.AvailableInstrumentsFetched(instruction.availableInstruments)

            is StepInstruction.LaunchClientAppStep -> {
                launchClientApp(instruction.href)
            }

            is StepInstruction.PaymentSessionCompleted -> {
                onPaymentComplete(instruction.url)
            }

            is StepInstruction.ProblemOccurred -> {
                onProblemOccurred(instruction.problemDetails)
            }

            else -> {
                _nativePaymentState.value =
                    NativePaymentState.SdkProblemOccurred(NativePaymentProblem.PaymentSessionEndReached)
            }
        }
    }

    /**
     * This method will start the native payment session
     *
     * @param url An url to start the payment session. Will be supplied by merchant
     *
     */
    fun startPaymentSession(
        url: String,
    ) {
        paymentAttemptInstrument = null
        currentPaymentOutputModel = null
        SessionOperationHandler.clearUsedUrls()

        paymentSessionUrl = URL(url)
        getPaymentSession()
    }

    private fun getPaymentSession() {
        executeNextStepUntilFurtherInstructions(
            operationStep = OperationStep(
                requestMethod = RequestMethod.GET,
                url = paymentSessionUrl
            )
        )
    }

    /**
     * This method will start a native payment with the supplied payment method aka instrument
     */
    fun makePaymentAttempt(
        with: PaymentAttemptInstrument,
    ) {
        paymentAttemptInstrument = with
        executeNextStepUntilFurtherInstructions(
            operationStep = SessionOperationHandler.getOperationStepForPaymentAttempt(
                currentPaymentOutputModel,
                with
            )
        )
    }

    /**
     * This method aborts the payment completely
     * After this a new call to [startPaymentSession] must be done to make a new payment attempt
     */
    fun abortPaymentSession() {
        executeNextStepUntilFurtherInstructions(
            operationStep = SessionOperationHandler.getOperationStepForAbortPayment(
                currentPaymentOutputModel
            )
        )
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
                    // this case should never happen
                }
            }
        } ?: kotlin.run {
            _nativePaymentState.value =
                NativePaymentState.SdkProblemOccurred(NativePaymentProblem.ClientAppLaunchFailed)

        }
    }

    private fun launchSwish(uri: Uri, context: Context?) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context?.startActivity(intent)
        } catch (e: Exception) {
            _nativePaymentState.value =
                NativePaymentState.SdkProblemOccurred(NativePaymentProblem.ClientAppLaunchFailed)
        }
    }

    private fun onPaymentComplete(url: String) {
        currentPaymentOutputModel = null
        paymentAttemptInstrument = null

        when (url) {
            orderInfo.completeUrl -> {
                _nativePaymentState.value = NativePaymentState.PaymentComplete
            }

            orderInfo.cancelUrl -> {
                _nativePaymentState.value =
                    NativePaymentState.PaymentCanceled
            }

            else -> {
                _nativePaymentState.value =
                    NativePaymentState.SdkProblemOccurred(NativePaymentProblem.PaymentSessionEndReached)
            }
        }

    }

    private fun onProblemOccurred(problemDetails: ProblemDetails) {
        _nativePaymentState.value = NativePaymentState.SessionProblemOccurred(problemDetails)
    }

    /**
     * To be able to destruct list where the size can vary
     */
    operator fun <T> List<T>.component1(): T? = if (isNotEmpty()) get(0) else null
    operator fun <T> List<T>.component2(): T? = if (size > 1) get(1) else null

}