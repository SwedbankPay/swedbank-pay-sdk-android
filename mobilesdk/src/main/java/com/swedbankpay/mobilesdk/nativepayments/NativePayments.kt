package com.swedbankpay.mobilesdk.nativepayments


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.internal.CallbackActivity
import com.swedbankpay.mobilesdk.nativepayments.api.NativePaymentsAPIClient
import com.swedbankpay.mobilesdk.nativepayments.model.response.Instrument
import com.swedbankpay.mobilesdk.nativepayments.model.response.NativePaymentResponse
import com.swedbankpay.mobilesdk.nativepayments.model.response.RequestMethod
import com.swedbankpay.mobilesdk.nativepayments.model.response.Session
import com.swedbankpay.mobilesdk.nativepayments.util.NativePaymentState
import com.swedbankpay.mobilesdk.nativepayments.util.SwishUriUtil.toIntentUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
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
        Log.d("sessionui", "we are back")
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var currentSessionState: Session? = null

    private var chosenInstrumentForThePayment: Instrument? = null

    private val client: NativePaymentsAPIClient by lazy {
        NativePaymentsAPIClient()
    }

    init {
        CallbackActivity.onCallbackUrlInvoked.observeForever(callbackUrlObserver)
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
                            instrument = chosenInstrumentForThePayment
                        ).let { step ->
                            nextStep = step
                            if (step.instruction != null
                            ) {
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
        executeNextStepUntilFurtherInstructions(
            operationStep = OperationStep(
                requestMethod = RequestMethod.GET,
                url = URL(url)
            ),
            onSuccess = { stepInstruction ->
                when (stepInstruction) {
                    is StepInstruction.AvailableInstrumentStep ->
                        _nativePaymentState.value =
                            NativePaymentState.AvailablePaymentMethods(stepInstruction.availableInstruments.map {
                                it.instrument ?: Instrument.SWISH
                            })

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
    fun startNativePaymentFor(
        instrument: Instrument,
    ) {
        chosenInstrumentForThePayment = instrument
        executeNextStepUntilFurtherInstructions(
            operationStep = SessionOperationHandler.getOperationStepForInstrumentViews(
                currentSessionState,
                instrument
            ),
            onSuccess = { stepInstruction ->
                when (stepInstruction) {
                    is StepInstruction.LaunchSwishAppStep -> {
                        val swishUri = stepInstruction.uri.toIntentUri(configuration)

                        if (swishUri != null) {
                            _nativePaymentState.value = NativePaymentState.LaunchSwish(swishUri)
                        } else {
                            _nativePaymentState.value =
                                NativePaymentState.Error("Not a valid swish url")
                        }
                    }

                    else -> _nativePaymentState.value = NativePaymentState.Error("Wrong step")
                }
            },
            onError = {
                _nativePaymentState.value = NativePaymentState.Error(it)
            }
        )
    }

}