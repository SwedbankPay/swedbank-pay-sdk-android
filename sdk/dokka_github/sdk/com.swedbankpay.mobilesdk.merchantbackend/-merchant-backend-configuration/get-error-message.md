[sdk](../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../index.md) / [MerchantBackendConfiguration](index.md) / [getErrorMessage](./get-error-message.md)

# getErrorMessage

`fun getErrorMessage(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, exception: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

Called by [PaymentFragment](../../com.swedbankpay.mobilesdk/-payment-fragment/index.md) when it needs to show an error message
because an operation failed.

You can return null if you have no further details to provide.

### Parameters

`context` - an application context

`exception` - the exception that caused the failure

**Return**
an error message

