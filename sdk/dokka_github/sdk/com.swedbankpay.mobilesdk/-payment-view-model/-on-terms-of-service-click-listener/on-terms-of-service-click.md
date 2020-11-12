[sdk](../../../index.md) / [com.swedbankpay.mobilesdk](../../index.md) / [PaymentViewModel](../index.md) / [OnTermsOfServiceClickListener](index.md) / [onTermsOfServiceClick](./on-terms-of-service-click.md)

# onTermsOfServiceClick

`abstract fun onTermsOfServiceClick(paymentFragment: `[`PaymentFragment`](../../-payment-fragment/index.md)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Called when the user clicks on the Terms of Service link in the Payment Menu.

### Parameters

`paymentFragment` - the [PaymentFragment](../../-payment-fragment/index.md) the user is interacting with

`url` - the Terms of Service url

**Return**
`true` if you handled the event yourself and wish to disable the default behaviour, `false` if you want to let the SDK show the ToS web page.

