package com.swedbankpay.mobilesdk.test.integration

import com.swedbankpay.mobilesdk.RequestDecorator
import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration

internal val paymentTestConfiguration = MerchantBackendConfiguration
    .Builder("https://payex-merchant-samples.ey.r.appspot.com/")
    .requestDecorator(
        RequestDecorator.withHeaders(
            "x-payex-sample-apikey", "c339f53d-8a36-4ea9-9695-75048e592cc0",
            "x-payex-sample-access-token", "token123"
        ))
    .build()
