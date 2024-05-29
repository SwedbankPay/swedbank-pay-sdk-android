package com.swedbankpay.mobilesdk.nativepayments.api.model.request.util

import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.core.os.ConfigurationCompat
import com.swedbankpay.mobilesdk.BuildConfig
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.Client
import com.swedbankpay.mobilesdk.nativepayments.api.model.request.Service
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*

internal object RequestDataUtil {


    fun getClient() = Client(
        userAgent = "SwedbankPaySDK-Android/${getVersion()}",
        ipAddress = getIPAddress(),
        screenHeight = getPhoneSize().heightPixels,
        screenWidth = getPhoneSize().widthPixels,
        screenColorDepth = 24,
    )

    fun getService() = Service(
        name = "SwedbankPaySDK-Android",
        version = getVersion()
    )

    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    private fun getIPAddress(): String {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addresses = Collections.list(intf.inetAddresses);
                for (address in addresses) {
                    if (!address.isLoopbackAddress && !address.isLinkLocalAddress) {
                        val hostAddress = address.hostAddress;
                        val delimiter = hostAddress?.indexOf('%') //
                        delimiter?.let {
                            return if (it < 0) {
                                hostAddress
                            } else {
                                hostAddress.substring(0, delimiter)
                            }
                        }
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            return ""
        }
        return ""
    }

    fun getTimeZoneOffset(): String = SimpleDateFormat("Z", Locale.getDefault()).format(Date())

    fun nowAsIsoString(): String =
        SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss'Z'Z", Locale.getDefault()).format(Date())

    private fun getPhoneSize(): DisplayMetrics = Resources.getSystem().displayMetrics

    private fun getVersion() = BuildConfig.SDK_VERSION.take(5)

    fun getLanguages() =
        ConfigurationCompat.getLocales(Resources.getSystem().configuration).toLanguageTags()
}