package com.swedbankpay.mobilesdk.test.parcelable

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.swedbankpay.mobilesdk.*
import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendProblem
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.Serializable
import java.time.LocalDate
import java.util.*

/**
 * Test that Parcelable implementations are correct
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ParcelableTest {

    private fun testParcelable(parcelable: Parcelable) {
        val parcel = Parcel.obtain()

        parcel.writeParcelable(parcelable, 0)
        parcel.setDataPosition(0)
        val unparceled = parcel.readParcelable<Parcelable>(parcelable.javaClass.classLoader)
        Assert.assertEquals(parcelable, unparceled)

        parcel.recycle()
    }

    /**
     * Check that Consumer parcelizes correctly
     */
    @Test
    fun testConsumer() {
        testParcelable(Consumer(
            operation = ConsumerOperation.INITIATE_CONSUMER_SESSION,
            language = Language.SWEDISH,
            shippingAddressRestrictedToCountryCodes = listOf("test", "data")
        ))
    }

    private val paymentOrderUrls = PaymentOrderUrls(
        hostUrls = listOf("test", "data"),
        completeUrl = "completeUrl",
        cancelUrl = "cancelUrl",
        paymentUrl = "paymentUrl",
        callbackUrl = "callbackUrl",
        termsOfServiceUrl = "termsOfServiceUrl"
    )
    /**
     * Check that PaymentOrderUrls parcelizes correctly
     */
    @Test
    fun testPaymentOrderUrls() {
        testParcelable(paymentOrderUrls)
    }

    private val payeeInfo = PayeeInfo(
        payeeId = "payeeId",
        payeeReference = "payeeReference",
        payeeName = "payeeName",
        productCategory = "productCategory",
        orderReference = "orderReference",
        subsite = "subsite"
    )
    /**
     * Check that PayeeInfo parcelizes correctly
     */
    @Test
    fun testPayeeInfo() {
        testParcelable(payeeInfo)
    }

    private val paymentOrderPayer = PaymentOrderPayer(
        consumerProfileRef = "consumerProfileRef",
        email = "email",
        msisdn = "msisdn",
        payerReference = "payerReference"
    )
    /**
     * Check that PaymentOrderPayer parcelizes correctly
     */
    @Test
    fun testPaymentOrderPayer() {
        testParcelable(paymentOrderPayer)
    }

    private val orderItem = OrderItem(
        reference = "reference",
        name = "name",
        type = ItemType.PRODUCT,
        `class` = "class",
        itemUrl = "itemUrl",
        imageUrl = "imageUrl",
        description = "description",
        discountDescription = "discountDescription",
        quantity = 1234,
        quantityUnit = "quantityUnit",
        unitPrice = 1234,
        discountPrice = 1234,
        vatPercent = 12,
        amount = 1234,
        vatAmount = 12
    )
    /**
     * Check that OrderItem parcelizes correctly
     */
    @Test
    fun testOrderItem() {
        testParcelable(orderItem)
    }

    private val pickUpAddress = PickUpAddress(
        name = "name",
        streetAddress = "streetAddress",
        coAddress = "coAddress",
        city = "city",
        zipCode = "zipCode",
        countryCode = "countryCode"
    )
    /**
     * Check that PickUpAddress parcelizes correctly
     */
    @Test
    fun testPickUpAddress() {
        testParcelable(pickUpAddress)
    }

    private val riskIndicator = RiskIndicator(
        deliveryEmailAddress = "deliveryEmailAddress",
        deliveryTimeFrameIndicator = DeliveryTimeFrameIndicator.ELECTRONIC_DELIVERY,
        preOrderDate = RiskIndicator.formatPreOrderDate(LocalDate.of(2021, 2, 3)),
        preOrderPurchaseIndicator = PreOrderPurchaseIndicator.MERCHANDISE_AVAILABLE,
        shipIndicator = ShipIndicator.PICK_UP_AT_STORE(pickUpAddress),
        giftCardPurchase = false,
        reOrderPurchaseIndicator = ReOrderPurchaseIndicator.FIRST_TIME_ORDERED
    )
    /**
     * Check that RiskIndicator parcelizes correctly
     */
    @Test
    fun testRiskIndicator() {
        testParcelable(riskIndicator)
    }

    /**
     * Check that PaymentOrder parcelizes correctly
     */
    @Test
    fun testPaymentOrder() {
        testParcelable(PaymentOrder(
            operation = PaymentOrderOperation.VERIFY,
            currency = Currency.getInstance("EUR"),
            amount = 1234,
            vatAmount = 12,
            description = "description",
            userAgent = "userAgent",
            language = Language.SWEDISH,
            generateRecurrenceToken = true,
            generatePaymentToken = true,
            disableStoredPaymentDetails = true,
            restrictedToInstruments = listOf("test", "data"),
            urls = paymentOrderUrls,
            payeeInfo = payeeInfo,
            payer = paymentOrderPayer,
            orderItems = listOf(orderItem, orderItem),
            riskIndicator = riskIndicator,
            disablePaymentMenu = true,
            paymentToken = "paymentToken",
            initiatingSystemUserAgent = "initiatingSystemUserAgent"
        ))
    }

    /**
     * Check that Problem parcelizes correctly
     */
    @Test
    fun testProblem() {
        testParcelable(Problem("""
            {
            "type":"https://example.com/notfound",
            "title":"Not Found",
            "status":404,
            "test":"data"
            }
        """.trimIndent()))
    }

    /**
     * Check that MerchantBackendProblem parcelizes correctly
     */
    @Test
    fun testMerchantBackendProblem() {
        testParcelable(MerchantBackendProblem.Client.SwedbankPay.NotFound(JsonObject().apply {
            addProperty("type", "https://api.payex.com/psp/errordetail/notfound")
            addProperty("test", "data")
        }))
    }

    /**
     * Check that TerminalFailure parcelizes correctly
     */
    @Test
    fun testTerminalFailure() {
        testParcelable(Gson().fromJson("""
            {
            "origin":"origin",
            "messageId":"messageId",
            "details":"details"
            }
        """.trimIndent(), TerminalFailure::class.java))
    }

    private fun makeViewPaymentOrderInfo(userData: Any?) = ViewPaymentOrderInfo(
        webViewBaseUrl = "webViewBaseUrl",
        viewPaymentOrder = "viewPaymentOrder",
        completeUrl = "completeUrl",
        cancelUrl = "cancelUrl",
        paymentUrl = "paymentUrl",
        termsOfServiceUrl = "termsOfServiceUrl",
        instrument = "instrument",
        availableInstruments = listOf("test", "data"),
        userData = userData
    )

    /**
     * Check that ViewPaymentOrderInfo parcelizes correctly
     */
    @Test
    fun testViewPaymentOrderInfo() {
        testParcelable(makeViewPaymentOrderInfo(null))
    }

    /**
     * Check that ViewPaymentOrderInfo with String userData parcelizes correctly
     */
    @Test
    fun testStringUserData() {
        testParcelable(makeViewPaymentOrderInfo("userData"))
    }

    /**
     * Check that ViewPaymentOrderInfo with Serializable userData parcelizes correctly
     */
    @Test
    fun testSerializableUserData() {
        data class UserData(
            @Suppress("unused") val field: String
        ) : Serializable
        testParcelable(makeViewPaymentOrderInfo(UserData("field")))
    }

    /*
     * Check that ViewPaymentOrderInfo with Parcelable userData parcelizes correctly
     *
    @Test
    fun testParcelableUserData() {
        class UserData(val field: String) : Parcelable
        testParcelable(makeViewPaymentOrderInfo(UserData("field")))
    }*/
}