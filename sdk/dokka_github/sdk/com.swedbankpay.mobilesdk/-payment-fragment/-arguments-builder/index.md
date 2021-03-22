//[sdk](../../../../index.md)/[com.swedbankpay.mobilesdk](../../index.md)/[PaymentFragment](../index.md)/[ArgumentsBuilder](index.md)



# ArgumentsBuilder  
 [androidJvm] class [ArgumentsBuilder](index.md)

Builder class for the argument [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html) used by PaymentFragment.

   


## Constructors  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/ArgumentsBuilder/#/PointingToDeclaration/"></a>[ArgumentsBuilder](-arguments-builder.md)| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/ArgumentsBuilder/#/PointingToDeclaration/"></a> [androidJvm] fun [ArgumentsBuilder](-arguments-builder.md)()   <br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/build/#/PointingToDeclaration/"></a>[build](build.md)| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/build/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [build](build.md)(): [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html)  <br>More info  <br>Convenience for build(Bundle()).  <br><br><br>[androidJvm]  <br>Content  <br>fun [build](build.md)(bundle: [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html)): [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html)  <br>More info  <br>Adds the values in this ArgumentsBuilder to a [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html).  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/consumer/#com.swedbankpay.mobilesdk.Consumer?/PointingToDeclaration/"></a>[consumer](consumer.md)| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/consumer/#com.swedbankpay.mobilesdk.Consumer?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [consumer](consumer.md)(consumer: [Consumer](../../-consumer/index.md)?): [PaymentFragment.ArgumentsBuilder](index.md)  <br>More info  <br>Sets a consumer for this payment.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/debugIntentUris/#kotlin.Boolean/PointingToDeclaration/"></a>[debugIntentUris](debug-intent-uris.md)| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/debugIntentUris/#kotlin.Boolean/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [debugIntentUris](debug-intent-uris.md)(debugIntentUris: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PaymentFragment.ArgumentsBuilder](index.md)  <br>More info  <br>Enables or disables verbose error dialogs when Android Intent Uris do not function correctly.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/paymentOrder/#com.swedbankpay.mobilesdk.PaymentOrder?/PointingToDeclaration/"></a>[paymentOrder](payment-order.md)| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/paymentOrder/#com.swedbankpay.mobilesdk.PaymentOrder?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [paymentOrder](payment-order.md)(paymentOrder: [PaymentOrder](../../-payment-order/index.md)?): [PaymentFragment.ArgumentsBuilder](index.md)  <br>More info  <br>Sets the payment order to create  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/setEnabledDefaultUI/#kotlin.IntArray/PointingToDeclaration/"></a>[setEnabledDefaultUI](set-enabled-default-u-i.md)| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/setEnabledDefaultUI/#kotlin.IntArray/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [setEnabledDefaultUI](set-enabled-default-u-i.md)(vararg defaultUI: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PaymentFragment.ArgumentsBuilder](index.md)  <br>More info  <br>Set the enabled default user interfaces.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/useBrowser/#kotlin.Boolean/PointingToDeclaration/"></a>[useBrowser](use-browser.md)| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/useBrowser/#kotlin.Boolean/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [useBrowser](use-browser.md)(external: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [PaymentFragment.ArgumentsBuilder](index.md)  <br>More info  <br>Sets if the payment flow should open an external browser or continue in WebView.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/useCheckin/#kotlin.Boolean/PointingToDeclaration/"></a>[useCheckin](use-checkin.md)| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/useCheckin/#kotlin.Boolean/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [useCheckin](use-checkin.md)(useCheckin: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PaymentFragment.ArgumentsBuilder](index.md)  <br>More info  <br>Enables or disables checkin for this payment.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/userData/#kotlin.Any?/PointingToDeclaration/"></a>[userData](user-data.md)| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/userData/#kotlin.Any?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [userData](user-data.md)(userData: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [PaymentFragment.ArgumentsBuilder](index.md)  <br>More info  <br>Sets custom data for the payment.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/viewModelProviderKey/#kotlin.String?/PointingToDeclaration/"></a>[viewModelProviderKey](view-model-provider-key.md)| <a name="com.swedbankpay.mobilesdk/PaymentFragment.ArgumentsBuilder/viewModelProviderKey/#kotlin.String?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [viewModelProviderKey](view-model-provider-key.md)(viewModelKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): [PaymentFragment.ArgumentsBuilder](index.md)  <br>More info  <br>Sets the key used on the containing [activity's](https://developer.android.com/reference/kotlin/androidx/fragment/app/FragmentActivity.html)[ViewModelProvider](https://developer.android.com/reference/kotlin/androidx/lifecycle/ViewModelProvider.html) for the [PaymentViewModel](../../-payment-view-model/index.md).  <br><br><br>|

