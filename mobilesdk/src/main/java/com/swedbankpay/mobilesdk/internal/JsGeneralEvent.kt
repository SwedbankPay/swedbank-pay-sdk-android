package com.swedbankpay.mobilesdk.internal

import androidx.annotation.Keep
import com.swedbankpay.Id

@Keep
internal data class JsGeneralEvent(
    val event: String? = null,
    val paymentOrder: Id? = null,
    val sourceEvent: String? = null
)

@Keep
internal data class JsOnPaidEvent(
    val event: String? = null,
    val paymentOrder: Id? = null,
    val redirectUrl: String? = null
)