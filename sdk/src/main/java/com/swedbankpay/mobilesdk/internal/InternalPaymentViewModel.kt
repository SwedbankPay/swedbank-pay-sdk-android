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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@MainThread
internal class InternalPaymentViewModel(app: Application) : AndroidViewModel(app) {
    private companion object {
        const val KEY_STATE = "s"
    }

    private val callbackUrlObserver = Observer<Unit> {
        checkCallbacks()
    }

    private val processState = MutableLiveData<ProcessState?>()
    private val moveToNextStateJob = MutableLiveData<Job?>()

    val webViewShowingRootPage = MutableLiveData(false)

    var reloadRequested = false

    val enabledDefaultUI = MutableLiveData<@PaymentFragment.DefaultUI Int>().apply { value = 0 }

    val uiState = Transformations.map(processState) { it?.uiState }

    val loading = Transformations.map(uiState) { it == UIState.Loading }

    val updatingPaymentOrder = Transformations.map(uiState) { it is UIState.UpdatingPaymentOrder }

    val currentHtmlContent = MediatorLiveData<HtmlContent>().apply {
        addSource(uiState) {
            // don't update the html in the updating state
            // to prevent unnecessary flicker
            if (it !is UIState.UpdatingPaymentOrder) {
                value = it?.asHtmlContent
            }
        }
    }

    val showingPaymentMenu = MediatorLiveData<Boolean>().apply {
        val observer = Observer<Any?> {
            value = processState.value is ProcessState.Paying
                    && webViewShowingRootPage.value == true
        }
        addSource(processState, observer)
        addSource(webViewShowingRootPage, observer)
    }

    private val message = MediatorLiveData<PaymentFragmentMessage>().apply {
        val observer = Observer<Any?> { _ ->
            configuration?.let { config ->
                value = uiState.value?.let {
                    val enabledUI = enabledDefaultUI.value ?: 0
                    PaymentFragmentMessage.fromUIState(getApplication(), config, it, enabledUI)
                }
            }
        }
        addSource(enabledDefaultUI, observer)
        addSource(uiState, observer)
    }

    val messageTitle = Transformations.map(message) { it?.title }

    val messageBody = Transformations.map(message) { it?.body }

    val retryActionAvailable = Transformations.map(uiState) { it is UIState.RetryableError }

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

    private fun checkCallbacks() {
        val payingState = processState.value as? ProcessState.Paying
        val callbackUrl = payingState?.viewPaymentOrderInfo?.paymentUrl
        if (callbackUrl != null && CallbackActivity.consumeCallbackUrl(callbackUrl)) {
            reloadPaymentPage()
        }
    }

