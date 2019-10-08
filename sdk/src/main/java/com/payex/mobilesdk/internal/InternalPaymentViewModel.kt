package com.payex.mobilesdk.internal

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.payex.mobilesdk.*
import com.payex.mobilesdk.internal.remote.RequestProblemException
import com.payex.mobilesdk.internal.remote.json.Link
import com.payex.mobilesdk.internal.remote.json.readLink
import com.payex.mobilesdk.internal.remote.json.writeLink
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import java.io.IOException

@MainThread
internal class InternalPaymentViewModel(app: Application) : AndroidViewModel(app) {
    private companion object {
        const val KEY_STATE = "s"
    }

    private val processState = MutableLiveData<ProcessState>()
    private val moveToNextStateJob = MutableLiveData<Job>()

    val uiState = Transformations.map(processState) { it?.uiState }

    val loading = Transformations.map(uiState) { it == UIState.Loading }
    val currentPage = Transformations.map(uiState) { it?.getWebViewPage(this) }
    val messageTitle = Transformations.map(uiState) {
        when (it) {
            is UIState.InitializationError -> R.string.payexsdk_bad_init_request_title
            is UIState.RetryableError -> R.string.payexsdk_retryable_error_title
            UIState.Success -> R.string.payexsdk_payment_success
            is UIState.Failure -> when (it.terminalFailure) {
                null -> R.string.payexsdk_payment_failed
                else -> R.string.payexsdk_terminal_failure_title
            }
            else -> null
        }?.let(getApplication<Application>()::getString)
    }
    val messageBody = Transformations.map(uiState) {
        when (it) {
            is UIState.InitializationError -> it.problem.getFriendlyDescription()
            is UIState.RetryableError -> sequenceOf(
                getApplication<Application>().getString(it.message),
                it.problem?.getFriendlyDescription(),
                it.ioException?.localizedMessage
            ).filterNotNull().joinToString("\n\n")
            is UIState.Failure -> it.terminalFailure?.messageId?.let {
                getApplication<Application>().getString(R.string.payexsdk_terminal_failure_message, it)
            }
            else -> null
        }
    }
    val retryActionAvailable = Transformations.map(uiState) { it is UIState.RetryableError }

    val termsOfServiceUrl = MutableLiveData<String>()

    val javascriptInterface = JSInterface(this)

    var configuration: Configuration? = null

    var publicVm: PaymentViewModel? = null
        set(value) {
            val old = field
            if (old != value) {
                old?.detachInternalViewModel(this)
                field = value
                value?.attachInternalViewModel(this)
            }
        }

    override fun onCleared() {
        super.onCleared()
        javascriptInterface.vm = null
        configuration = null
        publicVm = null
    }

    private fun setProcessState(processState: ProcessState?) {
        moveToNextStateJob.value?.cancel()
        moveToNextStateJob.value = null
        this.processState.value = processState
        if (processState?.shouldProceedAutomatically == true) {
            moveToNextState()
        }
    }

    private fun moveToNextState() {
        moveToNextStateJob.value?.cancel()
        moveToNextStateJob.value = viewModelScope.launch {
            try {
                val configuration = configuration
                if (configuration == null) {
                    Log.e(LOG_TAG, "moveToNextState Job active with null configuration; ignoring")
                    return@launch
                }
                val nextState = processState.value?.getNextState(getApplication(), configuration)
                moveToNextStateJob.value = null
                setProcessState(nextState)
            } catch (_: CancellationException) {}
        }
    }

    @TestOnly
    internal suspend fun waitForNonTransientState() {
        var job = moveToNextStateJob.value
        while (job != null) {
            job.join()
            job = moveToNextStateJob.value
        }
    }

    fun startStoredOrAnonymousCustomer(consumerProfileRef: String?, merchantData: String?) = start {
        ProcessState.CreatingPaymentOrder(consumerProfileRef, merchantData)
    }

    fun startIdentifiedCustomer(consumerIdentificationData: String, merchantData: String?) = start {
        ProcessState.InitializingConsumerSession(consumerIdentificationData, merchantData)
    }

    private inline fun start(f: () -> ProcessState) {
        if (processState.value == null) {
            setProcessState(f())
        }
    }

    fun saveState(bundle: Bundle) {
        processState.value?.let { bundle.putParcelable(KEY_STATE, it) }
    }

    fun resumeFromSavedState(bundle: Bundle) {
        setProcessState(bundle.getParcelable(KEY_STATE))
    }

    fun retryFromRetryableError() {
        if (processState.value?.isRetryableErrorState == true) {
            moveToNextState()
        }
    }

    fun onConsumerProfileRefAvailable(consumerProfileRef: String) {
        processState.value
            ?.getNextStateAfterConsumerProfileRefAvailable(consumerProfileRef)
            ?.let(::setProcessState)
    }

    fun onPaymentToS(url: String) {
        // See comment at PaymentViewModel.retryPreviousAction()
        termsOfServiceUrl.value = url
        termsOfServiceUrl.value = null
    }

