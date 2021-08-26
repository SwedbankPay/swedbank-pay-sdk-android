package com.swedbankpay.mobilesdk

/**
 * Shipping method for [RiskIndicator].
 */
@Suppress("unused")
class ShipIndicator private constructor(
    internal val serializedName: String,
    internal val pickUpAddress: PickUpAddress? = null
) {
    companion object {
        /**
         * Ship to cardholder's billing address
         */
        @JvmField val SHIP_TO_BILLING_ADDRESS = ShipIndicator("01")

        /**
         * Ship to another verified address on file with the merchant
         */
        @JvmField val SHIP_TO_VERIFIED_ADDRESS = ShipIndicator("02")

        /**
         * Ship to an address different to the cardholder's billing address
         */
        @JvmField val SHIP_TO_DIFFERENT_ADDRESS = ShipIndicator("03")

        /**
         * Ship to store/pick-up at store. Populate the pick-up address as completely as possible.
         *
         * @param pickUpAddress pick-up address details
         */
        @Suppress("FunctionName")
        @JvmStatic fun PICK_UP_AT_STORE(pickUpAddress: PickUpAddress?) = ShipIndicator("04", pickUpAddress)

        /**
         * Digital goods, no physical delivery
         */
        @JvmField val DIGITAL_GOODS = ShipIndicator("05")

        /**
         * Travel and event tickets, no shipping
         */
        @JvmField val TICKETS = ShipIndicator("06")

        /**
         * Other, e.g. gaming, digital service
         */
        @JvmField val OTHER = ShipIndicator("07")
    }
}
