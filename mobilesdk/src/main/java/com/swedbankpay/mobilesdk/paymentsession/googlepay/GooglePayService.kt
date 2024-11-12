package com.swedbankpay.mobilesdk.paymentsession.googlepay

import android.app.Activity
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
import com.swedbankpay.mobilesdk.paymentsession.googlepay.model.GooglePayResult
import com.swedbankpay.mobilesdk.paymentsession.googlepay.util.GooglePayConstants
import org.json.JSONArray
import org.json.JSONObject

internal data class GooglePayError(
    val statusCode: Int,
    val message: String? = null,
    val userCancelled: Boolean = false
)

internal object GooglePayService {

    fun launchGooglePay(
        expects: List<ExpectationModel>,
        activity: Activity,
        onGooglePayResult: (GooglePayResult?, GooglePayError?) -> Unit
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
                        val result = Gson().fromJson(it.toJson(), GooglePayResult::class.java)
                        onGooglePayResult(result, null)
                    }
                }

                CommonStatusCodes.CANCELED -> {
                    onGooglePayResult(
                        null,
                        GooglePayError(
                            statusCode = CommonStatusCodes.CANCELED,
                            userCancelled = true
                        )
                    )
                }

                else -> {
                    onGooglePayResult(
                        null,
                        GooglePayError(
                            statusCode = taskResult.status.statusCode,
                            message = "${CommonStatusCodes.getStatusCodeString(taskResult.status.statusCode)} ${taskResult.status.statusMessage}"
                        )
                    )
                }
            }
        }

        val allowedCardAuthMethods = JSONArray(
            expects.getStringArrayValueFor(GooglePayConstants.ALLOWED_CARD_AUTH_METHODS)
        )

        val allowedCardNetworks = JSONArray(
            expects.getStringArrayValueFor(GooglePayConstants.ALLOWED_CARD_NETWORKS)
        )

        val gatewayTokenizationSpecification = JSONObject()
            .put("type", GooglePayConstants.TYPE_PAYMENT_GATEWAY)
            .put(
                "parameters", JSONObject()
                    .put("gateway", expects.getStringValueFor(GooglePayConstants.GATEWAY))
                    .put(
                        "gatewayMerchantId",
                        expects.getStringValueFor(GooglePayConstants.GATEWAY_MERCHANT_ID)
                    )
            )

        val baseCardPaymentMethod = JSONObject()
            .put("type", GooglePayConstants.TYPE_CARD)
            .put(
                "parameters", JSONObject()
                    .put("allowedAuthMethods", allowedCardAuthMethods)
                    .put("allowedCardNetworks", allowedCardNetworks)
                    .put(
                        "billingAddressRequired",
                        expects.getBooleanValueFor(GooglePayConstants.BILLING_ADDRESS_REQUIRED)
                    )
            )
            .put("tokenizationSpecification", gatewayTokenizationSpecification)

        val transactionInfo = JSONObject()
            .put("totalPrice", expects.getStringValueFor(GooglePayConstants.TOTAL_PRICE))
            .put(
                "totalPriceStatus",
                expects.getStringValueFor(GooglePayConstants.TOTAL_PRICE_STATUS)
            )
            .put(
                "totalPriceLabel",
                expects.getStringValueFor(GooglePayConstants.TOTAL_PRICE_LABEL)
            )
            .put(
                "countryCode",
                expects.getStringValueFor(GooglePayConstants.COUNTRY_CODE)
            )
            .put(
                "currencyCode",
                expects.getStringValueFor(GooglePayConstants.CURRENCY_CODE)
            )
            .put(
                "transactionId",
                expects.getStringValueFor(GooglePayConstants.TRANSACTION_ID)
            )

        val merchantInfo = JSONObject()
            .put(
                "merchantName",
                expects.getStringValueFor(GooglePayConstants.MERCHANT_NAME)
            )
            .put("merchantId", expects.getStringValueFor(GooglePayConstants.MERCHANT_ID))

        val baseRequest = JSONObject()
            .put("apiVersion", GooglePayConstants.GOOGLE_PAY_API_VERSION)
            .put("apiVersionMinor", GooglePayConstants.GOOGLE_PAY_API_VERSION_MINOR)
            .put("transactionInfo", transactionInfo)
            .put("merchantInfo", merchantInfo)
            .put(
                "shippingAddressRequired",
                expects.getBooleanValueFor(GooglePayConstants.SHIPPING_ADDRESS_REQUIRED)
            ).put(
                "shippingAddressParameters", JSONObject()
                    .put(
                        "phoneNumberRequired",
                        expects.getBooleanValueFor(GooglePayConstants.PHONE_NUMBER_REQUIRED)
                    )
            ).put(
                "allowedPaymentMethods", JSONArray(
                    listOf(
                        baseCardPaymentMethod
                    )
                )
            )

        val environment = when (
            expects.getStringValueFor(GooglePayConstants.ENVIRONMENT)
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