    fun onPaymentSuccess() {
        setProcessState(ProcessState.Success)
    }

    fun onPaymentFailed() {
        processState.value
            ?.getNextStateAfterPaymentFailed()
            ?.let(::setProcessState)
    }

    fun onError(terminalFailure: TerminalFailure?) {
        setProcessState(ProcessState.PayExError(terminalFailure))
    }

    private fun Problem.getFriendlyDescription(): String {
        val resId = when (this) {
            is Problem.Client.MobileSDK.Unauthorized -> R.string.payexsdk_problem_unauthorized
            is Problem.Client.MobileSDK.InvalidRequest -> R.string.payexsdk_problem_invalid_request
            is Problem.Client.PayEx.InputError -> R.string.payexsdk_problem_input_error
            is Problem.Client.PayEx.Forbidden -> R.string.payexsdk_problem_forbidden
            is Problem.Client.PayEx.NotFound -> R.string.payexsdk_problem_not_found
            is Problem.Client.Unknown -> R.string.payexsdk_problem_unknown
            is Problem.Client.UnexpectedContent -> R.string.payexsdk_problem_unexpected_content
            is Problem.Server.MobileSDK.BackendConnectionTimeout -> R.string.payexsdk_problem_backend_connection_timeout
            is Problem.Server.MobileSDK.BackendConnectionFailure -> R.string.payexsdk_problem_backend_connection_failure
            is Problem.Server.MobileSDK.InvalidBackendResponse -> R.string.payexsdk_problem_invalid_backend_response
            is Problem.Server.PayEx.SystemError -> R.string.payexsdk_problem_system_error
            is Problem.Server.PayEx.ConfigurationError -> R.string.payexsdk_problem_configuration_error
            is Problem.Server.Unknown -> R.string.payexsdk_problem_unknown
            is Problem.Server.UnexpectedContent -> R.string.payexsdk_problem_unexpected_content
        }
        return getApplication<Application>().getString(resId)
    }

    sealed class UIState {
        open fun getWebViewPage(vm: AndroidViewModel): String? = null

        object Loading : UIState()
        class HtmlContent(@StringRes val template: Int, val link: String) : UIState() {
            override fun getWebViewPage(vm: AndroidViewModel): String? {
                return vm.getApplication<Application>().getString(template, link)
            }
        }
        class InitializationError(val problem: Problem) : UIState()
        class RetryableError(val problem: Problem?, val ioException: IOException?, @StringRes val message: Int) : UIState()
        object Success : UIState()
        class Failure(val terminalFailure: TerminalFailure?) : UIState()
    }

    @Suppress("unused") // The IDE does not understand @JvmField val CREATOR
    private sealed class ProcessState : Parcelable {
        abstract val uiState: UIState

        open val shouldProceedAutomatically get() = false
        open val isRetryableErrorState get() = false
        open suspend fun getNextState(context: Context, configuration: Configuration) = this
        open fun getNextStateAfterConsumerProfileRefAvailable(consumerProfileRef: String): ProcessState? = null
        open fun getNextStateAfterPaymentFailed(): ProcessState? = null

        override fun describeContents() = 0

        val Problem.isRetryable get() = this is Problem.Server && this !is Problem.Server.PayEx.ConfigurationError

        class InitializingConsumerSession(
            private val consumerIdentificationData: String,
            private val merchantData: String?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::InitializingConsumerSession)
            }

            override val uiState get() = UIState.Loading

