package com.swedbankpay.mobilesdk.test

object TestConstants {
    const val consumerIdentificationData = "idData"
    const val consumerProfileRef = "profile"
    const val merchantData = "merchantData"
    const val viewConsumerSessionLink = "consumerLink"
    const val consumerSessionHtmlPage = "consumerHtml"
    const val consumerSessionErrorOrigin = "consumer"
    const val consumerSessionErrorMessageId = "consumerErrId"
    const val consumerSessionErrorDetails = "consumerErrDetails"
    const val consumerSessionError =
        """{"origin":"$consumerSessionErrorOrigin","messageId":"$consumerSessionErrorMessageId","details":"$consumerSessionErrorDetails"}"""
    const val paymentOrderUrl = "https://paymenturl.invalid/"
    const val viewPaymentorderLink = "paymentLink"
    const val paymentorderHtmlPage = "paymentHtml"
    const val paymentToSUrl = "https://tosurl.invalid/"
    const val onPaymentTosEvent = """{"openUrl":"$paymentToSUrl"}"""
    const val paymentOrderErrorOrigin = "paymentmenu"
    const val paymentOrderErrorMessageId = "paymentErrId"
    const val paymentOrderErrorDetails = "paymentErrDetails"
    const val paymentOrderError =
        """{"origin":"$paymentOrderErrorOrigin","messageId":"$paymentOrderErrorMessageId","details":"$paymentOrderErrorDetails"}"""
}