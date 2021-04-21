package com.swedbankpay.mobilesdk.test.integration.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream

internal fun openTestLogOutputStream(context: Context, name: String): PrintStream {
    val dir = context.getExternalFilesDir(null)
        ?: throw IOException("External files dir not available")
    val file = dir.resolve(name)
    return PrintStream(FileOutputStream(file, true).buffered())
}
