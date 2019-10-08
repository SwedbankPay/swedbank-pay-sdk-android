package com.payex.mobilesdk.internal.remote

import com.payex.mobilesdk.Problem
import java.io.IOException

internal class RequestProblemException(val problem: Problem, cause: Throwable? = null) : IOException(cause)