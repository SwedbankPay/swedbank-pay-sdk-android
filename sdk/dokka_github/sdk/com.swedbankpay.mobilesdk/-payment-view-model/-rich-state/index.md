//[sdk](../../../../index.md)/[com.swedbankpay.mobilesdk](../../index.md)/[PaymentViewModel](../index.md)/[RichState](index.md)



# RichState  
 [androidJvm] class [RichState](index.md)

Contains the state of the payment process and possible associated data.

   


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.RichState/exception/#/PointingToDeclaration/"></a>[exception](exception.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.RichState/exception/#/PointingToDeclaration/"></a> [androidJvm] val [exception](exception.md): [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)?If the current state is [RETRYABLE_ERROR](../-state/-r-e-t-r-y-a-b-l-e_-e-r-r-o-r/index.md), or [FAILURE](../-state/-f-a-i-l-u-r-e/index.md) caused by an [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html), this property contains that exception.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.RichState/retryableErrorMessage/#/PointingToDeclaration/"></a>[retryableErrorMessage](retryable-error-message.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.RichState/retryableErrorMessage/#/PointingToDeclaration/"></a> [androidJvm] val [retryableErrorMessage](retryable-error-message.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?If the current state is [RETRYABLE_ERROR](../-state/-r-e-t-r-y-a-b-l-e_-e-r-r-o-r/index.md), this property contains an error message describing the situation.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.RichState/state/#/PointingToDeclaration/"></a>[state](state.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.RichState/state/#/PointingToDeclaration/"></a> [androidJvm] val [state](state.md): [PaymentViewModel.State](../-state/index.md)The state of the payment process.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.RichState/terminalFailure/#/PointingToDeclaration/"></a>[terminalFailure](terminal-failure.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.RichState/terminalFailure/#/PointingToDeclaration/"></a> [androidJvm] val [terminalFailure](terminal-failure.md): [TerminalFailure](../../-terminal-failure/index.md)?If the current state is [FAILURE](../-state/-f-a-i-l-u-r-e/index.md), and it was caused by an onError callback from the Chekout API, this property contains an object describing the error.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.RichState/updateException/#/PointingToDeclaration/"></a>[updateException](update-exception.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.RichState/updateException/#/PointingToDeclaration/"></a> [androidJvm] val [updateException](update-exception.md): [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)?If the current state is [IN_PROGRESS](../-state/-i-n_-p-r-o-g-r-e-s-s/index.md), and an attempt to update the payment order failed, the cause of the failure.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.RichState/viewPaymentOrderInfo/#/PointingToDeclaration/"></a>[viewPaymentOrderInfo](view-payment-order-info.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.RichState/viewPaymentOrderInfo/#/PointingToDeclaration/"></a> [androidJvm] val [viewPaymentOrderInfo](view-payment-order-info.md): [ViewPaymentOrderInfo](../../-view-payment-order-info/index.md)?If the current state is [IN_PROGRESS](../-state/-i-n_-p-r-o-g-r-e-s-s/index.md) or [UPDATING_PAYMENT_ORDER](../-state/-u-p-d-a-t-i-n-g_-p-a-y-m-e-n-t_-o-r-d-e-r/index.md), the [ViewPaymentOrderInfo](../../-view-payment-order-info/index.md) object describing the current payment order.   <br>|

