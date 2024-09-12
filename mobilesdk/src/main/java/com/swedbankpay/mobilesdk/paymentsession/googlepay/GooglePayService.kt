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
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.ExpectationModel
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.getBooleanValueFor
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.getStringArrayValueFor
import com.swedbankpay.mobilesdk.paymentsession.api.model.response.getStringValueFor
import com.swedbankpay.mobilesdk.paymentsession.exposedmodel.PaymentSessionProblem
import org.json.JSONArray
import org.json.JSONObject
import se.vettefors.googlepaytest.model.GooglePayResult

internal object GooglePayService {

    fun launchGooglePay(
        expects: List<ExpectationModel>,
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

        val allowedCardAuthMethods = JSONArray(
            expects.getStringArrayValueFor(GooglePayExpectedValues.ALLOWED_CARD_AUTH_METHODS)
        )

        val allowedCardNetworks = JSONArray(
            expects.getStringArrayValueFor(GooglePayExpectedValues.ALLOWED_CARD_NETWORKS)
        )

        val gatewayTokenizationSpecification = JSONObject()
            .put("type", GooglePayExpectedValues.TYPE_PAYMENT_GATEWAY)
            .put(
                "parameters", JSONObject()
                    .put("gateway", expects.getStringValueFor(GooglePayExpectedValues.GATEWAY))
                    .put(
                        "gatewayMerchantId",
                        expects.getStringValueFor(GooglePayExpectedValues.GATEWAY_MERCHANT_ID)
                    )
            )

        val baseCardPaymentMethod = JSONObject()
            .put("type", GooglePayExpectedValues.TYPE_CARD)
            .put(
                "parameters", JSONObject()
                    .put("allowedAuthMethods", allowedCardAuthMethods)
                    .put("allowedCardNetworks", allowedCardNetworks)
                    .put(
                        "billingAddressRequired",
                        expects.getBooleanValueFor(GooglePayExpectedValues.BILLING_ADDRESS_REQUIRED)
                    )
//                    .put(
//                        "billingAddressParameters", JSONObject()
//                            .put("format", "FULL")
//                    )
            )
            .put("tokenizationSpecification", gatewayTokenizationSpecification)

        val transactionInfo = JSONObject()
            .put("totalPrice", expects.getStringValueFor(GooglePayExpectedValues.TOTAL_PRICE))
            .put(
                "totalPriceStatus",
                expects.getStringValueFor(GooglePayExpectedValues.TOTAL_PRICE_STATUS)
            )
            .put(
                "totalPriceLabel",
                expects.getStringValueFor(GooglePayExpectedValues.TOTAL_PRICE_LABEL)
            )
            .put(
                "countryCode",
                expects.getStringValueFor(GooglePayExpectedValues.COUNTRY_CODE)
            )
            .put(
                "currencyCode",
                expects.getStringValueFor(GooglePayExpectedValues.CURRENCY_CODE)
            )
            .put(
                "transactionId",
                expects.getStringValueFor(GooglePayExpectedValues.TRANSACTION_ID)
            )

        val merchantInfo = JSONObject()
            .put(
                "merchantName",
                expects.getStringValueFor(GooglePayExpectedValues.MERCHANT_NAME)
            )
            .put("merchantId", expects.getStringValueFor(GooglePayExpectedValues.MERCHANT_ID))

        val baseRequest = JSONObject()
            .put("apiVersion", 2)
            .put("apiVersionMinor", 0)
            .put("transactionInfo", transactionInfo)
            .put("merchantInfo", merchantInfo)
            .put(
                "shippingAddressRequired",
                expects.getBooleanValueFor(GooglePayExpectedValues.SHIPPING_ADDRESS_REQUIRED)
            )
//            .put(
//                "shippingAddressParameters", JSONObject()
//                    .put("phoneNumberRequired", false)
//                    .put(
//                        "allowedCountryCodes", JSONArray(
//                            listOf(
//                                "SE"
//                            )
//                        )
//                    )
//            )
            .put(
                "allowedPaymentMethods", JSONArray(
                    listOf(
                        baseCardPaymentMethod
                    )
                )
            )

        val environment = when (
            expects.getStringValueFor(GooglePayExpectedValues.ENVIRONMENT)
        ) {
            "TEST" -> WalletConstants.ENVIRONMENT_TEST
            "PRODUCTION" -> WalletConstants.ENVIRONMENT_PRODUCTION
            else -> WalletConstants.ENVIRONMENT_TEST
        }

        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(environment)
            .build()
        val paymentsClient = Wallet.getPaymentsClient(activity, walletOptions)

        val request = PaymentDataRequest.fromJson(baseRequest.toString())
        val googlePayTask = paymentsClient.loadPaymentData(request)

        googlePayTask.addOnCompleteListener(googlePayLauncher::launch)
    }

}