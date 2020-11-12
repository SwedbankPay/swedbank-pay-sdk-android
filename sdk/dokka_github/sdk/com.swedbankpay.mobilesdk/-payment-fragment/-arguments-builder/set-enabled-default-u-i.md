[sdk](../../../index.md) / [com.swedbankpay.mobilesdk](../../index.md) / [PaymentFragment](../index.md) / [ArgumentsBuilder](index.md) / [setEnabledDefaultUI](./set-enabled-default-u-i.md)

# setEnabledDefaultUI

`fun setEnabledDefaultUI(vararg defaultUI: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): ArgumentsBuilder`

Set the enabled default user interfaces.

There are four:

* [RETRY_PROMPT](../-r-e-t-r-y_-p-r-o-m-p-t.md), a prompt to retry a failed request that can reasonably be retried
* [COMPLETE_MESSAGE](../-c-o-m-p-l-e-t-e_-m-e-s-s-a-g-e.md), a laconic completion message
* [ERROR_MESSAGE](../-e-r-r-o-r_-m-e-s-s-a-g-e.md) a less laconic, though a bit technical, error message

If a default UI is not enabled, the fragment will be blank instead.

The default is to only enable RETRY_PROMPT and UPDATE_PAYMENTORDER_ERROR_DIALOG.
This is often useful, as a custom retry prompt is likely unnecessary,
and failures in updating a payment order (i.e. setting the instrument)
are unlikely enough not to warrant customized UI, but the success and error states
should cause the fragment to be dismissed.

To disable everything, pass an empty argument list here (a value of 0 also works).
If it is more convenient for you, you may also OR the flags manually and call this
method with the result value.

### Parameters

`defaultUI` - the default UI to enable