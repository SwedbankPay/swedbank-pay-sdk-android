//[sdk](../../../../index.md)/[com.swedbankpay.mobilesdk](../../index.md)/[PaymentViewModel](../index.md)/[State](index.md)



# State  
 [androidJvm] enum [State](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)<[PaymentViewModel.State](index.md)> 

State of a payment process

   


## Entries  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.IDLE///PointingToDeclaration/"></a>[IDLE](-i-d-l-e/index.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.IDLE///PointingToDeclaration/"></a> [androidJvm] [IDLE](-i-d-l-e/index.md)()  <br>No payment active   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.IN_PROGRESS///PointingToDeclaration/"></a>[IN_PROGRESS](-i-n_-p-r-o-g-r-e-s-s/index.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.IN_PROGRESS///PointingToDeclaration/"></a> [androidJvm] [IN_PROGRESS](-i-n_-p-r-o-g-r-e-s-s/index.md)()  <br>Payment is active and is waiting either for user interaction or backend response   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.UPDATING_PAYMENT_ORDER///PointingToDeclaration/"></a>[UPDATING_PAYMENT_ORDER](-u-p-d-a-t-i-n-g_-p-a-y-m-e-n-t_-o-r-d-e-r/index.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.UPDATING_PAYMENT_ORDER///PointingToDeclaration/"></a> [androidJvm] [UPDATING_PAYMENT_ORDER](-u-p-d-a-t-i-n-g_-p-a-y-m-e-n-t_-o-r-d-e-r/index.md)()  <br>Payment order is being updated (because you called [updatePaymentOrder](../update-payment-order.md)).   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.COMPLETE///PointingToDeclaration/"></a>[COMPLETE](-c-o-m-p-l-e-t-e/index.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.COMPLETE///PointingToDeclaration/"></a> [androidJvm] [COMPLETE](-c-o-m-p-l-e-t-e/index.md)()  <br>Payment is complete.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.CANCELED///PointingToDeclaration/"></a>[CANCELED](-c-a-n-c-e-l-e-d/index.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.CANCELED///PointingToDeclaration/"></a> [androidJvm] [CANCELED](-c-a-n-c-e-l-e-d/index.md)()  <br>Payment was canceled by the user.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.RETRYABLE_ERROR///PointingToDeclaration/"></a>[RETRYABLE_ERROR](-r-e-t-r-y-a-b-l-e_-e-r-r-o-r/index.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.RETRYABLE_ERROR///PointingToDeclaration/"></a> [androidJvm] [RETRYABLE_ERROR](-r-e-t-r-y-a-b-l-e_-e-r-r-o-r/index.md)()  <br>Payment is active, but could not proceed.   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.FAILURE///PointingToDeclaration/"></a>[FAILURE](-f-a-i-l-u-r-e/index.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State.FAILURE///PointingToDeclaration/"></a> [androidJvm] [FAILURE](-f-a-i-l-u-r-e/index.md)()  <br>Payment has failed.   <br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State/isFinal/#/PointingToDeclaration/"></a>[isFinal](is-final.md)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State/isFinal/#/PointingToDeclaration/"></a> [androidJvm] abstract val [isFinal](is-final.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)true if this is a final state for [PaymentFragment](../../-payment-fragment/index.md), false otherwise   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State/name/#/PointingToDeclaration/"></a>[name](index.md#%5Bcom.swedbankpay.mobilesdk%2FPaymentViewModel.State%2Fname%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State/name/#/PointingToDeclaration/"></a> [androidJvm] val [name](index.md#%5Bcom.swedbankpay.mobilesdk%2FPaymentViewModel.State%2Fname%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>|
| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State/ordinal/#/PointingToDeclaration/"></a>[ordinal](index.md#%5Bcom.swedbankpay.mobilesdk%2FPaymentViewModel.State%2Fordinal%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000)| <a name="com.swedbankpay.mobilesdk/PaymentViewModel.State/ordinal/#/PointingToDeclaration/"></a> [androidJvm] val [ordinal](index.md#%5Bcom.swedbankpay.mobilesdk%2FPaymentViewModel.State%2Fordinal%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-859440000): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)   <br>|

