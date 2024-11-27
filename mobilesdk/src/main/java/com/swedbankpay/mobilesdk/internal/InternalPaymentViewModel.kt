package com.swedbankpay.mobilesdk.internal

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.Consumer
import com.swedbankpay.mobilesdk.PaymentFragment
import com.swedbankpay.mobilesdk.PaymentOrder
import com.swedbankpay.mobilesdk.PaymentViewModel
import com.swedbankpay.mobilesdk.R
import com.swedbankpay.mobilesdk.TerminalFailure
import com.swedbankpay.mobilesdk.ViewPaymentOrderInfo
import com.swedbankpay.mobilesdk.toStyleJs
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@MainThread
internal class InternalPaymentViewModel(app: Application) : AndroidViewModel(app) {
    private companion object {
        const val KEY_STATE = "s"
    }

    private val callbackUrlObserver = Observer<Unit> {
        checkCallbacks()
    }

    private val stateBridgeObserver = Observer<UIState?>
    {
        PaymentFragmentStateBridge.paymentProcessState.value = it
    }

    private val processState = MutableLiveData<ProcessState?>()
    private val moveToNextStateJob = MutableLiveData<Job?>()

    val webViewShowingRootPage = MutableLiveData(false)

    var reloadRequested = false

    val enabledDefaultUI = MutableLiveData<@PaymentFragment.DefaultUI Int>().apply { value = 0 }

    val uiState = processState.map { it?.uiState }

    val loading = uiState.map { it == UIState.Loading }

    val updatingPaymentOrder = uiState.map { it is UIState.UpdatingPaymentOrder }

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

    val messageTitle = message.map { it?.title }

    val messageBody = message.map { it?.body }

