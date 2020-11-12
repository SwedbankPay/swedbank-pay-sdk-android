[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentViewModel](index.md) / [updatePaymentOrder](./update-payment-order.md)

# updatePaymentOrder

`fun updatePaymentOrder(updateInfo: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Attempts to update the ongoing payment order.
The meaning of `updateInfo` is up to your  [Configuration.updatePaymentOrder](../-configuration/update-payment-order.md)
implementation.

If you are using
[com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration](../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-configuration/index.md),
and the payment order is in instrument mode, you can set the instrument
by calling this with a `String` argument specifying the new instrument;
see [PaymentInstruments](../-payment-instruments/index.md).

### Parameters

`updateInfo` - any data you need to perform the update. Must be [android.os.Parcelable](https://developer.android.com/reference/android/os/Parcelable.html) or [Serializable](https://docs.oracle.com/javase/6/docs/api/java/io/Serializable.html)