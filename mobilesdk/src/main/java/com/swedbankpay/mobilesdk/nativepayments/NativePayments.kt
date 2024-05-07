package com.swedbankpay.mobilesdk.nativepayments


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.PaymentViewModel
import com.swedbankpay.mobilesdk.internal.CallbackActivity
import com.swedbankpay.mobilesdk.nativepayments.api.NativePaymentsAPIClient
import com.swedbankpay.mobilesdk.nativepayments.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.nativepayments.model.response.NativePaymentResponse
import com.swedbankpay.mobilesdk.nativepayments.model.response.Problem
import com.swedbankpay.mobilesdk.nativepayments.model.response.RequestMethod
import com.swedbankpay.mobilesdk.nativepayments.model.response.Session
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

class NativePayments(
    val configuration: Configuration
) {
    companion object {
        private val _nativePaymentState: MutableLiveData<NativePaymentState> = MutableLiveData()
        val nativePaymentState: LiveData<NativePaymentState> = _nativePaymentState
    }

    private val callbackUrlObserver = Observer<Unit> {
        checkCallbacks()
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var currentSessionState: Session? = null

    private var choosenPaymentAttempInstrument: PaymentAttemptInstrument? = null

    private var currentPaymentState: PaymentViewModel.State = PaymentViewModel.State.IDLE

    private var paymentSessionUrl: URL? = null

    private val client: NativePaymentsAPIClient by lazy {
        NativePaymentsAPIClient()
    }

    private val viewPaymentOrderInfo = configuration.postNativePaymentOrders()

    init {
        CallbackActivity.onCallbackUrlInvoked.observeForever(callbackUrlObserver)
    }

    private fun checkCallbacks() {
        val callbackUrl = viewPaymentOrderInfo.paymentUrl
        if (callbackUrl != null && CallbackActivity.consumeCallbackUrl(callbackUrl)) {
            currentPaymentState = PaymentViewModel.State.COMPLETE
            getPaymentSession()
        }
    }

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
                if (nextStep.delayRequestDuration > 0) {
                    delay(nextStep.delayRequestDuration)
                }
                when (val nativePaymentResponse = client.executeNextRequest(nextStep)) {
                    is NativePaymentResponse.PaymentError -> {
                        withContext(Dispatchers.Main) {
                            onError(nativePaymentResponse.paymentError.detail)
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
                            paymentState = currentPaymentState,
                            instrument = choosenPaymentAttempInstrument
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
     * @param url An url to start the payment session. Will come from merchants backend
     *
     */
    fun startNativePaymentSession(
        url: String,
    ) {
        paymentSessionUrl = URL(url)
        currentPaymentState = PaymentViewModel.State.IN_PROGRESS
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
                            NativePaymentState.AvailablePaymentMethods(stepInstruction.availableInstruments)

                    is StepInstruction.PaymentSessionCompleted -> {
                        onPaymentComplete(stepInstruction.url)
                    }

                    is StepInstruction.ProblemOccurred -> {
                        onProblemOccurred(stepInstruction.problem)
                    }

                    else -> _nativePaymentState.value = NativePaymentState.Error("Wrong step")
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
        instrument: PaymentAttemptInstrument,
    ) {
        currentPaymentState = PaymentViewModel.State.IN_PROGRESS
        choosenPaymentAttempInstrument = instrument
        executeNextStepUntilFurtherInstructions(
            operationStep = SessionOperationHandler.getOperationStepForPaymentAttempt(
                currentSessionState,
                instrument
            ),
            onSuccess = { stepInstruction ->
                when (stepInstruction) {
                    is StepInstruction.LaunchSwishAppStep -> {
                        val swishUri = stepInstruction.uri.addCallbackUrl(configuration)

                        if (swishUri != null) {
                            _nativePaymentState.value = NativePaymentState.LaunchSwish(swishUri)
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

    private fun onPaymentComplete(url: String) {
        currentPaymentState = PaymentViewModel.State.IDLE
        when (url) {
            configuration.postNativePaymentOrders().completeUrl -> {
                _nativePaymentState.value = NativePaymentState.PaymentComplete
            }

            configuration.postNativePaymentOrders().cancelUrl -> {
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
        _nativePaymentState.value = NativePaymentState.NativeProblem(
            title = problem.title,
            status = problem.status,
            detail = problem.detail
        )
    }

}