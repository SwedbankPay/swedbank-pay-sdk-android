[sdk](../index.md) / [com.swedbankpay.mobilesdk](./index.md)

## Package com.swedbankpay.mobilesdk

### Types

| Name | Summary |
|---|---|
| [Configuration](-configuration/index.md) | The Swedbank Pay configuration for your application.`abstract class Configuration` |
| [ConfigurationCompat](-configuration-compat/index.md) | Java compatibility wrapper for [Configuration](-configuration/index.md).`abstract class ConfigurationCompat : `[`Configuration`](-configuration/index.md) |
| [Consumer](-consumer/index.md) | A consumer to identify using the [checkin](https://developer.swedbankpay.com/checkout/checkin) flow.`data class Consumer : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)`, ExtensibleJsonObject` |
| [ConsumerOperation](-consumer-operation/index.md) | Operations that can be performed with a [Consumer](-consumer/index.md).`enum class ConsumerOperation` |
| [DeliveryTimeFrameIndicator](-delivery-time-frame-indicator/index.md) | Product delivery timeframe for a [RiskIndicator](-risk-indicator/index.md).`enum class DeliveryTimeFrameIndicator` |
| [ItemType](-item-type/index.md) | The type of an [OrderItem](-order-item/index.md).`enum class ItemType` |
| [Language](-language/index.md) | Languages supported by checkin and payment menu.`enum class Language` |
| [OrderItem](-order-item/index.md) | An item being paid for, part of a [PaymentOrder](-payment-order/index.md).`data class OrderItem : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html) |
| [PayeeInfo](-payee-info/index.md) | Information about the payee (recipient) of a payment order`data class PayeeInfo : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html) |
| [PaymentFragment](-payment-fragment/index.md) | A [Fragment](#) that handles a payment process.`open class PaymentFragment : Fragment` |
| [PaymentInstruments](-payment-instruments/index.md) | Constant values for common payment instruments`object PaymentInstruments` |
| [PaymentOrder](-payment-order/index.md) | Description a payment order.`data class PaymentOrder : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)`, ExtensibleJsonObject` |
| [PaymentOrderOperation](-payment-order-operation/index.md) | Type of operation the payment order performs`enum class PaymentOrderOperation` |
| [PaymentOrderPayer](-payment-order-payer/index.md) | Information about the payer of a payment order`data class PaymentOrderPayer : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html) |
| [PaymentOrderUrls](-payment-order-urls/index.md) | A set of URLs relevant to a payment order.`data class PaymentOrderUrls : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html) |
| [PaymentViewModel](-payment-view-model/index.md) | ViewModel for communicating with a [PaymentFragment](-payment-fragment/index.md).`class PaymentViewModel : AndroidViewModel` |
| [PickUpAddress](-pick-up-address/index.md) | Pick-up address data for [RiskIndicator](-risk-indicator/index.md).`data class PickUpAddress : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html) |
| [PreOrderPurchaseIndicator](-pre-order-purchase-indicator/index.md) | Purchase indicator values for [RiskIndicator](-risk-indicator/index.md).`enum class PreOrderPurchaseIndicator` |
| [Problem](-problem/index.md) | An RFC 7807 HTTP API Problem Details object.`open class Problem : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)`, `[`Serializable`](https://docs.oracle.com/javase/6/docs/api/java/io/Serializable.html) |
| [ReOrderPurchaseIndicator](-re-order-purchase-indicator/index.md) | Re-order purchase indicator values for [RiskIndicator](-risk-indicator/index.md).`enum class ReOrderPurchaseIndicator` |
| [RequestDecorator](-request-decorator/index.md) | Callback for adding custom headers to backend requests.`abstract class RequestDecorator` |
| [RequestDecoratorCompat](-request-decorator-compat/index.md) | Java compatibility wrapper for [RequestDecorator](-request-decorator/index.md).`open class RequestDecoratorCompat : `[`RequestDecorator`](-request-decorator/index.md) |
| [RiskIndicator](-risk-indicator/index.md) | Optional information to reduce the risk factor of a payment.`data class RiskIndicator : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html) |
| [ShipIndicator](-ship-indicator/index.md) | Shipping method for [RiskIndicator](-risk-indicator/index.md).`class ShipIndicator` |
| [TerminalFailure](-terminal-failure/index.md) | Describes a terminal error condition signaled by an onError callback from Swedbank Pay.`class TerminalFailure : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html) |
| [UserHeaders](-user-headers/index.md) | Builder for custom headers.`class UserHeaders` |
| [ViewConsumerIdentificationInfo](-view-consumer-identification-info/index.md) | Data required to show the checkin view.`data class ViewConsumerIdentificationInfo` |
| [ViewPaymentOrderInfo](-view-payment-order-info/index.md) | Data required to show the payment menu.`data class ViewPaymentOrderInfo : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html) |

### Extensions for External Classes

| Name | Summary |
|---|---|
| [androidx.fragment.app.FragmentActivity](androidx.fragment.app.-fragment-activity/index.md) |  |
