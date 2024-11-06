package com.swedbankpay.mobilesdk.paymentsession.util

import androidx.annotation.Keep
import com.swedbankpay.mobilesdk.logging.model.ExtensionsModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.SwedbankPayAPIError
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ProblemDetails
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentAttemptInstrument
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentSessionProblem

/**
 * This files holds various functions for logging purposes
 */

@Keep
internal fun PaymentAttemptInstrument.toExtensionsModel(): ExtensionsModel {
    val values: MutableMap<String, String?> = mutableMapOf(
        "instrument" to this.identifier
    )

    return when (this) {
        is PaymentAttemptInstrument.CreditCard -> {
            val usedCard = this.prefill

            val creditCardValues = mapOf(
                "paymentToken" to usedCard.paymentToken,
                "cardNumber" to usedCard.maskedPan,
                "cardExpiryMonth" to usedCard.expiryMonth,
                "cardExpiryYear" to usedCard.expiryYear
            )

            values.putAll(creditCardValues)

            ExtensionsModel(
                values = values
            )
        }

        is PaymentAttemptInstrument.Swish -> {
            values["msisdn"] = msisdn
            ExtensionsModel(
                values = values
            )
        }

        is PaymentAttemptInstrument.NewCreditCard -> {
            ExtensionsModel(
                values = values
            )
        }

        is PaymentAttemptInstrument.GooglePay -> {
            ExtensionsModel(
                values = values
            )
        }

        is PaymentAttemptInstrument.WebBased -> {
            ExtensionsModel(
                values = values
            )
        }
    }
}

@Keep
internal fun ProblemDetails.toExtensionsModel() =
    ExtensionsModel(
        values = mutableMapOf(
            "problemType" to type,
            "problemTitle" to title,
            "problemStatus" to status.toString(),
            "problemDetail" to detail
        )
    )

@Keep
internal fun launchClientAppExtensionsModel(
    paymentUrl: String?,
    launchUrl: String?,
    succeeded: Boolean
) = ExtensionsModel(
    values = mutableMapOf(
        "callbackUrl" to paymentUrl,
        "clientAppLaunchUrl" to launchUrl,
        "launchSucceeded" to succeeded.toString()
    )
)

@Keep
internal fun clientAppCallbackExtensionsModel(
    callbackUrl: String
) = ExtensionsModel(
    values = mutableMapOf(
        "callbackUrl" to callbackUrl
    )
)

@Keep
internal fun scaMethodRequestExtensionModel(
    completionIndicator: String,
    errorMessage: String? = null,
    responseCode: Int? = null
) = ExtensionsModel(
    values = if (errorMessage != null) {
        mutableMapOf(
            "methodCompletionIndicator" to completionIndicator,
            "errorMessage" to errorMessage,
            "responseCode" to responseCode.toString()
        )
    } else {
        mutableMapOf(
            "methodCompletionIndicator" to completionIndicator
        )
    }
)

@Keep
internal fun scaRedirectResultExtensionModel(
    cresReceived: Boolean,
    errorMessage: String? = null,
    responseCode: Int? = null
) = ExtensionsModel(
    values = if (errorMessage != null) {
        mutableMapOf(
            "cresRecieved" to cresReceived.toString(),
            "errorMessage" to errorMessage,
            "responseCode" to responseCode.toString()
        )
    } else {
        mutableMapOf(
            "cresRecieved" to cresReceived.toString()
        )
    }
)


@Keep
internal fun PaymentSessionProblem.toExtensionsModel(): ExtensionsModel {
    val values: MutableMap<String, String?> = when (this) {
        PaymentSessionProblem.ClientAppLaunchFailed -> mutableMapOf("problem" to "clientAppLaunchFailed")
        is PaymentSessionProblem.PaymentSessionAPIRequestFailed -> {
            val requestFailedValues: MutableMap<String, String?> = when (error) {
                is SwedbankPayAPIError.Error -> mutableMapOf(
                    "problem" to "paymentSessionAPIRequestFailed",
                    "errorMessage" to error.message,
                    "responseCode" to error.responseCode.toString()
                )

                SwedbankPayAPIError.InvalidUrl -> mutableMapOf(
                    "problem" to "paymentSessionAPIRequestFailed",
                    "errorMessage" to "Invalid url",
                )

                SwedbankPayAPIError.Unknown -> mutableMapOf(
                    "problem" to "paymentSessionAPIRequestFailed",
                    "errorMessage" to "Unknown",
                )
            }
            requestFailedValues
        }

        PaymentSessionProblem.PaymentSessionEndReached -> mutableMapOf("problem" to "paymentSessionEndReached")
        PaymentSessionProblem.InternalInconsistencyError -> mutableMapOf("problem" to "internalInconsistencyError")
        PaymentSessionProblem.AutomaticConfigurationFailed -> mutableMapOf("problem" to "automaticConfigurationFailed")
        is PaymentSessionProblem.PaymentSession3DSecureFragmentLoadFailed -> mutableMapOf(
            "problem" to "paymentSession3DSecureFragmentLoadFailed",
            "errorMessage" to error.message,
            "responseCode" to error.responseCode.toString()
        )
    }

    return ExtensionsModel(
        values = values
    )
}

@Keep
internal fun SwedbankPayAPIError.toExtensionsModel(): ExtensionsModel {
    val values: MutableMap<String, String?> = mutableMapOf(
        "errorMessage" to when (this) {
            is SwedbankPayAPIError.Error -> message
            SwedbankPayAPIError.InvalidUrl -> "invalid url"
            SwedbankPayAPIError.Unknown -> "unknown"
        }
    )

    return ExtensionsModel(values = values)
}