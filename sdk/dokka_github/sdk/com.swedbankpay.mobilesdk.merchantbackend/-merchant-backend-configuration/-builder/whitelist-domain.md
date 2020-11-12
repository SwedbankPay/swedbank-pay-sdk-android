[sdk](../../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../../index.md) / [MerchantBackendConfiguration](../index.md) / [Builder](index.md) / [whitelistDomain](./whitelist-domain.md)

# whitelistDomain

`fun whitelistDomain(domain: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, includeSubdomains: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): Builder`

Adds a domain to the list of allowed domains.

By default, the list contains the domain of the backend URL,
including its subdomains. If you wish to change that default,
you must call this method for each domain you wish to allow.
If you call this method, the default will NOT be used, so you
need to add the domain of the backend URL explicitly.

### Parameters

`domain` - the domain to whitelist

`includeSubdomains` - if `true`, also adds any subdomains of `domain` to the whitelist

**Return**
this

