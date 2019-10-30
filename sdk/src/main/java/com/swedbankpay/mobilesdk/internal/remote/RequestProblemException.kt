package com.swedbankpay.mobilesdk.internal.remote

import com.swedbankpay.mobilesdk.Problem
import java.io.IOException

internal class RequestProblemException(val problem: Problem, cause: Throwable? = null) : IOException(cause)