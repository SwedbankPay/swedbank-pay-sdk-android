[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [PaymentViewModel](index.md) / [setOnTermsOfServiceClickListener](./set-on-terms-of-service-click-listener.md)

# setOnTermsOfServiceClickListener

`fun setOnTermsOfServiceClickListener(lifecycleOwner: LifecycleOwner?, listener: OnTermsOfServiceClickListener?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Set an OnTermsOfServiceClickListener to be notified when the user clicks on the Terms of Service link
in the Payment Menu.

Optionally, you may provide a [LifecycleOwner](#) that this listener is bound to.
It will then be automatically removed when the LifecycleOwner is destroyed.
If you do not provide a LifecycleOwner, be careful not to leak expensive objects here.

### Parameters

`lifecycleOwner` - : the LifecycleOwner to bind the listener to, or `null` to keep the listener until the next call to this method

`listener` - the OnTermsOfServiceClickListener to set, or `null` to remove the listener