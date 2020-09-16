package com.swedbankpay.mobilesdk.internal

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.swedbankpay.mobilesdk.*
import com.swedbankpay.mobilesdk.R
import com.swedbankpay.mobilesdk.internal.remote.RequestProblemException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.Serializable

@MainThread
internal class InternalPaymentViewModel(app: Application) : AndroidViewModel(app) {
    private companion object {
        const val KEY_STATE = "s"
    }

    private val callbackUrlObserver = Observer<Unit> {
        checkCallbacks()
    }

    private val processState = MutableLiveData<ProcessState>()
    private val moveToNextStateJob = MutableLiveData<Job>()

    var reloadRequested = false

    val enabledDefaultUI = MutableLiveData<@PaymentFragment.DefaultUI Int>().apply { value = 0 }

    val uiState = Transformations.map(processState) { it?.uiState }

    val loading = Transformations.map(uiState) { it == UIState.Loading }
    val currentHtmlContent = Transformations.map(uiState) { it?.asHtmlContent }

    val messageTitle = mapUIState {
        when {
            it is UIState.InitializationError && isEnabled(PaymentFragment.ERROR_MESSAGE) ->
                R.string.swedbankpaysdk_bad_init_request_title

            it is UIState.RetryableError && isEnabled(PaymentFragment.RETRY_PROMPT) ->
                R.string.swedbankpaysdk_retryable_error_title

            it == UIState.Complete && isEnabled(PaymentFragment.SUCCESS_MESSAGE) ->
                R.string.swedbankpaysdk_payment_success

            it is UIState.Failure && isEnabled(PaymentFragment.ERROR_MESSAGE) ->
                when (it.terminalFailure) {
                    null -> R.string.swedbankpaysdk_payment_failed
                    else -> R.string.swedbankpaysdk_terminal_failure_title
                }

            else -> null
        }?.let(getApplication<Application>()::getString)
    }

