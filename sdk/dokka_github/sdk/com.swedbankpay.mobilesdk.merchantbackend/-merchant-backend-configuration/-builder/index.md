[sdk](../../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../../index.md) / [MerchantBackendConfiguration](../index.md) / [Builder](./index.md)

# Builder

`class Builder`

A builder object for [MerchantBackendConfiguration](../index.md).

### Parameters

`backendUrl` - the URL of your merchant backend

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | A builder object for [MerchantBackendConfiguration](../index.md).`Builder(backendUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [backendUrl](backend-url.md) | the URL of your merchant backend`val backendUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [build](build.md) | Creates a [Configuration](../../../com.swedbankpay.mobilesdk/-configuration/index.md) object using the current values of the Builder.`fun build(): `[`MerchantBackendConfiguration`](../index.md) |
| [pinCertificates](pin-certificates.md) | Pins certificates for a hostname pattern.`fun pinCertificates(pattern: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg certificates: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Builder` |
| [requestDecorator](request-decorator.md) | Sets a [RequestDecorator](../../../com.swedbankpay.mobilesdk/-request-decorator/index.md) that adds custom headers to backend requests.`fun requestDecorator(requestDecorator: `[`RequestDecorator`](../../../com.swedbankpay.mobilesdk/-request-decorator/index.md)`): Builder` |
| [whitelistDomain](whitelist-domain.md) | Adds a domain to the list of allowed domains.`fun whitelistDomain(domain: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, includeSubdomains: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): Builder` |
