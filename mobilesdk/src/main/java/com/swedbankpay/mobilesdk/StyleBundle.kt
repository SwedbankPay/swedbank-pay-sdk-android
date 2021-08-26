package com.swedbankpay.mobilesdk

import android.os.Bundle
import android.os.Parcelable
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.annotations.JsonAdapter
import com.swedbankpay.mobilesdk.internal.BundleTypeAdapterFactory
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Type

/**
 * Convert a [Map] containing styling parameters to a [Bundle].
 *
 * Styling parameters must be [passed][PaymentFragment.ARG_STYLE]
 * to [PaymentFragment] as a [Bundle], but it may be more convenient
 * to build the parameters as a [Map] and convert them to a [Bundle]
 * with this function.
 *
 * It may be simpler to use [PaymentFragment.ArgumentsBuilder]
 * and [style][PaymentFragment.ArgumentsBuilder.style] instead.
 *
 * E.g:
 *
 *     arguments.putBundle(ARG_STYLE, mapOf(
 *         "thisElement" to mapOf(
 *             "thisAttribute" to "x",
 *             "thatAttribute" to "y"
 *         ),
 *         "thatElement" to mapOf(
 *             "yonAttribute" to "z"
 *         )
 *     ).toStyleBundle())
 */
fun Map<*, *>.toStyleBundle(): Bundle {
    return Bundle(size).apply {
        for ((key, value) in this@toStyleBundle) {
            if (key != null && value != null) {
                putStyleValue(key.toString(), value)
            }
        }
    }
}
internal fun Bundle.toStyleJs(): String {
    return GsonBuilder()
        .registerTypeAdapterFactory(BundleTypeAdapterFactory)
        .create()
        .toJson(this)
}

private fun Bundle.putStyleValue(key: String?, value: Any) {
    when (value) {
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Map<*, *> -> putBundle(key, value.toStyleBundle())
        is Bundle -> putBundle(key, value)
        is Long -> putLong(key, value)
        is Double -> putDouble(key, value)
        is Float -> putFloat(key, value)
        is Boolean -> putBoolean(key, value)
        is CharSequence -> putCharSequence(key, value)
        is List<*> -> putStyleValueList(key, value)
        is Array<*> -> putStyleValueList(key, value.asList())
    }
}

private fun Bundle.putStyleValueList(key: String?, list: List<*>) {
    putParcelableArrayList(key, list.mapTo(ArrayList(list.size)) {
        it?.let(::AnyStyleValue)
    })
}

@Parcelize
@JsonAdapter(AnyStyleValue.Serializer::class)
private class AnyStyleValue private constructor(
    private val bundle: Bundle
) : Parcelable {
    // This is a bit inefficient, but this way we don't need
    // to write the polymorphic put logic twice.
    // This is not exactly a hot path, and a concerned
    // user can build the Bundle manually themself.
    constructor(wrapped: Any) : this(
        Bundle(1).apply { putStyleValue(null, wrapped) }
    )
    val wrapped get() = bundle.get(null)

    class Serializer : JsonSerializer<AnyStyleValue> {
        override fun serialize(
            src: AnyStyleValue,
            typeOfSrc: Type?,
            context: JsonSerializationContext
        ): JsonElement {
            return context.serialize(src.wrapped)
        }
    }
}