    val messageBody = mapUIState {
        when {
            it is UIState.InitializationError && isEnabled(PaymentFragment.ERROR_MESSAGE) ->
                it.problem.getFriendlyDescription()

            it is UIState.RetryableError && isEnabled(PaymentFragment.RETRY_PROMPT) ->
                getApplication<Application>().run {
                    sequenceOf(
                        getString(it.message),
                        it.problem?.getFriendlyDescription(),
                        it.exception?.localizedMessage,
                        getString(R.string.swedbankpaysdk_pull_to_refresh)
                    )
                }.filterNotNull().joinToString("\n\n")

            it is UIState.Failure && isEnabled(PaymentFragment.ERROR_MESSAGE) ->
                it.terminalFailure?.messageId?.let { messageId ->
                    getApplication<Application>().getString(R.string.swedbankpaysdk_terminal_failure_message, messageId)
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

    init {
        CallbackActivity.onCallbackUrlInvoked.observeForever(callbackUrlObserver)
    }
    
    var useExternalBrowser: Boolean = false
        private set

    var debugIntentUris = false

    override fun onCleared() {
        super.onCleared()
        CallbackActivity.onCallbackUrlInvoked.removeObserver(callbackUrlObserver)
        javascriptInterface.vm = null
        configuration = null
        publicVm = null
    }

    private class UIStateContext(
        @PaymentFragment.DefaultUI
        private val enabledDefaultUI: Int
    ) {
        fun isEnabled(@PaymentFragment.DefaultUI defaultUI: Int): Boolean {
            return enabledDefaultUI and defaultUI != 0
        }
    }
    private fun <T> mapUIState(f: UIStateContext.(UIState?) -> T): LiveData<T> {
        return MediatorLiveData<T>().apply {
            val observer = Observer<Any?> {
                val enabledUI = enabledDefaultUI.value ?: 0
                val state = uiState.value
                value = UIStateContext(enabledUI).f(state)
            }
            addSource(enabledDefaultUI, observer)
            addSource(uiState, observer)
        }
    }

    private fun checkCallbacks() {
        val payingState = processState.value as? ProcessState.Paying
        val callbackUrl = payingState?.paymentUrl
        if (callbackUrl != null && CallbackActivity.consumeCallbackUrl(callbackUrl)) {
            reloadPaymentPage(payingState)
        }
    }

    private fun reloadPaymentPage(payingState: ProcessState.Paying) {
        reloadRequested = true
        // Call setValue to make observers rebind themselves
        // This will, in effect, cause the payment page to be reloaded.
        processState.value = payingState
    }

    private fun setProcessState(processState: ProcessState?) {
        moveToNextStateJob.value?.cancel()
        moveToNextStateJob.value = null
        this.processState.value = processState
        if (processState?.shouldProceedAutomatically == true) {
            moveToNextState()
        } else {
            checkCallbacks()
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

    fun start(
        useCheckin: Boolean,
        consumer: Consumer?,
        paymentOrder: PaymentOrder?,
        userData: Any?,
        useBrowser: Boolean
    ) {
        useExternalBrowser = useBrowser
        if (processState.value == null) {
            val state = if (useCheckin) {
                ProcessState.InitializingConsumerSession(consumer, paymentOrder, userData)
            } else {
                ProcessState.CreatingPaymentOrder(null, paymentOrder, userData)
            }
            setProcessState(state)
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

    fun onError(terminalFailure: TerminalFailure?) {
        setProcessState(ProcessState.SwedbankPayError(terminalFailure))
    }

    fun getPaymentMenuHtmlContent(): UIState.HtmlContent? {
        return (processState.value as? ProcessState.Paying)?.uiState
    }

    fun overrideNavigation(uri: Uri): Boolean {
        val payingState = processState.value as? ProcessState.Paying ?: return false
        when (val uriString = uri.toString()) {
            payingState.completeUrl -> setProcessState(ProcessState.Complete)
            payingState.cancelUrl -> setProcessState(ProcessState.Canceled)
            payingState.paymentUrl -> reloadPaymentPage(payingState)
            payingState.termsOfServiceUrl -> {
                // See comment at PaymentViewModel.retryPreviousAction()
                termsOfServiceUrl.value = uriString
                termsOfServiceUrl.value = null
            }
            else -> return false
        }
        return true
    }

    private fun Problem.getFriendlyDescription(): String {
        val resId = when (this) {
            is Problem.Client.MobileSDK.Unauthorized -> R.string.swedbankpaysdk_problem_unauthorized
            is Problem.Client.MobileSDK.InvalidRequest -> R.string.swedbankpaysdk_problem_invalid_request
            is Problem.Client.SwedbankPay.InputError -> R.string.swedbankpaysdk_problem_input_error
            is Problem.Client.SwedbankPay.Forbidden -> R.string.swedbankpaysdk_problem_forbidden
            is Problem.Client.SwedbankPay.NotFound -> R.string.swedbankpaysdk_problem_not_found
            is Problem.Client.Unknown -> R.string.swedbankpaysdk_problem_unknown
            is Problem.Client.UnexpectedContent -> R.string.swedbankpaysdk_problem_unexpected_content
            is Problem.Server.MobileSDK.BackendConnectionTimeout -> R.string.swedbankpaysdk_problem_backend_connection_timeout
            is Problem.Server.MobileSDK.BackendConnectionFailure -> R.string.swedbankpaysdk_problem_backend_connection_failure
            is Problem.Server.MobileSDK.InvalidBackendResponse -> R.string.swedbankpaysdk_problem_invalid_backend_response
            is Problem.Server.SwedbankPay.SystemError -> R.string.swedbankpaysdk_problem_system_error
            is Problem.Server.SwedbankPay.ConfigurationError -> R.string.swedbankpaysdk_problem_configuration_error
            is Problem.Server.Unknown -> R.string.swedbankpaysdk_problem_unknown
            is Problem.Server.UnexpectedContent -> R.string.swedbankpaysdk_problem_unexpected_content
        }
        return getApplication<Application>().getString(resId)
    }



    sealed class UIState {
        open val asHtmlContent: HtmlContent? get() = null

        object Loading : UIState()
        class HtmlContent(
            val baseUrl: String?,
            @StringRes private val template: Int,
            private val link: String
        ) : UIState() {
            override val asHtmlContent get() = this
            fun getWebViewPage(context: Context): String {
                return context.getString(template, link)
            }
        }
        class InitializationError(val problem: Problem) : UIState()
        class RetryableError(val problem: Problem?, val exception: Exception?, @StringRes val message: Int) : UIState()
        class ConfigurationError(val exception: IllegalStateException) : UIState()
        object Complete : UIState()
        object Canceled : UIState()
        class Failure(val terminalFailure: TerminalFailure?) : UIState()
    }

    @Suppress("unused") // The IDE does not understand @JvmField val CREATOR
    private sealed class ProcessState : Parcelable {
        companion object {
            private const val USER_DATA_NULL = 0
            private const val USER_DATA_PARCELABLE = 1
            private const val USER_DATA_STRING = 2
            private const val USER_DATA_SERIALIZABLE = 3
            protected fun Parcel.writeUserData(userData: Any?, flags: Int) {
                when (userData) {
                    null -> writeInt(USER_DATA_NULL)
                    is Parcelable -> {
                        writeInt(USER_DATA_PARCELABLE)
                        writeParcelable(userData, flags)
                    }
                    is String -> {
                        writeInt(USER_DATA_STRING)
                        writeString(userData)
                    }
                    is Serializable -> {
                        writeInt(USER_DATA_SERIALIZABLE)
                        writeSerializable(userData)
                    }
                    else -> {
                        error("userData must be Parcelable or Serializable")
                    }
                }
            }
            protected fun Parcel.readUserData() = when (readInt()) {
                USER_DATA_NULL -> null
                USER_DATA_PARCELABLE -> readParcelable(ProcessState::class.java.classLoader)
                USER_DATA_STRING -> readString()
                USER_DATA_SERIALIZABLE -> readSerializable()
                else -> error("invalid userData type")
            }
        }

        abstract val uiState: UIState

        open val shouldProceedAutomatically get() = false
        open val isRetryableErrorState get() = false
        open suspend fun getNextState(context: Context, configuration: Configuration) = this
        open fun getNextStateAfterConsumerProfileRefAvailable(consumerProfileRef: String): ProcessState? = null

        override fun describeContents() = 0

        val Problem.isRetryable get() = this is Problem.Server && this !is Problem.Server.SwedbankPay.ConfigurationError

        class InitializingConsumerSession(
            private val consumer: Consumer?,
            private val paymentOrder: PaymentOrder?,
            private val userData: Any?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::InitializingConsumerSession)
            }

            override val uiState get() = UIState.Loading

            override val shouldProceedAutomatically get() = true
            override suspend fun getNextState(context: Context, configuration: Configuration): ProcessState {
                return try {
                    val viewConsumerIdentificationInfo = configuration
                        .postConsumers(context, consumer, userData)
                    IdentifyingConsumer(
                        viewConsumerIdentificationInfo.webViewBaseUrl,
                        viewConsumerIdentificationInfo.viewConsumerIdentification,
                        paymentOrder,
                        userData
                    )
                } catch (e: RequestProblemException) {
                    FailedInitializeConsumerSession(consumer, paymentOrder, userData, e.problem, null)
                } catch (e: Exception) {
                    FailedInitializeConsumerSession(consumer, paymentOrder, userData, null, e)
                }
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(consumer, flags)
                parcel.writeParcelable(paymentOrder, flags)
                parcel.writeUserData(userData, flags)
            }
            constructor(parcel: Parcel) : this(
                consumer = parcel.readParcelable(),
                paymentOrder = parcel.readParcelable(),
                userData = parcel.readUserData()
            )
        }

        class FailedInitializeConsumerSession(
            private val consumer: Consumer?,
            private val paymentOrder: PaymentOrder?,
            private val userData: Any?,
            private val problem: Problem?,
            private val exception: Exception?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::FailedInitializeConsumerSession)
            }

            override val uiState
                get() = when {
                    problem?.isRetryable == false -> UIState.InitializationError(problem)
                    exception is IllegalStateException -> UIState.ConfigurationError(exception)
                    else -> UIState.RetryableError(
                        problem,
                        exception,
                        R.string.swedbankpaysdk_failed_init_consumer
                    )
                }

            override val isRetryableErrorState get() = true
            override suspend fun getNextState(context: Context, configuration: Configuration): ProcessState {
                return InitializingConsumerSession(consumer, paymentOrder, userData)
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(consumer, flags)
                parcel.writeParcelable(paymentOrder, flags)
                parcel.writeUserData(userData, flags)
                parcel.writeParcelable(problem, flags)
                parcel.writeSerializable(exception)
            }
            constructor(parcel: Parcel) : this(
                consumer = parcel.readParcelable(),
                paymentOrder = parcel.readParcelable(),
                userData = parcel.readUserData(),
                problem = parcel.readParcelable(),
                exception = parcel.readSerializable() as? Exception
            )
        }

        class IdentifyingConsumer(
            private val baseUrl: String?,
            private val viewConsumerIdentification: String,
            private val paymentOrder: PaymentOrder?,
            private val userData: Any?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::IdentifyingConsumer)
            }

            override val uiState get() = UIState.HtmlContent(
                baseUrl,
                R.string.swedbankpaysdk_view_consumer_identification_template,
                viewConsumerIdentification
            )

            override fun getNextStateAfterConsumerProfileRefAvailable(consumerProfileRef: String): ProcessState? {
                return CreatingPaymentOrder(consumerProfileRef, paymentOrder, userData)
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(baseUrl)
                parcel.writeString(viewConsumerIdentification)
                parcel.writeParcelable(paymentOrder, flags)
                parcel.writeUserData(userData, flags)
            }
            constructor(parcel: Parcel) : this(
                baseUrl = parcel.readString(),
                viewConsumerIdentification = checkNotNull(parcel.readString()),
                paymentOrder = parcel.readParcelable(),
                userData = parcel.readUserData()
            )
        }

        class CreatingPaymentOrder(
            private val consumerProfileRef: String?,
            private val paymentOrder: PaymentOrder?,
            private val userData: Any?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::CreatingPaymentOrder)
            }

            override val uiState get() = UIState.Loading

            override val shouldProceedAutomatically get() = true
            override suspend fun getNextState(context: Context, configuration: Configuration): ProcessState {
                return try {
                    val viewPaymentOrderInfo = configuration
                        .postPaymentorders(context, paymentOrder, userData, consumerProfileRef)
                    Paying(viewPaymentOrderInfo)
                } catch (e: RequestProblemException) {
                    FailedCreatePaymentOrder(consumerProfileRef, paymentOrder, userData, e.problem, null)
                } catch (e: Exception) {
                    FailedCreatePaymentOrder(consumerProfileRef, paymentOrder, userData, null, e)
                }
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(consumerProfileRef)
                parcel.writeParcelable(paymentOrder, flags)
                parcel.writeUserData(userData, flags)
            }
            constructor(parcel: Parcel) : this(
                consumerProfileRef = parcel.readString(),
                paymentOrder = parcel.readParcelable(),
                userData = parcel.readUserData()
            )
        }

