package com.payex.mobilesdk.internal.remote

import com.payex.mobilesdk.BadRequestDescription
import java.io.IOException

internal class BadRequestException(
    val description: BadRequestDescription
) : IOException(description.code.toString())