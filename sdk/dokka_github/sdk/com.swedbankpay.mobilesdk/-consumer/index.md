//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[Consumer](index.md)



# Consumer  
 [androidJvm] data class [Consumer](index.md)(**operation**: [ConsumerOperation](../-consumer-operation/index.md), **language**: [Language](../-language/index.md), **shippingAddressRestrictedToCountryCodes**: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>, **extensionProperties**: [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html)?) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html), 

A consumer to identify using the [checkin](https://developer.swedbankpay.com/checkout/checkin) flow.



Please refer to the [Swedbank Pay documentation](https://developer.swedbankpay.com/checkout/checkin#checkin-back-end) for further information.

   


## Constructors  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/Consumer/Consumer/#com.swedbankpay.mobilesdk.ConsumerOperation#com.swedbankpay.mobilesdk.Language#kotlin.collections.List[kotlin.String]#android.os.Bundle?/PointingToDeclaration/"></a>[Consumer](-consumer.md)| <a name="com.swedbankpay.mobilesdk/Consumer/Consumer/#com.swedbankpay.mobilesdk.ConsumerOperation#com.swedbankpay.mobilesdk.Language#kotlin.collections.List[kotlin.String]#android.os.Bundle?/PointingToDeclaration/"></a> [androidJvm] fun [Consumer](-consumer.md)(operation: [ConsumerOperation](../-consumer-operation/index.md) = ConsumerOperation.INITIATE_CONSUMER_SESSION, language: [Language](../-language/index.md) = Language.ENGLISH, shippingAddressRestrictedToCountryCodes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>, extensionProperties: [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html)? = null)   <br>|


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/Consumer.Companion///PointingToDeclaration/"></a>[Companion](-companion/index.md)| <a name="com.swedbankpay.mobilesdk/Consumer.Companion///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/Consumer/describeContents/#/PointingToDeclaration/"></a>[describeContents](describe-contents.md)| <a name="com.swedbankpay.mobilesdk/Consumer/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [describeContents](describe-contents.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/Consumer/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](write-to-parcel.md)| <a name="com.swedbankpay.mobilesdk/Consumer/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [writeToParcel](write-to-parcel.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/Consumer/extensionProperties/#/PointingToDeclaration/"></a>[extensionProperties](extension-properties.md)| <a name="com.swedbankpay.mobilesdk/Consumer/extensionProperties/#/PointingToDeclaration/"></a> [androidJvm] @[Transient](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-transient/index.html)()  <br>  <br>open override val [extensionProperties](extension-properties.md): [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html)? = null   <br>|
| <a name="com.swedbankpay.mobilesdk/Consumer/language/#/PointingToDeclaration/"></a>[language](language.md)| <a name="com.swedbankpay.mobilesdk/Consumer/language/#/PointingToDeclaration/"></a> [androidJvm] @(value = language)  <br>  <br>val [language](language.md): [Language](../-language/index.md)The language to use in the checkin view.   <br>|
| <a name="com.swedbankpay.mobilesdk/Consumer/operation/#/PointingToDeclaration/"></a>[operation](operation.md)| <a name="com.swedbankpay.mobilesdk/Consumer/operation/#/PointingToDeclaration/"></a> [androidJvm] @(value = operation)  <br>  <br>val [operation](operation.md): [ConsumerOperation](../-consumer-operation/index.md)The operation to perform.   <br>|
| <a name="com.swedbankpay.mobilesdk/Consumer/shippingAddressRestrictedToCountryCodes/#/PointingToDeclaration/"></a>[shippingAddressRestrictedToCountryCodes](shipping-address-restricted-to-country-codes.md)| <a name="com.swedbankpay.mobilesdk/Consumer/shippingAddressRestrictedToCountryCodes/#/PointingToDeclaration/"></a> [androidJvm] @(value = shippingAddressRestrictedToCountryCodes)  <br>  <br>val [shippingAddressRestrictedToCountryCodes](shipping-address-restricted-to-country-codes.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>List of ISO-3166 codes of countries the merchant can ship to.   <br>|

