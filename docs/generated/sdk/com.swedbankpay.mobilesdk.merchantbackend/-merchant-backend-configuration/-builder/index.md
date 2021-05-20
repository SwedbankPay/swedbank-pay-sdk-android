//[sdk](../../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../../index.md)/[MerchantBackendConfiguration](../index.md)/[Builder](index.md)



# Builder  
 [androidJvm] class [Builder](index.md)(**backendUrl**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))

A builder object for [MerchantBackendConfiguration](../index.md).

   


## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder///PointingToDeclaration/"></a>backendUrl| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder///PointingToDeclaration/"></a><br><br>the URL of your merchant backend<br><br>|
  


## Constructors  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/Builder/#kotlin.String/PointingToDeclaration/"></a>[Builder](-builder.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/Builder/#kotlin.String/PointingToDeclaration/"></a> [androidJvm] fun [Builder](-builder.md)(backendUrl: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))the URL of your merchant backend   <br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/build/#/PointingToDeclaration/"></a>[build](build.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/build/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [build](build.md)(): [MerchantBackendConfiguration](../index.md)  <br>More info  <br>Creates a [Configuration](../../../com.swedbankpay.mobilesdk/-configuration/index.md) object using the current values of the Builder.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/pinCertificates/#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a>[pinCertificates](pin-certificates.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/pinCertificates/#kotlin.String#kotlin.Array[kotlin.String]/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [pinCertificates](pin-certificates.md)(pattern: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), vararg certificates: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [MerchantBackendConfiguration.Builder](index.md)  <br>More info  <br>Pins certificates for a hostname pattern.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/requestDecorator/#com.swedbankpay.mobilesdk.RequestDecorator/PointingToDeclaration/"></a>[requestDecorator](request-decorator.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/requestDecorator/#com.swedbankpay.mobilesdk.RequestDecorator/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [requestDecorator](request-decorator.md)(requestDecorator: [RequestDecorator](../../../com.swedbankpay.mobilesdk/-request-decorator/index.md)): [MerchantBackendConfiguration.Builder](index.md)  <br>More info  <br>Sets a [RequestDecorator](../../../com.swedbankpay.mobilesdk/-request-decorator/index.md) that adds custom headers to backend requests.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/whitelistDomain/#kotlin.String#kotlin.Boolean/PointingToDeclaration/"></a>[whitelistDomain](whitelist-domain.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/whitelistDomain/#kotlin.String#kotlin.Boolean/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [whitelistDomain](whitelist-domain.md)(domain: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), includeSubdomains: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [MerchantBackendConfiguration.Builder](index.md)  <br>More info  <br>Adds a domain to the list of allowed domains.  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/backendUrl/#/PointingToDeclaration/"></a>[backendUrl](backend-url.md)| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/backendUrl/#/PointingToDeclaration/"></a> [androidJvm] val [backendUrl](backend-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)the URL of your merchant backend   <br>|

