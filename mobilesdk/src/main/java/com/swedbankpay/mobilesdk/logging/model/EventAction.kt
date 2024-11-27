package com.swedbankpay.mobilesdk.logging.model

sealed class EventAction(val action: String, val extensions: ExtensionsModel? = null) {
    class SDKMethodInvoked(
        val method: MethodModel?,
        extensions: ExtensionsModel? = null
    ) : EventAction("SDKMethodInvoked", extensions)

    class SDKCallbackInvoked(
        val method: MethodModel?,
        extensions: ExtensionsModel? = null
    ) : EventAction("SDKCallbackInvoked", extensions)

    class HttpRequest(
        val http: HttpModel?,
        val duration: Int?,
        extensions: ExtensionsModel? = null
    ) : EventAction("HttpRequest", extensions)

    class LaunchClientApp(
        extensions: ExtensionsModel?
    ) : EventAction("LaunchClientApp", extensions)

    class ClientAppCallback(
        extensions: ExtensionsModel?
    ) : EventAction("ClientAppCallback", extensions)

    class SCAMethodRequest(
        val http: HttpModel?,
        val duration: Int?,
        extensions: ExtensionsModel?
    ) : EventAction("SCAMethodRequest", extensions)

    class SCARedirectResult(
        val http: HttpModel?,
        val duration: Int?,
        extensions: ExtensionsModel?
    ) : EventAction("SCARedirectResult", extensions)
}