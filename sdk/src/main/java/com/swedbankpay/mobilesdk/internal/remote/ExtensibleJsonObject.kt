package com.swedbankpay.mobilesdk.internal.remote

import android.os.Bundle
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

internal interface ExtensibleJsonObject {
    val extensionProperties: Bundle?

    object TypeAdapterFactory : com.google.gson.TypeAdapterFactory {
        override fun <T> create(
            gson: Gson,
            type: TypeToken<T>
        ): TypeAdapter<T>? {
            if (!ExtensibleJsonObject::class.java.isAssignableFrom(type.rawType)) {
                return null
            }

            val delegate = gson.getDelegateAdapter(this, type)
            return object : TypeAdapter<T>() {
                override fun write(out: JsonWriter?, value: T) {
                    val extension = (value as ExtensibleJsonObject).extensionProperties
                    if (extension == null) {
                        delegate.write(out, value)
                    } else {
                        val json = delegate.toJsonTree(value).asJsonObject
                        for (key in extension.keySet()) {
                            json.add(key, gson.toJsonTree(extension[key]))
                        }
                        gson.toJson(json, out)
                    }
                }

                override fun read(`in`: JsonReader?): T {
                    TODO("Not needed. If you see this error, this is no longer true, and you must implement this method.")
                }
            }
        }
    }
}


