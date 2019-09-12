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
import com.payex.mobilesdk.Configuration
import com.payex.mobilesdk.R
import com.payex.mobilesdk.TerminalFailure
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

    private val uiState = Transformations.map(processState) { it?.uiState }
    val currentPage = Transformations.map(uiState) { it?.getWebViewPage(this) }

    val javascriptInterface = JSInterface(this)

    private var configuration: Configuration? = null

    fun setConfiguration(configuration: Configuration) {
        this.configuration = configuration
    }

    override fun onCleared() {
        super.onCleared()
        javascriptInterface.vm = null
        configuration = null
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

    private sealed class UIState {
        open fun getWebViewPage(vm: AndroidViewModel): String? = null

        object Loading : UIState()
        class HtmlContent(@StringRes val template: Int, val link: String) : UIState() {
            override fun getWebViewPage(vm: AndroidViewModel): String? {
                return vm.getApplication<Application>().getString(template, link)
            }
        }
        class RetryableError(@StringRes val message: Int) : UIState()
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

        class InitializingConsumerSession(
            private val consumerIdentificationData: String,
            private val merchantData: String?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR = makeCreator(::InitializingConsumerSession) }

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
                } catch (e: Exception) {
                    e.printStackTrace()
                    FailedInitializeConsumerSession(consumerIdentificationData, merchantData)
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
            private val merchantData: String?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR = makeCreator(::FailedInitializeConsumerSession) }

            override val uiState get() = UIState.RetryableError(R.string.unknown_error)

            override val isRetryableErrorState get() = true
            override suspend fun getNextState(context: Context, configuration: Configuration): ProcessState {
                return InitializingConsumerSession(consumerIdentificationData, merchantData)
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

        class IdentifyingConsumer(
            private val viewConsumerIdentification: String,
            private val merchantData: String?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR = makeCreator(::IdentifyingConsumer) }

            override val uiState get() = UIState.HtmlContent(R.string.view_consumer_identification_template, viewConsumerIdentification)

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
            companion object { @JvmField val CREATOR = makeCreator(::CreatingPaymentOrder) }

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
                    Paying(viewPaymentOrder/*, paymentOrder.url*/)
                } catch (e: Exception) {
                    e.printStackTrace()
                    FailedCreatePaymentOrder(consumerProfileRef, merchantData)
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
            private val merchantData: String?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR = makeCreator(::FailedCreatePaymentOrder) }

            override val uiState get() = UIState.RetryableError(R.string.unknown_error)

            override val isRetryableErrorState get() = true
            override suspend fun getNextState(context: Context, configuration: Configuration): ProcessState {
                return CreatingPaymentOrder(consumerProfileRef, merchantData)
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

        class Paying(
            private val viewPaymentOrder: String
            //private val paymentOrderUrl: Link.PaymentOrder
        ) : ProcessState() {
            companion object { @JvmField val CREATOR = makeCreator(::Paying) }

            override val uiState get() = UIState.HtmlContent(R.string.view_paymentorder_template, viewPaymentOrder)

            override fun getNextStateAfterPaymentFailed() = PaymentFailed(/*paymentOrderUrl*/)

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(viewPaymentOrder)
                //parcel.writeLink(paymentOrderUrl)
            }
            constructor(parcel: Parcel) : this(
                checkNotNull(parcel.readString())
                //checkNotNull(parcel.readLink(Link::PaymentOrder))
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
            //private val paymentOrderUrl: Link.PaymentOrder
        ) : ProcessState() {
            companion object { @JvmField val CREATOR = makeCreator(::PaymentFailed) }

            override val uiState get() = UIState.Failure(null)

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                //parcel.writeLink(paymentOrderUrl)
            }
            constructor(parcel: Parcel) : this(
                //checkNotNull(parcel.readLink(Link::PaymentOrder))
            )
        }

        class PayExError(
            private val terminalFailure: TerminalFailure?
        ) : ProcessState() {
            companion object { @JvmField val CREATOR = makeCreator(::PayExError) }

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