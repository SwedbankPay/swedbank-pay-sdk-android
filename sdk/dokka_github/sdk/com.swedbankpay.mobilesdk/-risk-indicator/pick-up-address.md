[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RiskIndicator](index.md) / [pickUpAddress](./pick-up-address.md)

# pickUpAddress

`val pickUpAddress: `[`PickUpAddress`](../-pick-up-address/index.md)`?`

If [shipIndicator](ship-indicator.md) is "04", i.e. [ShipIndicator.PICK_UP_AT_STORE](../-ship-indicator/-p-i-c-k_-u-p_-a-t_-s-t-o-r-e.md), this field should be
populated.

You should use the constructor that takes a [ShipIndicator](../-ship-indicator/index.md) argument for `shipIndicator`,
which also set sets this field properly.

