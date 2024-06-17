package com.swedbankpay.mobilesdk.paymentsession.webviewservice.client

import android.webkit.JavascriptInterface
import android.webkit.WebView
import org.intellij.lang.annotations.Language
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

internal class RequestInspectorJavaScriptInterface(webView: WebView) {

    init {
        webView.addJavascriptInterface(this, INTERFACE_NAME)
    }

    private val recordedRequests = ArrayList<RecordedRequest>()

    fun findRecordedRequestForUrl(url: String): RecordedRequest? {
        return synchronized(recordedRequests) {
            // use findLast instead of find to find the last added query matching a URL -
            // they are included at the end of the list when written.
            recordedRequests.findLast { recordedRequest ->
                // Added search by exact URL to find the actual request body
                url == recordedRequest.url
            } ?: recordedRequests.findLast { recordedRequest ->
                // Previously, there was only a search by contains, and because of this, sometimes the wrong request body was found
                url.contains(recordedRequest.url)
            }
        }
    }

    data class RecordedRequest(
        val type: WebViewRequestType,
        val url: String,
        val method: String,
        val formParameters: Map<String, String>,
        val headers: Map<String, String>,
        val trace: String,
        val enctype: String?
    )

    @JavascriptInterface
    fun recordFormSubmission(
        url: String,
        method: String,
        formParameterList: String,
        headers: String,
        trace: String,
        enctype: String?
    ) {
        val formParameterJsonArray = JSONArray(formParameterList)
        val headerMap = getHeadersAsMap(headers)
        val formParameterMap = getFormParametersAsMap(formParameterJsonArray)

        addRecordedRequest(
            RecordedRequest(
                WebViewRequestType.FORM,
                url,
                method,
                formParameterMap,
                headerMap,
                trace,
                enctype
            )
        )
    }

    private fun addRecordedRequest(recordedRequest: RecordedRequest) {
        synchronized(recordedRequests) {
            recordedRequests.add(recordedRequest)
        }
    }

    private fun getHeadersAsMap(headersString: String): MutableMap<String, String> {
        val headersObject = JSONObject(headersString)
        val map = HashMap<String, String>()
        for (key in headersObject.keys()) {
            val lowercaseHeader = key.lowercase(Locale.getDefault())
            map[lowercaseHeader] = headersObject.getString(key)
        }
        return map
    }

    private fun getFormParametersAsMap(formParameterJsonArray: JSONArray): Map<String, String> {
        val map = HashMap<String, String>()
        repeat(formParameterJsonArray.length()) { i ->
            val formParameter = formParameterJsonArray.get(i) as JSONObject
            val name = formParameter.getString("name")
            val value = formParameter.getString("value")
            val checked = formParameter.optBoolean("checked")
            val type = formParameter.getString("type")
            if (!isExcludedFormParameter(type, checked)) {
                map[name] = value
            }
        }
        return map
    }

    private fun isExcludedFormParameter(type: String, checked: Boolean): Boolean {
        return (type == "radio" || type == "checkbox") && !checked
    }

    companion object {
        private const val LOG_TAG = "RequestInspectorJs"
        private const val INTERFACE_NAME = "RequestInspection"

        @Language("JS")
        private const val JAVASCRIPT_INTERCEPTION_CODE = """
function getFullUrl(url) {
    if (url.startsWith("/")) {
        return location.protocol + '//' + location.host + url;
    } else {
        return url;
    }
}

function recordFormSubmission(form) {
    var jsonArr = [];
    for (i = 0; i < form.elements.length; i++) {
        var parName = form.elements[i].name;
        var parValue = form.elements[i].value;
        var parType = form.elements[i].type;
        var parChecked = form.elements[i].checked;
        var parId = form.elements[i].id;

        jsonArr.push({
            name: parName,
            value: parValue,
            type: parType,
            checked:parChecked,
            id:parId
        });
    }

    const path = form.attributes['action'] === undefined ? "/" : form.attributes['action'].nodeValue;
    const method = form.attributes['method'] === undefined ? "GET" : form.attributes['method'].nodeValue;
    const url = getFullUrl(path);
    const encType = form.attributes['enctype'] === undefined ? "application/x-www-form-urlencoded" : form.attributes['enctype'].nodeValue;
    const err = new Error();
    $INTERFACE_NAME.recordFormSubmission(
        url,
        method,
        JSON.stringify(jsonArr),
        "{}",
        err.stack,
        encType
    );
}

function handleFormSubmission(e) {
    const form = e ? e.target : this;
    recordFormSubmission(form);
    form._submit();
}

HTMLFormElement.prototype._submit = HTMLFormElement.prototype.submit;
HTMLFormElement.prototype.submit = handleFormSubmission;
window.addEventListener('submit', function (submitEvent) {
    handleFormSubmission(submitEvent);
}, true);
        """

        fun enabledRequestInspection(webView: WebView) {
            webView.evaluateJavascript(
                "javascript: $JAVASCRIPT_INTERCEPTION_CODE",
                null
            )
        }
    }
}