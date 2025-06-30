package com.swedbankpay.mobilesdk.internal

import com.google.gson.Gson
import com.google.gson.JsonElement

private inline fun <T> tryOrNull(f: () -> T) = try { f() } catch (_: Exception) { null }
internal val JsonElement.asStringOrNull get() = tryOrNull { asString }
internal val JsonElement.asIntOrNull get() = tryOrNull { asInt }

internal fun <T> parseEvent(event: String, type: Class<T>): T? {
    return try {
        Gson().fromJson(event, type)
    } catch (_: Exception) {
        null
    }
}
