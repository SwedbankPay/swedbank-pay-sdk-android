package com.swedbankpay.mobilesdk.paymentsession.api

internal object PaymentSessionAPIConstants {
    const val REQUEST_TIME_OUT_IN_MS = 10 * 1000
    const val REQUEST_TIME_OUT_IN_MS_FOR_CREDIT_CARD = 30 * 1000

    const val SESSION_TIME_OUT_IN_MS = 20 * 1000
    const val SESSION_TIME_OUT_IN_MS_FOR_CREDIT_CARD = 30 * 1000

    const val NOTIFICATION_URL = "https://fake.payex.com/notification"

    // Expects name constants
    const val THREE_DS_METHOD_DATA = "threeDSMethodData"
    const val CREQ = "creq"
    const val TRAMPOLINE_NOTIFICATION_URL = "NotificationUrl"

    // Query parameters name constants
    const val CRES = "cres"

    val commonHeaders: Map<String, String> = mapOf(
        HTTPHeaderField.ACCEPT_TYPE.rawValue to ContentType.JSON.rawValue,
        HTTPHeaderField.CONTENT_TYPE.rawValue to ContentType.JSON.rawValue
    )

    enum class HTTPHeaderField {
        ACCEPT_TYPE {
            override val rawValue: String
                get() = "Accept"
        },
        CONTENT_TYPE {
            override val rawValue: String
                get() = "Content-Type"
        };

        abstract val rawValue: String
    }

    enum class ContentType {
        JSON {
            override val rawValue: String
                get() = "application/json"
        };

        abstract val rawValue: String
    }
}