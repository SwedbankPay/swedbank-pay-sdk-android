package com.swedbankpay.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.swedbankpay.mobilesdk.internal.asIntOrNull
import com.swedbankpay.mobilesdk.internal.asStringOrNull
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import java.io.IOException
import java.io.ObjectInputStream
import java.io.Serializable

/**
 * An RFC 7807 HTTP API Problem Details object.
 *
 * The SDK defines a subclass of Problem for problems expected to be
 * reported from a server implementing the Merchant Backend API.
 *
 * There is a [subclass][com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendProblem]
 * for problems expected to be reported by a server implementing the
 * Merchant Backend API.
 */
@Parcelize
open class Problem internal constructor(
    /**
     * The raw RFC 7807 object.
     */
    val raw: String,

    // JsonObject is not Serializable so we mark it as transient
    // and recreate it during deserialization
    @Transient
    private var _jsonObject: JsonObject
) : Parcelable, Serializable {
    internal companion object : Parceler<Problem> {
        override fun create(parcel: Parcel) = Problem(parcel = parcel)
        override fun Problem.write(parcel: Parcel, flags: Int) {
            parcel.writeString(raw)
        }
    }
    /**
     * Constructs a Problem from a parcel where it was previously written using [writeToParcel].
     */
    constructor(parcel: Parcel) : this(
        raw = checkNotNull(parcel.readString())
    )

    /**
     * Interprets a Gson JsonObject as a Problem.
     *
     * N.B! From an API stability perspective, please consider this constructor
     * an implementation detail. It is, however, exposed for convenience.
     */
    constructor(jsonObject: JsonObject) : this(
        raw = jsonObject.toString(),
        _jsonObject = jsonObject
    )

    /**
     * Parses a Problem from a String.
     *
     * @throws IllegalArgumentException if `raw`  does not represent a JSON object
     */
    constructor(raw: String) : this(
        raw = raw,
        _jsonObject = try {
            JsonParser.parseString(raw).asJsonObject
        } catch (e: Exception) {
            throw IllegalArgumentException("$raw is not a JSON object", e)
        }
    )

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(stream: ObjectInputStream) {
        stream.defaultReadObject()
        _jsonObject = JsonParser.parseString(raw).asJsonObject
    }

    /**
     * The raw RFC 7807 object parsed as a Gson JsonObject.
     *
     * N.B! From an API stability perspective, please consider this property
     * an implementation detail. It is, however, exposed for convenience.
     */
    val jsonObject: JsonObject
        get() = _jsonObject

    override fun equals(other: Any?): Boolean {
        return other is Problem
                && other::class == this::class
                && other.jsonObject == jsonObject
    }

    override fun hashCode() = jsonObject.hashCode()

    override fun toString() = raw

    /**
     * RFC 7807 default property: a URI reference that identifies the problem type.
     *
     * Defaults to "about:blank" if not present in the JSON.
     */
    val type get() = jsonObject["type"]?.asStringOrNull ?: "about:blank"
    /**
     * RFC 7807 default property: a short summary of the problem.
     */
    val title get() = jsonObject["title"]?.asStringOrNull
    /**
     * RFC 7807 default property: the HTTP status code
     *
     * This should always be the same as the actual HTTP status code
     * reported by the server.
     */
    val status: Int? get() = jsonObject["status"]?.asIntOrNull
    /**
     * RFC 7807 default property: a detailed explanation of the problem
     */
    val detail get() = jsonObject["detail"]?.asStringOrNull
    /**
     * RFC 7807 default property: a URI reference that identifies the specific
     * occurrence of the problem
     */
    val instance get() = jsonObject["instance"]?.asStringOrNull
}
