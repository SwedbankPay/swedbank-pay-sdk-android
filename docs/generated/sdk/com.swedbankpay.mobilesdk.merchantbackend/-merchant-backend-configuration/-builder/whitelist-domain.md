//[sdk](../../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../../index.md)/[MerchantBackendConfiguration](../index.md)/[Builder](index.md)/[whitelistDomain](whitelist-domain.md)



# whitelistDomain  
[androidJvm]  
Content  
fun [whitelistDomain](whitelist-domain.md)(domain: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), includeSubdomains: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [MerchantBackendConfiguration.Builder](index.md)  
More info  


Adds a domain to the list of allowed domains.



By default, the list contains the domain of the backend URL, including its subdomains. If you wish to change that default, you must call this method for each domain you wish to allow. If you call this method, the default will NOT be used, so you need to add the domain of the backend URL explicitly.



#### Return  


this



## Parameters  
  
androidJvm  
  
| | |
|---|---|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/whitelistDomain/#kotlin.String#kotlin.Boolean/PointingToDeclaration/"></a>domain| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/whitelistDomain/#kotlin.String#kotlin.Boolean/PointingToDeclaration/"></a><br><br>the domain to whitelist<br><br>|
| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/whitelistDomain/#kotlin.String#kotlin.Boolean/PointingToDeclaration/"></a>includeSubdomains| <a name="com.swedbankpay.mobilesdk.merchantbackend/MerchantBackendConfiguration.Builder/whitelistDomain/#kotlin.String#kotlin.Boolean/PointingToDeclaration/"></a><br><br>if true, also adds any subdomains of domain to the whitelist<br><br>|
  
  



