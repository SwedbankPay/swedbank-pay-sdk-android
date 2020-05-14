package com.swedbankpay.mobilesdk.test

import com.swedbankpay.mobilesdk.Consumer
import com.swedbankpay.mobilesdk.PaymentOrder
import com.swedbankpay.mobilesdk.PaymentOrderUrls
import java.util.*

internal object TestConstants {
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
            hostUrls = listOf("https://hosturl.invalid/"),
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
}