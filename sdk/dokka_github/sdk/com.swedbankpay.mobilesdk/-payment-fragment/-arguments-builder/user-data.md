[sdk](../../../index.md) / [com.swedbankpay.mobilesdk](../../index.md) / [PaymentFragment](../index.md) / [ArgumentsBuilder](index.md) / [userData](./user-data.md)

# userData

`fun userData(userData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): ArgumentsBuilder`

Sets custom data for the payment.

[com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration](../../../com.swedbankpay.mobilesdk.merchantbackend/-merchant-backend-configuration/index.md)
does not use this parameter.
If you create a custom [Configuration](../../-configuration/index.md), you may set any [android.os.Parcelable](https://developer.android.com/reference/android/os/Parcelable.html)
or [Serializable](https://docs.oracle.com/javase/6/docs/api/java/io/Serializable.html) object here, and receive it in your [Configuration](../../-configuration/index.md) callbacks.
Note that due to possible saving and restoring of the argument bundle, you should
not rely on receiving the same object as you set here, but an equal one.

Note that passing general Serializable objects is not recommended.
[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) has special treatment and is okay to pass here.

### Parameters

`userData` - data for your [Configuration](../../-configuration/index.md)