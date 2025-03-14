package com.swedbankpay.mobilesdk.paymentsession.api.model.request.util

import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.core.os.ConfigurationCompat
import com.swedbankpay.mobilesdk.BuildConfig
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.Browser
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.Client
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.ClientMinimum
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.ClientWithType
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.PresentationSdk
import com.swedbankpay.mobilesdk.paymentsession.api.model.request.Service
import java.net.NetworkInterface
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

internal object RequestDataUtil {

    const val SDK_NAME = "SwedbankPaySDK-Android"

    inline fun <reified T> getClient(): T {
        return when (T::class) {
            Client::class -> Client(
                userAgent = "${SDK_NAME}/${getVersion()}",
                ipAddress = getIPAddress(),
                screenHeight = getPhoneSize().heightPixels,
                screenWidth = getPhoneSize().widthPixels,
                screenColorDepth = 24,
            ) as T

            ClientWithType::class -> ClientWithType(
                userAgent = "${SDK_NAME}/${getVersion()}",
                ipAddress = getIPAddress(),
                screenHeight = getPhoneSize().heightPixels,
                screenWidth = getPhoneSize().widthPixels,
                screenColorDepth = 24,
                clientType = "Native"
            ) as T

            ClientMinimum::class -> ClientMinimum(
                userAgent = "${SDK_NAME}/${getVersion()}",
                ipAddress = getIPAddress(),
            ) as T

            else -> throw IllegalStateException("Type must be of Client, ClientWithType or ClientMinimum")
        }
    }

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

    fun getPresentationSdk() = PresentationSdk(
        name = "Android",
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

    private fun getTimeZoneOffsetInMinutes(): Int {
        val mCalendar: Calendar = GregorianCalendar()
        val mTimeZone: TimeZone = mCalendar.timeZone

        // Get the timezone offset with the function getRawOffset and add the daylight savings
        val mGMTOffset: Int =
            mTimeZone.rawOffset + (if (mTimeZone.inDaylightTime(Date())) mTimeZone.dstSavings else 0)

        // Return the converted offset to minutes
        return TimeUnit.MINUTES.convert(mGMTOffset.toLong(), TimeUnit.MILLISECONDS).toInt()
    }

    // 2024-09-25T16:46:46.923+02:00
    fun nowAsIsoString(): String {
        val formatter: SimpleDateFormat =
            object : SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sssZ", Locale.getDefault()) {
                override fun format(
                    date: Date,
                    toAppendTo: StringBuffer,
                    pos: FieldPosition
                ): StringBuffer {
                    val toFix = super.format(date, toAppendTo, pos)
                    return toFix.insert(toFix.length - 2, ':')
                }
            }
        return formatter.format(Date())
    }


    private fun getPhoneSize(): DisplayMetrics = Resources.getSystem().displayMetrics

    private fun getVersion() = BuildConfig.SDK_VERSION.take(5)

    private fun getLanguages() =
        ConfigurationCompat.getLocales(Resources.getSystem().configuration).toLanguageTags()
}