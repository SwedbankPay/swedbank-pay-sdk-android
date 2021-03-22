//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[PickUpAddress](index.md)



# PickUpAddress  
 [androidJvm] data class [PickUpAddress](index.md)(**name**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **streetAddress**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **coAddress**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **city**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **zipCode**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **countryCode**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

Pick-up address data for [RiskIndicator](../-risk-indicator/index.md).



When using [ShipIndicator.PICK_UP_AT_STORE](../-ship-indicator/-companion/-p-i-c-k_-u-p_-a-t_-s-t-o-r-e.md), you should populate this data as completely as possible to decrease the risk factor of the purchase.

   


## Constructors  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk/PickUpAddress/PickUpAddress/#android.os.Parcel/PointingToDeclaration/"></a>[PickUpAddress](-pick-up-address.md)| <a name="com.swedbankpay.mobilesdk/PickUpAddress/PickUpAddress/#android.os.Parcel/PointingToDeclaration/"></a> [androidJvm] fun [PickUpAddress](-pick-up-address.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html))   <br>|
| <a name="com.swedbankpay.mobilesdk/PickUpAddress/PickUpAddress/#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?/PointingToDeclaration/"></a>[PickUpAddress](-pick-up-address.md)| <a name="com.swedbankpay.mobilesdk/PickUpAddress/PickUpAddress/#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?#kotlin.String?/PointingToDeclaration/"></a> [androidJvm] fun [PickUpAddress](-pick-up-address.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, streetAddress: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, coAddress: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, city: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, zipCode: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, countryCode: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null)   <br>|


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PickUpAddress.Companion///PointingToDeclaration/"></a>[Companion](-companion/index.md)| <a name="com.swedbankpay.mobilesdk/PickUpAddress.Companion///PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PickUpAddress/describeContents/#/PointingToDeclaration/"></a>[describeContents](describe-contents.md)| <a name="com.swedbankpay.mobilesdk/PickUpAddress/describeContents/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [describeContents](describe-contents.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/PickUpAddress/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[writeToParcel](write-to-parcel.md)| <a name="com.swedbankpay.mobilesdk/PickUpAddress/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open override fun [writeToParcel](write-to-parcel.md)(parcel: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/PickUpAddress/city/#/PointingToDeclaration/"></a>[city](city.md)| <a name="com.swedbankpay.mobilesdk/PickUpAddress/city/#/PointingToDeclaration/"></a> [androidJvm] @(value = city)  <br>  <br>val [city](city.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullCity of the payer   <br>|
| <a name="com.swedbankpay.mobilesdk/PickUpAddress/coAddress/#/PointingToDeclaration/"></a>[coAddress](co-address.md)| <a name="com.swedbankpay.mobilesdk/PickUpAddress/coAddress/#/PointingToDeclaration/"></a> [androidJvm] @(value = coAddress)  <br>  <br>val [coAddress](co-address.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullC/O address of the payer   <br>|
| <a name="com.swedbankpay.mobilesdk/PickUpAddress/countryCode/#/PointingToDeclaration/"></a>[countryCode](country-code.md)| <a name="com.swedbankpay.mobilesdk/PickUpAddress/countryCode/#/PointingToDeclaration/"></a> [androidJvm] @(value = countryCode)  <br>  <br>val [countryCode](country-code.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullCountry code of the payer   <br>|
| <a name="com.swedbankpay.mobilesdk/PickUpAddress/name/#/PointingToDeclaration/"></a>[name](name.md)| <a name="com.swedbankpay.mobilesdk/PickUpAddress/name/#/PointingToDeclaration/"></a> [androidJvm] @(value = name)  <br>  <br>val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullName of the payer   <br>|
| <a name="com.swedbankpay.mobilesdk/PickUpAddress/streetAddress/#/PointingToDeclaration/"></a>[streetAddress](street-address.md)| <a name="com.swedbankpay.mobilesdk/PickUpAddress/streetAddress/#/PointingToDeclaration/"></a> [androidJvm] @(value = streetAddress)  <br>  <br>val [streetAddress](street-address.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullStreet address of the payer   <br>|
| <a name="com.swedbankpay.mobilesdk/PickUpAddress/zipCode/#/PointingToDeclaration/"></a>[zipCode](zip-code.md)| <a name="com.swedbankpay.mobilesdk/PickUpAddress/zipCode/#/PointingToDeclaration/"></a> [androidJvm] @(value = zipCode)  <br>  <br>val [zipCode](zip-code.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = nullZip code of the payer   <br>|

