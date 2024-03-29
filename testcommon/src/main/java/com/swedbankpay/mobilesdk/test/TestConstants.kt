package com.swedbankpay.mobilesdk.test

import com.swedbankpay.mobilesdk.*
import java.util.*

object TestConstants {
    const val hostUrl = "https://hosturl.invalid/"
    const val completeUrl = "https://completeurl.invalid/"
    const val termsOfServiceUrl = "https://tosurl.invalid/"
    const val paymentUrl = "https://paymenturl.invalid/"
    val consumer = Consumer(shippingAddressRestrictedToCountryCodes = listOf("CC"))
    val paymentOrder = PaymentOrder(
        currency = Currency.getInstance("NOK"),
        amount = 1L,
        vatAmount = 0L,
        description = "",
        urls = PaymentOrderUrls(
            hostUrls = listOf(hostUrl),
            completeUrl = completeUrl,
            paymentUrl = paymentUrl,
            termsOfServiceUrl = termsOfServiceUrl
        )
    )
    const val consumerProfileRef = "profile"
    const val viewConsumerSessionLink = "consumerLink"
    const val consumerSessionHtmlPage = "consumerHtml"
    const val consumerSessionErrorOrigin = "consumer"
    const val consumerSessionErrorMessageId = "consumerErrId"
    const val consumerSessionErrorDetails = "consumerErrDetails"
    const val consumerSessionError =
        """{"origin":"$consumerSessionErrorOrigin","messageId":"$consumerSessionErrorMessageId","details":"$consumerSessionErrorDetails"}"""
    const val viewPaymentorderLink = "paymentLink"
    const val paymentorderHtmlPage = "paymentHtml"

    const val paymentOrderErrorOrigin = "paymentmenu"
    const val paymentOrderErrorMessageId = "paymentErrId"
    const val paymentOrderErrorDetails = "paymentErrDetails"
    const val paymentOrderError =
        """{"origin":"$paymentOrderErrorOrigin","messageId":"$paymentOrderErrorMessageId","details":"$paymentOrderErrorDetails"}"""
    const val consumerRetryableErrorMessage = "retryConsumer"

    val viewConsumerIdentificationInfo = ViewConsumerIdentificationInfo(
        webViewBaseUrl = hostUrl,
        viewConsumerIdentification = viewConsumerSessionLink
    )

    val viewPaymentorderInfo = ViewPaymentOrderInfo(
        webViewBaseUrl = hostUrl,
        viewPaymentLink = viewPaymentorderLink,
        isV3 = paymentOrder.isV3,
        completeUrl = completeUrl,
        cancelUrl = null,
        paymentUrl = paymentUrl,
        termsOfServiceUrl = termsOfServiceUrl
    )
}