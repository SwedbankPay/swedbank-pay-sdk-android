package com.swedbankpay.mobilesdk.nativepayments.api.model.request.util

import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.core.os.ConfigurationCompat
import com.swedbankpay.mobilesdk.BuildConfig
import java.net.NetworkInterface
import java.util.*

internal object RequestDataUtil {

    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addresses = Collections.list(intf.inetAddresses);
                for (address in addresses) {
                    if (!address.isLoopbackAddress && !address.isLinkLocalAddress) {
                        val hostAddress = address.hostAddress;
                        val isIPv4: Boolean = (hostAddress?.indexOf(':') ?: 0) < 0
                        if (useIPv4) {
                            if (isIPv4)
                                return hostAddress;
                        } else {
                            if (!isIPv4) {
                                val delimiter = hostAddress?.indexOf('%') //
                                delimiter?.let {
                                    return if (it < 0) {
                                        hostAddress
                                    } else {
                                        hostAddress.substring(0, delimiter)
                                    }
                                }// drop ip6 zone suffix

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

    fun getTimeZoneOffset(): String {
        val tz = TimeZone.getDefault()
        val newOffset = tz.getOffset(System.currentTimeMillis())

        return String.format(
            "%s%02d%02d",
            if (newOffset >= 0) "+" else "-",
            newOffset / 3600000,
            newOffset / 60000 % 60
        )
    }

    fun getPhoneSize(): DisplayMetrics = Resources.getSystem().displayMetrics

    fun getVersion() = BuildConfig.SDK_VERSION.take(5)

    fun getLanguages() =
        ConfigurationCompat.getLocales(Resources.getSystem().configuration).toLanguageTags()
}