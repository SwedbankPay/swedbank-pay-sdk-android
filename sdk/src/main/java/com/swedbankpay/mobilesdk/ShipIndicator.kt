package com.swedbankpay.mobilesdk

class ShipIndicator private constructor(
    internal val serializedName: String,
    internal val pickUpAddress: PickUpAddress? = null
) {
    companion object {
        @JvmField val SHIP_TO_BILLING_ADDRESS = ShipIndicator("01")
        @JvmField val SHIP_TO_VERIFIED_ADDRESS = ShipIndicator("02")
        @JvmField val SHIP_TO_DIFFERENT_ADDRESS = ShipIndicator("03")
        @JvmStatic fun PICK_UP_AT_STORE(pickUpAddress: PickUpAddress?) = ShipIndicator("04", pickUpAddress)
        @JvmField val DIGITAL_GOODS = ShipIndicator("05")
        @JvmField val TICKETS = ShipIndicator("06")
        @JvmField val OTHER = ShipIndicator("07")
    }
}
