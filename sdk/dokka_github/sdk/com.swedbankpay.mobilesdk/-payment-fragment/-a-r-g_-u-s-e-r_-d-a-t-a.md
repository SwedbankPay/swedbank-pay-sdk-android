[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentFragment](index.md) / [ARG_USER_DATA](./-a-r-g_-u-s-e-r_-d-a-t-a.md)

# ARG_USER_DATA

`const val ARG_USER_DATA: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Argument key: Any data that you may need in your [Configuration](../-configuration/index.md) to prepare the checkin
and payment menu for this payment. You will receive this value as the `userData` argument
in your [Configuration.postConsumers](../-configuration/post-consumers.md) and [Configuration.postPaymentorders](../-configuration/post-paymentorders.md) methods.

Value must be [android.os.Parcelable](https://developer.android.com/reference/android/os/Parcelable.html) or [Serializable](https://docs.oracle.com/javase/6/docs/api/java/io/Serializable.html).

