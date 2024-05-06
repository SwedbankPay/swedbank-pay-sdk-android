package com.swedbankpay.mobilesdk.nativepayments.model.request.util

import android.content.res.Resources
import com.swedbankpay.mobilesdk.BuildConfig
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

internal object IntegrationRequestDataUtil {

    fun getLocalIpAddress(): String? {
        try {
            val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val networkInterface: NetworkInterface = en.nextElement()
                val enumerationIpAddress: Enumeration<InetAddress> = networkInterface.inetAddresses
                while (enumerationIpAddress.hasMoreElements()) {
                    val inetAddress: InetAddress = enumerationIpAddress.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return null
    }

    fun getTimeZoneOffset(): String {
        val tz = TimeZone.getDefault()
        val newOffset = tz.rawOffset

        return String.format(
            "%s%02d%02d",
            if (newOffset >= 0) "+" else "-",
            newOffset / 3600000,
            newOffset / 60000 % 60
        )
    }

    fun getPhoneSize() = Resources.getSystem().displayMetrics

    fun getVersion() = BuildConfig.SDK_VERSION.take(5)
}