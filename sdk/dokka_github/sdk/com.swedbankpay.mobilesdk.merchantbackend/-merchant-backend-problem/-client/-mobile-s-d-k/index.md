[sdk](../../../../index.md) / [com.swedbankpay.mobilesdk.merchantbackend](../../../index.md) / [MerchantBackendProblem](../../index.md) / [Client](../index.md) / [MobileSDK](./index.md)

# MobileSDK

`sealed class MobileSDK : Client`

Base class for [Client](../index.md) Problems defined by the example backend.

### Types

| Name | Summary |
|---|---|
| [InvalidRequest](-invalid-request.md) | The merchant backend did not understand the request.`class InvalidRequest : MobileSDK` |
| [Unauthorized](-unauthorized.md) | The merchant backend rejected the request because its authentication headers were invalid.`class Unauthorized : MobileSDK` |