    private fun reloadPaymentPage() {
        reloadRequested = true
        currentHtmlContent.apply {
            // Call setValue to make observers rebind themselves
            // This will, in effect, cause the payment page to be reloaded.
            value = value
        }
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
                val nextState = processState.value?.getNextState(
                    this@InternalPaymentViewModel,
                    configuration
                )
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

    fun updatePaymentOrder(updateInfo: Any?) {
        processState.value
            ?.getStateForUpdatingPaymentOrder(updateInfo)
            ?.let(::setProcessState)
    }

    fun onConsumerProfileRefAvailable(consumerProfileRef: String) {
        processState.value
            ?.getNextStateAfterConsumerProfileRefAvailable(consumerProfileRef)
            ?.let(::setProcessState)
    }

    fun onError(terminalFailure: TerminalFailure?) {
        setProcessState(ProcessState.SwedbankPayError(terminalFailure))
    }

    fun getPaymentMenuHtmlContent(): HtmlContent? {
        return (processState.value as? ProcessState.Paying)?.uiState?.asHtmlContent
    }

    fun overrideNavigation(uri: Uri): Boolean {
        val payingState = processState.value as? ProcessState.Paying ?: return false
        val viewPaymentOrderInfo = payingState.viewPaymentOrderInfo
        when (val uriString = uri.toString()) {
            viewPaymentOrderInfo.completeUrl -> setProcessState(ProcessState.Complete)
            viewPaymentOrderInfo.cancelUrl -> setProcessState(ProcessState.Canceled)
            viewPaymentOrderInfo.paymentUrl -> reloadPaymentPage()
            viewPaymentOrderInfo.termsOfServiceUrl -> return publicVm?.run {
                onTermsOfServiceClickListener
                    ?.onTermsOfServiceClick(this, uriString)
            } == true

            else -> return false
        }
        return true
    }

    interface HtmlContent {
        val baseUrl: String?
        fun getWebViewPage(context: Context): String
    }

    sealed class UIState {
        open val asHtmlContent: HtmlContent? get() = null

        object Loading : UIState()
        class ViewConsumerIdentification(
            override val baseUrl: String?,
            private val viewConsumerIdentification: String
        ) : UIState(), HtmlContent {
            override val asHtmlContent get() = this
            override fun getWebViewPage(context: Context): String {
                return context.getString(
                    R.string.swedbankpaysdk_view_consumer_identification_template,
                    viewConsumerIdentification
                )
            }
        }
        class ViewPaymentOrder(
            val viewPaymentOrderInfo: ViewPaymentOrderInfo,
            val updateException: Exception?
        ): UIState(), HtmlContent {
            override val asHtmlContent get() = this
            override val baseUrl get() = viewPaymentOrderInfo.webViewBaseUrl
            override fun getWebViewPage(context: Context): String {
                return context.getString(
                    R.string.swedbankpaysdk_view_paymentorder_template,
                    viewPaymentOrderInfo.viewPaymentOrder
                )
            }
        }

        object UpdatingPaymentOrder : UIState()
        class InitializationError(val exception: Exception) : UIState()
        class RetryableError(val exception: Exception, @StringRes val message: Int) : UIState()
        object Complete : UIState()
        object Canceled : UIState()
        class Failure(val terminalFailure: TerminalFailure?) : UIState()
    }

    private sealed class ProcessState : Parcelable {
        abstract val uiState: UIState

        open val shouldProceedAutomatically get() = false
        open val isRetryableErrorState get() = false
        open suspend fun getNextState(vm: InternalPaymentViewModel, configuration: Configuration) = this
        open fun getNextStateAfterConsumerProfileRefAvailable(consumerProfileRef: String): ProcessState? = null
        open fun getStateForUpdatingPaymentOrder(updateInfo: Any?): ProcessState? = null

        override fun describeContents() = 0

        class InitializingConsumerSession(
            private val consumer: Consumer?,
            private val paymentOrder: PaymentOrder?,
            private val userData: Any?
        ) : ProcessState() {
            companion object { @Suppress("unused") @JvmField val CREATOR =
                makeCreator(::InitializingConsumerSession)
            }

            override val uiState get() = UIState.Loading

            override val shouldProceedAutomatically get() = true
            override suspend fun getNextState(vm: InternalPaymentViewModel, configuration: Configuration): ProcessState {
                return try {
                    val viewConsumerIdentificationInfo = configuration
                        .postConsumers(vm.getApplication(), consumer, userData)
                    IdentifyingConsumer(
                        viewConsumerIdentificationInfo.webViewBaseUrl,
                        viewConsumerIdentificationInfo.viewConsumerIdentification,
                        paymentOrder,
                        userData
                    )
                } catch (e: Exception) {
                    val retryable = configuration.shouldRetryAfterPostConsumersException(e)
                    FailedInitializeConsumerSession(consumer, paymentOrder, userData, retryable, e)
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
            private val retryable: Boolean,
            private val exception: Exception
        ) : ProcessState() {
            companion object { @Suppress("unused") @JvmField val CREATOR =
                makeCreator(::FailedInitializeConsumerSession)
            }

            override val uiState
                get() = if (retryable) {
                    UIState.RetryableError(exception, R.string.swedbankpaysdk_failed_init_consumer)
                } else {
                    UIState.InitializationError(exception)
                }

            override val isRetryableErrorState get() = true
            override suspend fun getNextState(vm: InternalPaymentViewModel, configuration: Configuration): ProcessState {
                return InitializingConsumerSession(consumer, paymentOrder, userData)
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(consumer, flags)
                parcel.writeParcelable(paymentOrder, flags)
                parcel.writeUserData(userData, flags)
                parcel.writeBooleanCompat(retryable)
                parcel.writeSerializable(exception)
            }
            constructor(parcel: Parcel) : this(
                consumer = parcel.readParcelable(),
                paymentOrder = parcel.readParcelable(),
                userData = parcel.readUserData(),
                retryable = parcel.readBooleanCompat(),
                exception = parcel.readSerializable() as Exception
            )
        }

        class IdentifyingConsumer(
            private val baseUrl: String?,
            private val viewConsumerIdentification: String,
            private val paymentOrder: PaymentOrder?,
            private val userData: Any?
        ) : ProcessState() {
            companion object { @Suppress("unused") @JvmField val CREATOR =
                makeCreator(::IdentifyingConsumer)
            }

            override val uiState get() = UIState.ViewConsumerIdentification(
                baseUrl,
                viewConsumerIdentification
            )

            override fun getNextStateAfterConsumerProfileRefAvailable(consumerProfileRef: String): ProcessState {
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
            companion object { @Suppress("unused") @JvmField val CREATOR =
                makeCreator(::CreatingPaymentOrder)
            }

            override val uiState get() = UIState.Loading

            override val shouldProceedAutomatically get() = true
            override suspend fun getNextState(vm: InternalPaymentViewModel, configuration: Configuration): ProcessState {
                return try {
                    val viewPaymentOrderInfo = configuration
                        .postPaymentorders(vm.getApplication(), paymentOrder, userData, consumerProfileRef)
                    Paying(paymentOrder, userData, viewPaymentOrderInfo, null)
                } catch (e: Exception) {
                    val retryable = configuration.shouldRetryAfterPostPaymentordersException(e)
                    FailedCreatePaymentOrder(consumerProfileRef, paymentOrder, userData, retryable, e)
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
            private val retryable: Boolean,
            private val exception: Exception
        ) : ProcessState() {
            companion object { @Suppress("unused") @JvmField val CREATOR =
                makeCreator(::FailedCreatePaymentOrder)
            }

            override val uiState
                get() = if (retryable) {
                    UIState.RetryableError(exception, R.string.swedbankpaysdk_failed_create_payment)
                } else {
                    UIState.InitializationError(exception)
                }

            override val isRetryableErrorState get() = true
            override suspend fun getNextState(vm: InternalPaymentViewModel, configuration: Configuration): ProcessState {
                return CreatingPaymentOrder(consumerProfileRef, paymentOrder, userData)
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(consumerProfileRef)
                parcel.writeParcelable(paymentOrder, flags)
                parcel.writeUserData(userData, flags)
                parcel.writeBooleanCompat(retryable)
                parcel.writeSerializable(exception)
            }
            constructor(parcel: Parcel) : this(
                consumerProfileRef = parcel.readString(),
                paymentOrder = parcel.readParcelable(),
                userData = parcel.readUserData(),
                retryable = parcel.readBooleanCompat(),
                exception = parcel.readSerializable() as Exception
            )
        }

        class Paying(
            private val paymentOrder: PaymentOrder?,
            private val userData: Any?,
            val viewPaymentOrderInfo: ViewPaymentOrderInfo,
            val updateException: Exception?
        ) : ProcessState() {
            companion object { @Suppress("unused") @JvmField val CREATOR =
                makeCreator(::Paying)
            }

            override val uiState
                get() = UIState.ViewPaymentOrder(viewPaymentOrderInfo, updateException)

            override fun getStateForUpdatingPaymentOrder(updateInfo: Any?): ProcessState {
                return UpdatingPaymentOrder(paymentOrder, userData, viewPaymentOrderInfo, updateInfo)
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(paymentOrder, flags)
                parcel.writeUserData(userData, flags)
                parcel.writeParcelable(viewPaymentOrderInfo, flags)
                parcel.writeSerializable(updateException)
            }
            constructor(parcel: Parcel) : this(
                paymentOrder = parcel.readParcelable(),
                userData = parcel.readUserData(),
                viewPaymentOrderInfo = checkNotNull(parcel.readParcelable()),
                updateException = parcel.readSerializable() as? Exception
            )
        }

        class UpdatingPaymentOrder(
            private val paymentOrder: PaymentOrder?,
            private val userData: Any?,
            private val viewPaymentOrderInfo: ViewPaymentOrderInfo,
            private val updateInfo: Any?
        ) : ProcessState() {
            companion object { @Suppress("unused") @JvmField val CREATOR =
                makeCreator(::UpdatingPaymentOrder)
            }

            override val uiState get() = UIState.UpdatingPaymentOrder

            override val shouldProceedAutomatically get() = true
            override suspend fun getNextState(vm: InternalPaymentViewModel, configuration: Configuration): ProcessState {
                var exception: Exception? = null
                val viewPaymentOrderInfo = try {
                    delay(1000)
                    configuration.updatePaymentOrder(
                        vm.getApplication(), paymentOrder, userData, viewPaymentOrderInfo, updateInfo
                    )
                } catch (e: Exception) {
                    exception = e
                    this.viewPaymentOrderInfo
                }
                return Paying(paymentOrder, userData, viewPaymentOrderInfo, exception)
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(paymentOrder, flags)
                parcel.writeUserData(userData, flags)
                parcel.writeParcelable(viewPaymentOrderInfo, flags)
                parcel.writeUserData(updateInfo, flags)
            }
            constructor(parcel: Parcel) : this(
                paymentOrder = parcel.readParcelable(),
                userData = parcel.readUserData(),
                viewPaymentOrderInfo = checkNotNull(parcel.readParcelable()),
                updateInfo = parcel.readUserData()
            )
        }

        object Complete : ProcessState() {
            override val uiState get() = UIState.Complete

            override fun writeToParcel(parcel: Parcel, flags: Int) {}
            @Suppress("unused") @JvmField val CREATOR = makeCreator { Complete }
        }

        object Canceled : ProcessState() {
            override val uiState get() = UIState.Canceled

            override fun writeToParcel(parcel: Parcel, flags: Int) {}
            @Suppress("unused") @JvmField val CREATOR = makeCreator { Canceled }
        }

        class SwedbankPayError(
            private val terminalFailure: TerminalFailure?
        ) : ProcessState() {
            companion object { @Suppress("unused") @JvmField val CREATOR =
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
