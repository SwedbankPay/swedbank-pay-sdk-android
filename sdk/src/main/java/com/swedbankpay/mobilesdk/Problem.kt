package com.swedbankpay.mobilesdk

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.swedbankpay.mobilesdk.internal.asIntOrNull
import com.swedbankpay.mobilesdk.internal.asStringOrNull
import com.swedbankpay.mobilesdk.internal.makeCreator
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
 *
 * IMPORTANT: Problem synchronizes on itself, so you should never synchronize
 * on a Problem object yourself.
 */
@Suppress("unused")
open class Problem : Parcelable, Serializable {
    companion object {
        @JvmField
        val CREATOR = makeCreator(::Problem)
    }

    // JsonObject is not Serializable so we mark it as transient
    // and recreate it if needed
    @Transient
    @Volatile
    private var _jsonObject: JsonObject?
    /**
     * The raw RFC 7807 object parsed as a Gson JsonObject.
     *
     * N.B! From an API stability perspective, please consider this property
     * an implementation detail. It is, however, exposed for convenience.
     */
    val jsonObject: JsonObject
        get() = _jsonObject ?: synchronized(this) {
            // Yes, we are effectively reimplementing lazy here.
            // We have little choice, however, as lazy does not provide
            // an implementation that marks the payload as transient,
            // which we require here for Serializable.
            _jsonObject ?: JsonParser.parseString(raw).asJsonObject.also {
                _jsonObject = it
            }
        }

    /**
     * The raw RFC 7807 object.
     */
    val raw: String

    /**
     * RFC 7807 default property: a URI reference that identifies the problem type.
     *
     * Defaults to "about:blank" if not present in the JSON.
     */
    val type get() = jsonObject.get("type")?.asStringOrNull ?: "about:blank"
    /**
     * RFC 7807 default property: a short summary of the problem.
     */
    val title get() = jsonObject.get("title")?.asStringOrNull
    /**
     * RFC 7807 default property: the HTTP status code
     *
     * This should always be the same as the actual HTTP status code
     * reported by the server.
     */
    val status: Int? get() = jsonObject.get("status")?.asIntOrNull
    /**
     * RFC 7807 default property: a detailed explanation of the problem
     */
    val detail get() = jsonObject.get("detail")?.asStringOrNull
    /**
     * RFC 7807 default property: a URI reference that identifies the specific
     * occurrence of the problem
     */
    val instance get() = jsonObject.get("instance")?.asStringOrNull

    /**
     * Interprets a Gson JsonObject as a Problem.
     *
     * N.B! From an API stability perspective, please consider this constructor
     * an implementation detail. It is, however, exposed for convenience.
     */
    constructor(jsonObject: JsonObject) {
        _jsonObject = jsonObject
        raw = jsonObject.toString()
    }

    /**
     * Parses a Problem from a String.
     *
     * @throws IllegalArgumentException if `raw`  does not represent a JSON object
     */
    constructor(raw: String) {
        val jsonObject = try {
            JsonParser.parseString(raw).asJsonObject
        } catch (e: Exception) {
            throw IllegalArgumentException("$raw is not a JSON object", e)
        }
        _jsonObject = jsonObject
        this.raw = raw
    }

    override fun describeContents() = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(raw)
    }
    constructor(parcel: Parcel) : this(
        checkNotNull(parcel.readString())
    )
}
