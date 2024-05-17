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
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.NativePaymentResponse
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.Problem
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.RequestMethod
import com.swedbankpay.mobilesdk.nativepayments.api.model.response.Session
import com.swedbankpay.mobilesdk.nativepayments.util.NativePaymentState
import com.swedbankpay.mobilesdk.nativepayments.util.SwishUriUtil.addCallbackUrl
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
        private val _nativePaymentState: MutableLiveData<NativePaymentState> = MutableLiveData()
        val nativePaymentState: LiveData<NativePaymentState> = _nativePaymentState
    }

    /**
     * When redirected from swish this callback will be triggered
     */
    private val callbackUrlObserver = Observer<Unit> {
        checkCallbacks()
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var currentSessionState: Session? = null

    private var chosenPaymentAttemptInstrument: PaymentAttemptInstrument? = null

    private var paymentSessionUrl: URL? = null

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
        operationStep: OperationStep,
        onSuccess: (StepInstruction) -> Unit,
        onError: (String) -> Unit
    ) {
        // So we don't launch multiple jobs when calling this method again
        scope.coroutineContext.cancelChildren()
        scope.launch {
            var nextStep = operationStep

            while (nextStep.instruction == null) {
                // This is here for polling purposes
                if (nextStep.delayRequestDuration > 0) {
                    delay(nextStep.delayRequestDuration)
                }
                when (val nativePaymentResponse = client.executeNextRequest(nextStep)) {
                    is NativePaymentResponse.PaymentError -> {
                        withContext(Dispatchers.Main) {
                            onError(
                                nativePaymentResponse.paymentError.detail ?: "Something went wrong"
                            )
                        }
                        break
                    }

                    is NativePaymentResponse.UnknownError -> {
                        withContext(Dispatchers.Main) {
                            onError(nativePaymentResponse.message)
                        }
                        break
                    }

                    is NativePaymentResponse.Success -> {
                        currentSessionState = nativePaymentResponse.session
                        SessionOperationHandler.getNextStep(
                            session = currentSessionState,
                            instrument = chosenPaymentAttemptInstrument
                        ).let { step ->
                            nextStep = step
                            if (step.instruction != null
                            ) {
                                if (step.instruction is StepInstruction.ProblemOccurred) {
                                    client.postFailedAttemptRequest(step.instruction.problem)
                                }
                                withContext(Dispatchers.Main) {
                                    if (step.instruction.errorMessage != null) {
                                        onError(step.instruction.errorMessage)
                                    } else {
                                        onSuccess(step.instruction)
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
     * This method will start the native payment session
     *
     * @param url An url to start the payment session. Will be supplied by merchant
     *
     */
    fun startPaymentSession(
        url: String,
    ) {
        paymentSessionUrl = URL(url)
        SessionOperationHandler.clearUsedSwishUrls()
        getPaymentSession()
    }

    private fun getPaymentSession() {
        executeNextStepUntilFurtherInstructions(
            operationStep = OperationStep(
                requestMethod = RequestMethod.GET,
                url = paymentSessionUrl
            ),
            onSuccess = { stepInstruction ->
                when (stepInstruction) {
                    is StepInstruction.AvailableInstrumentStep ->
                        _nativePaymentState.value =
                            NativePaymentState.AvailableInstrumentsFetched(stepInstruction.availableInstruments)

                    is StepInstruction.PaymentSessionCompleted -> {
                        onPaymentComplete(stepInstruction.url)
                    }

                    is StepInstruction.ProblemOccurred -> {
                        onProblemOccurred(stepInstruction.problem)
                    }

                    else -> _nativePaymentState.value = NativePaymentState.Error("Internal error")
                }
            },
            onError = {
                _nativePaymentState.value = NativePaymentState.Error(it)
            }
        )
    }

    /**
     * This method will start a native payment with the supplied payment method aka instrument
     */
    fun makePaymentAttempt(
        with: PaymentAttemptInstrument,
    ) {
        chosenPaymentAttemptInstrument = with
        executeNextStepUntilFurtherInstructions(
            operationStep = SessionOperationHandler.getOperationStepForPaymentAttempt(
                currentSessionState,
                with
            ),
            onSuccess = { stepInstruction ->
                when (stepInstruction) {
                    is StepInstruction.LaunchSwishAppStep -> {
                        val swishUri = stepInstruction.uri.addCallbackUrl(orderInfo)

                        if (swishUri != null && with is PaymentAttemptInstrument.Swish) {
                            launchClientApp(swishUri, with.localStartContext)
                        } else {
                            _nativePaymentState.value =
                                NativePaymentState.Error("Not a valid swish url")
                        }
                    }

                    is StepInstruction.PaymentSessionCompleted -> {
                        onPaymentComplete(stepInstruction.url)
                    }

                    is StepInstruction.ProblemOccurred -> {
                        onProblemOccurred(stepInstruction.problem)
                    }

                    else -> _nativePaymentState.value =
                        NativePaymentState.Error("Inconsistent state. Please abort the payment and try again.")
                }
            },
            onError = {
                _nativePaymentState.value = NativePaymentState.Error(it)
            }
        )
    }

    /**
     * This method aborts the payment completely
     * After this a new call to [startPaymentSession] must be done to make a new payment attempt
     */
    fun abortPaymentSession() {
        executeNextStepUntilFurtherInstructions(
            operationStep = SessionOperationHandler.getOperationStepForAbortPayment(
                currentSessionState
            ),
            onSuccess = { stepInstruction ->
                when (stepInstruction) {
                    is StepInstruction.PaymentSessionCompleted -> {
                        onPaymentComplete(stepInstruction.url)
                    }

                    is StepInstruction.ProblemOccurred -> {
                        onProblemOccurred(stepInstruction.problem)
                    }

                    else -> _nativePaymentState.value =
                        NativePaymentState.Error("Inconsistent state. Please abort the payment and try again.")
                }
            },
            onError = {
                _nativePaymentState.value = NativePaymentState.Error(it)
            }
        )
    }

    private fun launchClientApp(uri: Uri, context: Context?) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context?.startActivity(intent)
        } catch (e: Exception) {
           // TODO Beacon logging that app was not able to launch
        }
    }

    private fun onPaymentComplete(url: String) {
        currentSessionState = null
        chosenPaymentAttemptInstrument = null

        when (url) {
            orderInfo.completeUrl -> {
                _nativePaymentState.value = NativePaymentState.PaymentSucceeded
            }

            orderInfo.cancelUrl -> {
                _nativePaymentState.value =
                    NativePaymentState.Error("Payment failed")
            }

            else -> {
                _nativePaymentState.value =
                    NativePaymentState.Error("Something strange happened")
            }
        }
    }

    private fun onProblemOccurred(problem: Problem) {
        _nativePaymentState.value = NativePaymentState.PaymentFailed(
            title = problem.title ?: "Error",
            status = problem.status ?: -1,
            detail = problem.detail ?: "Something went wrong"
        )
    }

}