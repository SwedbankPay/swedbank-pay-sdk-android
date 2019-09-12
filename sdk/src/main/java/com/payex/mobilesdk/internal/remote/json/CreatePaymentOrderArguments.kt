package com.payex.mobilesdk.internal.remote.json

import com.google.gson.annotations.SerializedName

internal class CreatePaymentOrderArguments(
    @SerializedName("customerProfileRef") private val consumerProfileRef: String?,
    @SerializedName("merchantData") private val merchantData: String?
)