    val retryActionAvailable = uiState.map { it is UIState.RetryableError }

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
        uiState.removeObserver(stateBridgeObserver)
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
            } catch (_: CancellationException) {
            }
        }
    }

    fun start(
        useCheckin: Boolean,
        consumer: Consumer?,
        paymentOrder: PaymentOrder?,
        userData: Any?,
        style: Bundle?,
        useBrowser: Boolean,
        checkoutV3: Boolean = false
    ) {
        useExternalBrowser = useBrowser
        if (processState.value == null) {
            val state = if (useCheckin) {
                // Check-in requires V2
                ProcessState.InitializingConsumerSession(consumer, paymentOrder, userData, style)
            } else {
                // otherwise we use v3 if desired
                if (checkoutV3 && paymentOrder != null) {
                    paymentOrder.isV3 = true
                }
                ProcessState.CreatingPaymentOrder(null, paymentOrder, userData, style)
            }
            setProcessState(state)
        }

        // To be able to observe ui state from payment fragment in payment sessions
        uiState.observeForever(stateBridgeObserver)
    }

    fun saveState(bundle: Bundle) {
        processState.value?.let { bundle.putParcelable(KEY_STATE, it) }
    }

    fun resumeFromSavedState(bundle: Bundle) {
        setProcessState(bundle.getParcelableInternal(KEY_STATE, ProcessState::class.java))
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
        setFailureState(FailureReason.SwedbankPayError(terminalFailure))
    }

    fun onPaid(message: String) {
        //setFailureState(FailureReason.SwedbankPayError(terminalFailure))
        Log.d(LOG_TAG, "onPaid: $message")
        setProcessState(ProcessState.Complete)
    }

    fun onGeneralEvent(message: String) {
        Log.d(LOG_TAG, "onGeneralEvent: $message")
        publicVm?.run {
            javaScriptEventListener?.javaScriptEvent(this, message)
        }
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

    fun onRedirectError(errorCode: Int, description: String?, failingUrl: String) {
        setFailureState(
            FailureReason.RedirectError(
                Uri.parse(failingUrl), errorCode, description
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onRedirectError(request: WebResourceRequest, error: WebResourceError) {
        setFailureState(
            FailureReason.RedirectError(
                request.url, error.errorCode, error.description?.toString()
            )
        )
    }

    fun onRedirectHttpError(request: WebResourceRequest, errorResponse: WebResourceResponse) {
        setFailureState(
            FailureReason.RedirectHttpError(
                request.url, errorResponse.statusCode, errorResponse.reasonPhrase
            )
        )
    }

    private fun setFailureState(reason: FailureReason) {
        setProcessState(ProcessState.Failed(reason))
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
            private val viewConsumerIdentification: String,
            private val style: Bundle?
        ) : UIState(), HtmlContent {
            override val asHtmlContent get() = this
            override fun getWebViewPage(context: Context): String {
                return context.getString(
                    R.string.swedbankpaysdk_view_consumer_identification_template,
                    viewConsumerIdentification,
                    style?.toStyleJs()
                )
            }
        }

        class ViewPaymentOrder(
            val viewPaymentOrderInfo: ViewPaymentOrderInfo,
            private val style: Bundle?,
            val updateException: Exception?
        ) : UIState(), HtmlContent {
            override val asHtmlContent get() = this
            override val baseUrl get() = viewPaymentOrderInfo.webViewBaseUrl

            override fun getWebViewPage(context: Context): String {

                if (viewPaymentOrderInfo.isV3) {
                    return context.getString(
                        R.string.swedbankpaysdk_view_checkout_template,
                        viewPaymentOrderInfo.viewPaymentLink,
                        style?.toStyleJs()
                    )
                }
                return context.getString(
                    R.string.swedbankpaysdk_view_paymentorder_template,
                    viewPaymentOrderInfo.viewPaymentLink,
                    style?.toStyleJs()
                )

            }
        }

        object UpdatingPaymentOrder : UIState()
        class InitializationError(val exception: Exception) : UIState()
        class RetryableError(val exception: Exception, @StringRes val message: Int) : UIState()
        object Complete : UIState()
        object Canceled : UIState()
        class Failure(val failureReason: FailureReason) : UIState()
    }

    sealed class FailureReason : Parcelable {
        @Parcelize
        class SwedbankPayError(val terminalFailure: TerminalFailure?) : FailureReason()

        @Parcelize
        class RedirectError(
            val uri: Uri,
            val errorCode: Int,
            val description: String?
        ) : FailureReason()

        @Parcelize
        class RedirectHttpError(
            val uri: Uri,
            val statusCode: Int,
            val reasonPhrase: String
        ) : FailureReason()
    }

    private sealed class ProcessState : Parcelable {
        abstract val uiState: UIState

        open val shouldProceedAutomatically get() = false
        open val isRetryableErrorState get() = false
        open suspend fun getNextState(vm: InternalPaymentViewModel, configuration: Configuration) =
            this

        open fun getNextStateAfterConsumerProfileRefAvailable(consumerProfileRef: String): ProcessState? =
            null

        open fun getStateForUpdatingPaymentOrder(updateInfo: Any?): ProcessState? = null

        @Parcelize
        class InitializingConsumerSession(
            private val consumer: Consumer?,
            private val paymentOrder: PaymentOrder?,
            private val userData: @RawValue Any?,
            private val style: Bundle?
        ) : ProcessState() {
            override val uiState get() = UIState.Loading

            override val shouldProceedAutomatically get() = true
            override suspend fun getNextState(
                vm: InternalPaymentViewModel,
                configuration: Configuration
            ): ProcessState {
                return try {
                    val viewConsumerIdentificationInfo = configuration
                        .postConsumers(vm.getApplication(), consumer, userData)
                    IdentifyingConsumer(
                        viewConsumerIdentificationInfo.webViewBaseUrl,
                        viewConsumerIdentificationInfo.viewConsumerIdentification,
                        paymentOrder,
                        userData,
                        style
                    )
                } catch (e: Exception) {
                    val retryable = configuration.shouldRetryAfterPostConsumersException(e)
                    FailedInitializeConsumerSession(
                        consumer, paymentOrder, userData, style, retryable, e
                    )
                }
            }
        }

        @Parcelize
        class FailedInitializeConsumerSession(
            private val consumer: Consumer?,
            private val paymentOrder: PaymentOrder?,
            private val userData: @RawValue Any?,
            private val style: Bundle?,
            private val retryable: Boolean,
            private val exception: Exception
        ) : ProcessState() {
            override val uiState
                get() = if (retryable) {
                    UIState.RetryableError(exception, R.string.swedbankpaysdk_failed_init_consumer)
                } else {
                    UIState.InitializationError(exception)
                }

            override val isRetryableErrorState get() = true
            override suspend fun getNextState(
                vm: InternalPaymentViewModel,
                configuration: Configuration
            ): ProcessState {
                return InitializingConsumerSession(consumer, paymentOrder, userData, style)
            }
        }

        @Parcelize
        class IdentifyingConsumer(
            private val baseUrl: String?,
            private val viewConsumerIdentification: String,
            private val paymentOrder: PaymentOrder?,
            private val userData: @RawValue Any?,
            private val style: Bundle?
        ) : ProcessState() {
            override val uiState
                get() = UIState.ViewConsumerIdentification(
                    baseUrl,
                    viewConsumerIdentification,
                    style
                )

            override fun getNextStateAfterConsumerProfileRefAvailable(consumerProfileRef: String): ProcessState {
                return CreatingPaymentOrder(consumerProfileRef, paymentOrder, userData, style)
            }
        }

        @Parcelize
        class CreatingPaymentOrder(
            private val consumerProfileRef: String?,
            private val paymentOrder: PaymentOrder?,
            private val userData: @RawValue Any?,
            private val style: Bundle?
        ) : ProcessState() {
            override val uiState get() = UIState.Loading

            override val shouldProceedAutomatically get() = true
            override suspend fun getNextState(
                vm: InternalPaymentViewModel,
                configuration: Configuration
            ): ProcessState {
                return try {
                    val viewPaymentOrderInfo = configuration.postPaymentorders(
                        vm.getApplication(),
                        paymentOrder,
                        userData,
                        consumerProfileRef
                    )
                    Paying(paymentOrder, userData, style, viewPaymentOrderInfo, null)
                } catch (e: Exception) {
                    val retryable = configuration.shouldRetryAfterPostPaymentordersException(e)
                    FailedCreatePaymentOrder(
                        consumerProfileRef, paymentOrder, userData, style, retryable, e
                    )
                }
            }
        }

        @Parcelize
        class FailedCreatePaymentOrder(
            private val consumerProfileRef: String?,
            private val paymentOrder: PaymentOrder?,
            private val userData: @RawValue Any?,
            private val style: Bundle?,
            private val retryable: Boolean,
            private val exception: Exception
        ) : ProcessState() {
            override val uiState
                get() = if (retryable) {
                    UIState.RetryableError(exception, R.string.swedbankpaysdk_failed_create_payment)
                } else {
                    UIState.InitializationError(exception)
                }

            override val isRetryableErrorState get() = true
            override suspend fun getNextState(
                vm: InternalPaymentViewModel,
                configuration: Configuration
            ): ProcessState {
                return CreatingPaymentOrder(consumerProfileRef, paymentOrder, userData, style)
            }
        }

        @Parcelize
        class Paying(
            private val paymentOrder: PaymentOrder?,
            private val userData: @RawValue Any?,
            private val style: Bundle?,
            val viewPaymentOrderInfo: ViewPaymentOrderInfo,
            val updateException: Exception?
        ) : ProcessState() {
            override val uiState
                get() = UIState.ViewPaymentOrder(viewPaymentOrderInfo, style, updateException)

            override fun getStateForUpdatingPaymentOrder(updateInfo: Any?): ProcessState {
                return UpdatingPaymentOrder(
                    paymentOrder, userData, style, viewPaymentOrderInfo, updateInfo
                )
            }
        }

        @Parcelize
        class UpdatingPaymentOrder(
            private val paymentOrder: PaymentOrder?,
            private val userData: @RawValue Any?,
            private val style: Bundle?,
            private val viewPaymentOrderInfo: ViewPaymentOrderInfo,
            private val updateInfo: @RawValue Any?
        ) : ProcessState() {
            override val uiState get() = UIState.UpdatingPaymentOrder

            override val shouldProceedAutomatically get() = true
            override suspend fun getNextState(
                vm: InternalPaymentViewModel,
                configuration: Configuration
            ): ProcessState {
                var exception: Exception? = null
                val viewPaymentOrderInfo = try {
                    delay(1000)
                    configuration.updatePaymentOrder(
                        vm.getApplication(),
                        paymentOrder,
                        userData,
                        viewPaymentOrderInfo,
                        updateInfo
                    )
                } catch (e: Exception) {
                    exception = e
                    this.viewPaymentOrderInfo
                }
                return Paying(paymentOrder, userData, style, viewPaymentOrderInfo, exception)
            }
        }

        @Parcelize
        object Complete : ProcessState() {
            override val uiState get() = UIState.Complete
        }

        @Parcelize
        object Canceled : ProcessState() {
            override val uiState get() = UIState.Canceled
        }

        @Parcelize
        class Failed(
            private val failureReason: FailureReason
        ) : ProcessState() {
            override val uiState get() = UIState.Failure(failureReason)
        }
    }
}
