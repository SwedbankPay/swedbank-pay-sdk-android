package com.swedbankpay.mobilesdk.paymentsession.api.model.request.util

import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.core.os.ConfigurationCompat
import com.swedbankpay.mobilesdk.BuildConfig
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.Browser
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.Client
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.Service
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.net.NetworkInterface
import java.util.*

internal object RequestDataUtil {

    const val SDK_NAME = "SwedbankPaySDK-Android"

    fun getClient() = Client(
        userAgent = "${SDK_NAME}/${getVersion()}",
        ipAddress = getIPAddress(),
        screenHeight = getPhoneSize().heightPixels,
        screenWidth = getPhoneSize().widthPixels,
        screenColorDepth = 24,
    )

    fun getBrowser() = Browser(
        acceptHeader = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
        languageHeader = getLanguages(),
        timeZoneOffset = getTimeZoneOffsetInMinutes(),
        javascriptEnabled = true
    )

    fun getService() = Service(
        name = SDK_NAME,
        version = getVersion()
    )

    /**
     * Get IP address from first non-localhost interface
     * @return  address or empty string
     */
    private fun getIPAddress(): String {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addresses = Collections.list(intf.inetAddresses)
                for (address in addresses) {
                    if (!address.isLoopbackAddress && !address.isLinkLocalAddress) {
                        val hostAddress = address.hostAddress
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

    private fun getTimeZoneOffsetInMinutes(): Int = ZonedDateTime.now().offset.totalSeconds / 60

    // 2024-09-25T16:46:46.923+02:00
    fun nowAsIsoString(): String {
        val formatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSSXXX")
        return ZonedDateTime.now().format(formatter)
    }


    private fun getPhoneSize(): DisplayMetrics = Resources.getSystem().displayMetrics

    private fun getVersion() = BuildConfig.SDK_VERSION.take(5)

    private fun getLanguages() =
        ConfigurationCompat.getLocales(Resources.getSystem().configuration).toLanguageTags()
}