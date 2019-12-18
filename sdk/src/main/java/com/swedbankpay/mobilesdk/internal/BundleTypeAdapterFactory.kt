package com.swedbankpay.mobilesdk.internal

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

internal object BundleTypeAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        if (!Bundle::class.java.isAssignableFrom(type.rawType)) {
            return null
        }

        @Suppress("UNCHECKED_CAST")
        return object : TypeAdapter<Bundle>() {
            override fun write(out: JsonWriter, bundle: Bundle?) {
                when (bundle) {
                    null -> out.nullValue()
                    else -> {
                        out.beginObject()
                        for (key in bundle.keySet()) {
                            out.name(key)
                            when (val value = bundle[key]) {
                                null -> out.nullValue()
                                else -> gson.toJson(value, value.javaClass, out)
                            }
                        }
                        out.endObject()
                    }
                }
            }
            override fun read(`in`: JsonReader?): Bundle {
                TODO("Not needed. If you see this error, this is no longer true, and you must implement this method.")
            }
        } as TypeAdapter<T>
    }
}