            override val shouldProceedAutomatically get() = true
            override suspend fun getNextState(context: Context, configuration: Configuration): ProcessState {
                return try {
                    val operations = configuration
                        .getTopLevelResources(context)
                        .consumers
                        .post(context, configuration, consumerIdentificationData)
                        .operations
                    val viewConsumerIdentification =
                        operations.find("view-consumer-identification")?.href
                            ?: throw IOException("Missing required operation")
                    IdentifyingConsumer(viewConsumerIdentification, merchantData)
                } catch (e: RequestProblemException) {
                    FailedInitializeConsumerSession(consumerIdentificationData, merchantData, e.problem, null)
                } catch (e: Exception) {
                    FailedInitializeConsumerSession(consumerIdentificationData, merchantData, null, e as? IOException)
                }
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(consumerIdentificationData)
                parcel.writeString(merchantData)
            }
            constructor(parcel: Parcel) : this(
                checkNotNull(parcel.readString()),
                parcel.readString()
            )
        }

        class FailedInitializeConsumerSession(
            private val consumerIdentificationData: String,
            private val merchantData: String?,
            private val problem: Problem?,
            private val ioException: IOException?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::FailedInitializeConsumerSession)
            }

            override val uiState
                get() = if (problem?.isRetryable == false) {
                    UIState.InitializationError(problem)
                } else {
                    UIState.RetryableError(problem, ioException, R.string.payexsdk_failed_init_consumer)
                }

            override val isRetryableErrorState get() = true
            override suspend fun getNextState(context: Context, configuration: Configuration): ProcessState {
                return InitializingConsumerSession(consumerIdentificationData, merchantData)
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(consumerIdentificationData)
                parcel.writeString(merchantData)
                parcel.writeParcelable(problem, flags)
                parcel.writeSerializable(ioException)
            }
            constructor(parcel: Parcel) : this(
                checkNotNull(parcel.readString()),
                parcel.readString(),
                parcel.readParcelable(Problem::class.java.classLoader),
                parcel.readSerializable() as? IOException
            )
        }

        class IdentifyingConsumer(
            private val viewConsumerIdentification: String,
            private val merchantData: String?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::IdentifyingConsumer)
            }

            override val uiState get() = UIState.HtmlContent(R.string.payexsdk_view_consumer_identification_template, viewConsumerIdentification)

            override fun getNextStateAfterConsumerProfileRefAvailable(consumerProfileRef: String): ProcessState? {
                return CreatingPaymentOrder(consumerProfileRef, merchantData)
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(viewConsumerIdentification)
                parcel.writeString(merchantData)
            }
            constructor(parcel: Parcel) : this(
                checkNotNull(parcel.readString()),
                parcel.readString()
            )
        }

        class CreatingPaymentOrder(
            private val consumerProfileRef: String?,
            private val merchantData: String?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::CreatingPaymentOrder)
            }

            override val uiState get() = UIState.Loading

            override val shouldProceedAutomatically get() = true
            override suspend fun getNextState(context: Context, configuration: Configuration): ProcessState {
                return try {
                    val paymentOrder = configuration
                        .getTopLevelResources(context)
                        .paymentorders
                        .post(context, configuration, consumerProfileRef, merchantData)
                    val viewPaymentOrder =
                        paymentOrder.operations.find("view-paymentorder")?.href
                            ?: throw IOException("Missing required operation")
                    Paying(viewPaymentOrder, paymentOrder.url)
                } catch (e: RequestProblemException) {
                    FailedCreatePaymentOrder(consumerProfileRef, merchantData, e.problem, null)
                } catch (e: Exception) {
                    FailedCreatePaymentOrder(consumerProfileRef, merchantData, null, e as? IOException)
                }
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(consumerProfileRef)
                parcel.writeString(merchantData)
            }

            constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString()
            )
        }

        class FailedCreatePaymentOrder(
            private val consumerProfileRef: String?,
            private val merchantData: String?,
            private val problem: Problem?,
            private val ioException: IOException?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::FailedCreatePaymentOrder)
            }

            override val uiState
                get() = if (problem?.isRetryable == false) {
                    UIState.InitializationError(problem)
                } else {
                    UIState.RetryableError(problem, ioException, R.string.payexsdk_failed_create_payment)
                }

            override val isRetryableErrorState get() = true
            override suspend fun getNextState(context: Context, configuration: Configuration): ProcessState {
                return CreatingPaymentOrder(consumerProfileRef, merchantData)
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(consumerProfileRef)
                parcel.writeString(merchantData)
                parcel.writeParcelable(problem, flags)
                parcel.writeSerializable(ioException)
            }
            constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readParcelable(Problem::class.java.classLoader),
                parcel.readSerializable() as? IOException
            )
        }

        class Paying(
            private val viewPaymentOrder: String,
            private val paymentOrderUrl: Link.PaymentOrder
        ) : ProcessState() {
            companion object { @JvmField val CREATOR = makeCreator(::Paying) }

            override val uiState get() = UIState.HtmlContent(R.string.payexsdk_view_paymentorder_template, viewPaymentOrder)

            override fun getNextStateAfterPaymentFailed() = PaymentFailed(paymentOrderUrl)

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(viewPaymentOrder)
                parcel.writeLink(paymentOrderUrl)
            }
            constructor(parcel: Parcel) : this(
                checkNotNull(parcel.readString()),
                checkNotNull(parcel.readLink(Link::PaymentOrder))
            )
        }

        object Success : ProcessState() {
            override val uiState get() = UIState.Success

            override fun writeToParcel(parcel: Parcel, flags: Int) {}
            @JvmField
            val CREATOR = object : Parcelable.Creator<Success> {
                override fun createFromParcel(parcel: Parcel) = Success
                override fun newArray(size: Int) = arrayOfNulls<Success>(size)
            }
        }

        class PaymentFailed(
            private val paymentOrderUrl: Link.PaymentOrder
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::PaymentFailed)
            }

            override val uiState get() = UIState.Failure(null)

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeLink(paymentOrderUrl)
            }
            constructor(parcel: Parcel) : this(
                checkNotNull(parcel.readLink(Link::PaymentOrder))
            )
        }

        class PayExError(
            private val terminalFailure: TerminalFailure?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::PayExError)
            }

            override val uiState get() = UIState.Failure(terminalFailure)

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(terminalFailure, flags)
            }
            constructor(parcel: Parcel) : this(
                parcel.readParcelable<TerminalFailure>(TerminalFailure::class.java.classLoader)
            )
        }
    }

}