package com.swedbankpay.mobilesdk.paymentsession.googlepay

import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.google.android.gms.wallet.contract.TaskResultContracts
import com.google.gson.Gson
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.IntegrationTask
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentSessionProblem
import org.json.JSONArray
import org.json.JSONObject
import se.vettefors.googlepaytest.model.GooglePayResult

internal object GooglePayService {

    fun launchGooglePay(
        task: IntegrationTask,
        activity: Activity,
        errorHandler: (PaymentSessionProblem) -> Unit,
        onGooglePayResult: (GooglePayResult) -> Unit
    ) {
        val activityResultRegistry = when (activity) {
            is AppCompatActivity -> activity.activityResultRegistry
            is ComponentActivity -> activity.activityResultRegistry
            else -> throw IllegalArgumentException("Activity must be of type AppCompatActivity or ComponentActivity")
        }

        val googlePayLauncher = activityResultRegistry.register(
            "GooglePay",
            TaskResultContracts.GetPaymentDataResult()
        ) { taskResult ->
            when (taskResult.status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    taskResult.result?.let {
                        Log.d("google", "${it.toJson()} ")
                        val result = Gson().fromJson(it.toJson(), GooglePayResult::class.java)
                        onGooglePayResult(result)
                    }
                }
                //CommonStatusCodes.CANCELED -> The user canceled
                //AutoResolveHelper.RESULT_ERROR -> The API returned an error (it.status: Status)
                //CommonStatusCodes.INTERNAL_ERROR -> Handle other unexpected errors
            }
        }

        val allowedCardAuthMethodsExpectationsModel =
            task.getExpectValuesFor(GooglePayExpectedValues.ALLOWED_CARD_AUTH_METHODS)

        val allowedCardNetworksExpectationsModel =
            task.getExpectValuesFor(GooglePayExpectedValues.ALLOWED_CARD_NETWORKS)

        if (allowedCardAuthMethodsExpectationsModel?.type != "string[]"
            || allowedCardNetworksExpectationsModel?.type != "string[]"
        ) {
            errorHandler.invoke(PaymentSessionProblem.InternalInconsistencyError)
        }

        @Suppress("UNCHECKED_CAST")
        val allowedCardAuthMethods = JSONArray(
            allowedCardAuthMethodsExpectationsModel?.value as List<String>
        )

        @Suppress("UNCHECKED_CAST")
        val allowedCardNetworks = JSONArray(
            allowedCardNetworksExpectationsModel?.value as List<String>
        )


        val gatewayTokenizationSpecification = JSONObject()
            .put("type", "PAYMENT_GATEWAY")
            .put(
                "parameters", JSONObject()
                    .put("gateway", task.getExpectValuesFor(GooglePayExpectedValues.GATEWAY)?.value)
                    .put(
                        "gatewayMerchantId",
                        task.getExpectValuesFor(GooglePayExpectedValues.GATEWAY_MERCHANT_ID)?.value
                    )
            )

        val baseCardPaymentMethod = JSONObject()
            .put("type", "CARD")
            .put(
                "parameters", JSONObject()
                    .put("allowedAuthMethods", allowedCardAuthMethods)
                    .put("allowedCardNetworks", allowedCardNetworks)
                    .put(
                        "billingAddressRequired",
                        task.getExpectValuesFor(GooglePayExpectedValues.BILLING_ADDRESS_REQUIRED)?.value
                    )
                    .put(
                        "billingAddressParameters", JSONObject()
                            .put("format", "FULL")
                    )
            )
            .put("tokenizationSpecification", gatewayTokenizationSpecification)

        val transactionInfo = JSONObject()
            .put("totalPrice", task.getExpectValuesFor(GooglePayExpectedValues.TOTAL_PRICE)?.value)
            .put(
                "totalPriceStatus",
                task.getExpectValuesFor(GooglePayExpectedValues.TOTAL_PRICE_STATUS)?.value
            )
            .put(
                "totalPriceLabel",
                task.getExpectValuesFor(GooglePayExpectedValues.TOTAL_PRICE_LABEL)?.value
            )
            .put(
                "countryCode",
                task.getExpectValuesFor(GooglePayExpectedValues.COUNTRY_CODE)?.value
            )
            .put(
                "currencyCode",
                task.getExpectValuesFor(GooglePayExpectedValues.CURRENCY_CODE)?.value
            )
            .put(
                "transactionId",
                task.getExpectValuesFor(GooglePayExpectedValues.TRANSACTION_ID)?.value
            )

        val merchantInfo = JSONObject()
            .put(
                "merchantName",
                task.getExpectValuesFor(GooglePayExpectedValues.MERCHANT_NAME)?.value
            )

        val baseRequest = JSONObject()
            .put("apiVersion", 2)
            .put("apiVersionMinor", 0)
            .put("transactionInfo", transactionInfo)
            .put("merchantInfo", merchantInfo)
            .put(
                "shippingAddressRequired",
                task.getExpectValuesFor(GooglePayExpectedValues.SHIPPING_ADDRESS_REQUIRED)?.value
            )
            .put(
                "shippingAddressParameters", JSONObject()
                    .put("phoneNumberRequired", false)
                    .put(
                        "allowedCountryCodes", JSONArray(
                            listOf(
                                "SE"
                            )
                        )
                    )
            )
            .put(
                "allowedPaymentMethods", JSONArray(
                    listOf(
                        baseCardPaymentMethod
                    )
                )
            )

        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
            .build()
        val paymentsClient = Wallet.getPaymentsClient(activity, walletOptions)

        Log.d("googlepay", "launchGooglePay: $baseRequest ")

        val request = PaymentDataRequest.fromJson(baseRequest.toString())
        val googlePayTask = paymentsClient.loadPaymentData(request)

        googlePayTask.addOnCompleteListener(googlePayLauncher::launch)
    }

}