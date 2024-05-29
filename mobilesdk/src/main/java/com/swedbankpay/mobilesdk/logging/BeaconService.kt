package com.swedbankpay.mobilesdk.logging

import com.google.gson.Gson
import com.swedbankpay.mobilesdk.logging.model.BeaconInput
import com.swedbankpay.mobilesdk.logging.model.BeaconType
import com.swedbankpay.mobilesdk.logging.model.EventAction
import com.swedbankpay.mobilesdk.logging.model.EventModel
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.util.RequestDataUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.URL
import java.util.LinkedList
import java.util.Queue
import javax.net.ssl.HttpsURLConnection

internal object BeaconService {

    private var beaconUrl: String? = null
    private val beacons: Queue<BeaconInput> = LinkedList()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun setBeaconUrl(url: String?) {
        beaconUrl = url
    }

    fun logEvent(eventAction: EventAction) {
        var beaconInput = BeaconInput(
            type = BeaconType.CLIENT_EVENT.identifier,
            service = RequestDataUtil.getService(),
            client = RequestDataUtil.getClient(),
            event = EventModel(
                created = RequestDataUtil.nowAsIsoString(),
                action = eventAction.action,
                duration = when (eventAction) {
                    is EventAction.HttpRequest -> eventAction.duration
                    else -> null
                }
            ),
            extensions = eventAction.extensions
        )

        when (eventAction) {
            is EventAction.HttpRequest -> {
                beaconInput = beaconInput.copy(http = eventAction.http)
            }

            is EventAction.SDKCallbackInvoked -> {
                beaconInput = beaconInput.copy(method = eventAction.method)
            }

            is EventAction.SDKMethodInvoked -> {
                beaconInput = beaconInput.copy(method = eventAction.method)
            }

            else -> {
                // Event actions that doesn't need additional data models
                // EventAction.LaunchClientApp
            }
        }

        beacons.add(beaconInput)
        if (beaconUrl != null) {
            pickFromQueue()
        }

    }

    private fun pickFromQueue() {
        scope.launch {
            makeLogRequest(beacons.poll())
        }
    }

    fun clearQueue() {
        beacons.clear()
    }

    private fun makeLogRequest(beaconInput: BeaconInput?) {
        beaconInput?.let {
            try {
                val beaconInputData = Gson().toJson(beaconInput)

                val url = URL(beaconUrl)

                val connection = url.openConnection() as HttpsURLConnection

                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept", "application/json")
                connection.doInput = true
                connection.doOutput = true

                val outputStreamWriter = OutputStreamWriter(connection.outputStream)
                outputStreamWriter.write(beaconInputData)
                outputStreamWriter.flush()

                val responseCode = connection.responseCode

                if (responseCode == HttpsURLConnection.HTTP_NO_CONTENT) {
                    pickFromQueue()
                } else {
                    beacons.add(beaconInput)
                }
            } catch (e: Exception) {
                beacons.add(beaconInput)
            }
        }
    }
}