        class FailedCreatePaymentOrder(
            private val consumerProfileRef: String?,
            private val paymentOrder: PaymentOrder?,
            private val userData: Any?,
            private val problem: Problem?,
            private val exception: Exception?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::FailedCreatePaymentOrder)
            }

            override val uiState
                get() = when {
                    problem?.isRetryable == false -> UIState.InitializationError(problem)
                    exception is IllegalStateException -> UIState.ConfigurationError(exception)
                    else -> UIState.RetryableError(
                        problem,
                        exception,
                        R.string.swedbankpaysdk_failed_create_payment
                    )
                }

            override val isRetryableErrorState get() = true
            override suspend fun getNextState(context: Context, configuration: Configuration): ProcessState {
                return CreatingPaymentOrder(consumerProfileRef, paymentOrder, userData)
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(consumerProfileRef)
                parcel.writeParcelable(paymentOrder, flags)
                parcel.writeUserData(userData, flags)
                parcel.writeParcelable(problem, flags)
                parcel.writeSerializable(exception)
            }
            constructor(parcel: Parcel) : this(
                consumerProfileRef = parcel.readString(),
                paymentOrder = parcel.readParcelable(),
                userData = parcel.readUserData(),
                problem = parcel.readParcelable(),
                exception = parcel.readSerializable() as? Exception
            )
        }

        class Paying(
            private val baseUrl: String?,
            private val viewPaymentOrder: String,
            val completeUrl: String,
            val cancelUrl: String?,
            val paymentUrl: String?,
            val termsOfServiceUrl: String?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR = makeCreator(::Paying) }

            constructor(viewPaymentOrderInfo: ViewPaymentOrderInfo) : this(
                baseUrl = viewPaymentOrderInfo.webViewBaseUrl,
                viewPaymentOrder = viewPaymentOrderInfo.viewPaymentOrder,
                completeUrl = viewPaymentOrderInfo.completeUrl,
                cancelUrl = viewPaymentOrderInfo.cancelUrl,
                paymentUrl = viewPaymentOrderInfo.paymentUrl,
                termsOfServiceUrl = viewPaymentOrderInfo.termsOfServiceUrl
            )

            override val uiState get() = UIState.HtmlContent(
                baseUrl,
                R.string.swedbankpaysdk_view_paymentorder_template,
                viewPaymentOrder
            )

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(baseUrl)
                parcel.writeString(viewPaymentOrder)
                parcel.writeString(completeUrl)
                parcel.writeString(cancelUrl)
                parcel.writeString(paymentUrl)
                parcel.writeString(termsOfServiceUrl)
            }
            constructor(parcel: Parcel) : this(
                baseUrl = parcel.readString(),
                viewPaymentOrder = checkNotNull(parcel.readString()),
                completeUrl = checkNotNull(parcel.readString()),
                cancelUrl = parcel.readString(),
                paymentUrl = parcel.readString(),
                termsOfServiceUrl = parcel.readString()
            )
        }

        object Complete : ProcessState() {
            override val uiState get() = UIState.Complete

            override fun writeToParcel(parcel: Parcel, flags: Int) {}
            @JvmField val CREATOR = makeCreator { Complete }
        }

        object Canceled : ProcessState() {
            override val uiState get() = UIState.Canceled

            override fun writeToParcel(parcel: Parcel, flags: Int) {}
            @JvmField val CREATOR = makeCreator { Canceled }
        }

        class SwedbankPayError(
            private val terminalFailure: TerminalFailure?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR =
                makeCreator(::SwedbankPayError)
            }

            override val uiState get() = UIState.Failure(terminalFailure)

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(terminalFailure, flags)
            }
            constructor(parcel: Parcel) : this(
                parcel.readParcelable<TerminalFailure>()
            )
        }
    }

}
