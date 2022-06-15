package com.swedbankpay.mobilesdk.merchantbackend.test.integration

import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration
import com.swedbankpay.mobilesdk.merchantbackend.RequestDecorator

internal val paymentOnlyTestConfiguration = MerchantBackendConfiguration
    .Builder("https://payex-merchant-samples.ey.r.appspot.com/")
    .requestDecorator(
        RequestDecorator.withHeaders(
            "x-payex-sample-apikey", "c339f53d-8a36-4ea9-9695-75048e592cc0",
            "x-payex-sample-access-token", "token123"
        ))
    .build()

internal val enterpriseTestConfiguration = MerchantBackendConfiguration
    .Builder("https://enterprise-dev-dot-payex-merchant-samples.ey.r.appspot.com/")
    .requestDecorator(
        RequestDecorator.withHeaders(
            "x-payex-sample-apikey", "c339f53d-8a36-4ea9-9695-75048e592cc0",
            "x-payex-sample-access-token", "token123"
        ))
    .build()

internal val testConfigurations = arrayOf(enterpriseTestConfiguration, paymentOnlyTestConfiguration)
internal var paymentTestConfiguration = paymentOnlyTestConfiguration