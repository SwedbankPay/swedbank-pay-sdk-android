package com.swedbankpay.mobilesdk.nativepayments


import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.nativepayments.api.NativePaymentsAPIClient
import com.swedbankpay.mobilesdk.nativepayments.model.response.Instrument
import com.swedbankpay.mobilesdk.nativepayments.model.response.MethodBaseModel
import com.swedbankpay.mobilesdk.nativepayments.model.response.NativePaymentResponse
import com.swedbankpay.mobilesdk.nativepayments.model.response.RequestMethod
import com.swedbankpay.mobilesdk.nativepayments.model.response.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.net.URL

class NativePayments(
    configuration: Configuration
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var currentSessionState: Session? = null

    private var chosenInstrumentForThePayment: Instrument? = null

    private val client: NativePaymentsAPIClient by lazy {
        NativePaymentsAPIClient()
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
                when (val nativePaymentResponse = client.executeNextRequest(nextStep)) {
                    is NativePaymentResponse.PaymentError -> {
                        onError(nativePaymentResponse.paymentError.detail)
                        break
                    }

                    is NativePaymentResponse.UnknownError -> {
                        onError(nativePaymentResponse.message)
                        break
                    }

                    is NativePaymentResponse.Success -> {
                        currentSessionState = nativePaymentResponse.session
                        SessionOperationHandler.getNextStep(
                            session = currentSessionState,
                            instrument = chosenInstrumentForThePayment
                        ).let { step ->
                            nextStep = step
                            if (step.instruction != null
                            ) {
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

    /**
     * This method will start the native payment session and return
     * a list with available payment methods aka instruments (Swish, Credit Card)
     *
     * @param url An url to start the payment session. Will come from merchants backend
     * @param onSuccess Contains a list with available payment method
     * @param onError Contains an error message
     *
     */
    fun startNativePaymentSession(
        url: String,
        onSuccess: (List<MethodBaseModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        executeNextStepUntilFurtherInstructions(
            operationStep = OperationStep(
                requestMethod = RequestMethod.GET,
                url = URL(url)
            ),
            onSuccess = { stepInstruction ->
                when (stepInstruction) {
                    is StepInstruction.AvailableInstrumentStep ->
                        onSuccess(stepInstruction.availableInstruments)

                    else -> onError("Wrong step")
                }
            },
            onError = onError
        )
    }

    fun startNativePaymentFor(
        instrument: Instrument,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        chosenInstrumentForThePayment = instrument
        executeNextStepUntilFurtherInstructions(
            operationStep = SessionOperationHandler.getOperationStepForInstrumentViews(
                currentSessionState,
                instrument
            ),
            onSuccess = { stepInstruction ->
                when (stepInstruction) {
                    is StepInstruction.LaunchSwishAppStep -> onSuccess(stepInstruction.uri)
                    else -> onError("Wrong step")
                }
            },
            onError = onError
        )
    }

}