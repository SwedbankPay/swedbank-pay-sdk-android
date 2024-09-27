package com.swedbankpay.mobilesdk.paymentsession.googlepay.util

internal object GooglePayConstants {

    // Api Version
    const val GOOGLE_PAY_API_VERSION = 2
    const val GOOGLE_PAY_API_VERSION_MINOR = 0

    // Types
    const val TYPE_PAYMENT_GATEWAY = "PAYMENT_GATEWAY"
    const val TYPE_CARD = "CARD"

    // Expected parameters
    const val ENVIRONMENT = "EnvironmentNative"
    const val GATEWAY = "Gateway"
    const val GATEWAY_MERCHANT_ID = "GatewayMerchantId"
    const val ALLOWED_CARD_NETWORKS = "AllowedCardNetworks"
    const val ALLOWED_CARD_AUTH_METHODS = "AllowedCardAuthMethods"
    const val TOTAL_PRICE = "TotalPrice"
    const val TOTAL_PRICE_STATUS = "TotalPriceStatus"
    const val COUNTRY_CODE = "CountryCode"
    const val CURRENCY_CODE = "CurrencyCode"
    const val BILLING_ADDRESS_REQUIRED = "BillingAddressRequired"
    const val SHIPPING_ADDRESS_REQUIRED = "ShippingAddressRequired"
    const val PHONE_NUMBER_REQUIRED = "PhoneNumberRequired"
    const val TOTAL_PRICE_LABEL = "TotalPriceLabel"
    const val TRANSACTION_ID = "TransactionId"
    const val MERCHANT_NAME = "MerchantName"
    const val MERCHANT_ID = "MerchantId"
}