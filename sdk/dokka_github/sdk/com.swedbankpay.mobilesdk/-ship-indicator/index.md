[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [ShipIndicator](./index.md)

# ShipIndicator

`class ShipIndicator`

Shipping method for [RiskIndicator](../-risk-indicator/index.md).

### Companion Object Properties

| Name | Summary |
|---|---|
| [DIGITAL_GOODS](-d-i-g-i-t-a-l_-g-o-o-d-s.md) | Digital goods, no physical delivery`val DIGITAL_GOODS: `[`ShipIndicator`](./index.md) |
| [OTHER](-o-t-h-e-r.md) | Other, e.g. gaming, digital service`val OTHER: `[`ShipIndicator`](./index.md) |
| [SHIP_TO_BILLING_ADDRESS](-s-h-i-p_-t-o_-b-i-l-l-i-n-g_-a-d-d-r-e-s-s.md) | Ship to cardholder's billing address`val SHIP_TO_BILLING_ADDRESS: `[`ShipIndicator`](./index.md) |
| [SHIP_TO_DIFFERENT_ADDRESS](-s-h-i-p_-t-o_-d-i-f-f-e-r-e-n-t_-a-d-d-r-e-s-s.md) | Ship to an address different to the cardholder's billing address`val SHIP_TO_DIFFERENT_ADDRESS: `[`ShipIndicator`](./index.md) |
| [SHIP_TO_VERIFIED_ADDRESS](-s-h-i-p_-t-o_-v-e-r-i-f-i-e-d_-a-d-d-r-e-s-s.md) | Ship to another verified address on file with the merchant`val SHIP_TO_VERIFIED_ADDRESS: `[`ShipIndicator`](./index.md) |
| [TICKETS](-t-i-c-k-e-t-s.md) | Travel and event tickets, no shipping`val TICKETS: `[`ShipIndicator`](./index.md) |

### Companion Object Functions

| Name | Summary |
|---|---|
| [PICK_UP_AT_STORE](-p-i-c-k_-u-p_-a-t_-s-t-o-r-e.md) | Ship to store/pick-up at store. Populate the pick-up address as completely as possible.`fun PICK_UP_AT_STORE(pickUpAddress: `[`PickUpAddress`](../-pick-up-address/index.md)`?): `[`ShipIndicator`](./index.md) |
