package com.swedbankpay.mobilesdk.logging.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.Client
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.Service
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.util.RequestDataUtil


@Keep
internal data class BeaconInput(
    @SerializedName("type")
    val type: String,
    @SerializedName("service")
    val service: Service,
    @SerializedName("client")
    val client: Client,
    @SerializedName("event")
    val event: EventModel? = null,
    @SerializedName("http")
    val http: HttpModel? = null,
    @SerializedName("method")
    val method: MethodModel? = null,
    @SerializedName("extensions")
    val extensions: ExtensionsModel? = null,
)

@Keep
enum class BeaconType(val identifier: String) {
    @SerializedName("ClientEvent")
    CLIENT_EVENT("ClientEvent"),

    @SerializedName("ClientSla")
    CLIENT_SLA("ClientSla"),

    @SerializedName("RemoteSla")
    REMOTE_SLA("RemoteSla"),
}

@Keep
data class EventModel(
    @SerializedName("created")
    val created: String,
    @SerializedName("action")
    val action: String,
    @SerializedName("duration")
    val duration: Int? = null,
)

@Keep
data class HttpModel(
    @SerializedName("requestUrl")
    val requestUrl: String,
    @SerializedName("method")
    val method: String? = null,
    @SerializedName("responseStatusCode")
    val responseStatusCode: Int? = null,
)

@Keep
data class MethodModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("sdk")
    val sdk: String? = RequestDataUtil.SDK_NAME,
    @SerializedName("succeeded")
    val succeeded: Boolean? = null,
)

@Keep
data class ExtensionsModel(
    @SerializedName("details")
    val details: String? = null,
    @SerializedName("values")
    val values: MutableMap<String, Any?>? = null,
)

