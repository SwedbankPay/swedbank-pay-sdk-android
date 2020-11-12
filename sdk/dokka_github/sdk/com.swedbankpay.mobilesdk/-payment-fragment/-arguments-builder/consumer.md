[sdk](../../../index.md) / [com.swedbankpay.mobilesdk](../../index.md) / [PaymentFragment](../index.md) / [ArgumentsBuilder](index.md) / [consumer](./consumer.md)

# consumer

`fun consumer(consumer: `[`Consumer`](../../-consumer/index.md)`?): ArgumentsBuilder`

Sets a consumer for this payment.
Also enables or disables checkin based on the argument:
If `consumer` is `null`, disables checkin;
if consumer `consumer` is not `null`, enables checkin.
If you wish to override this, call [useCheckin](use-checkin.md) afterwards.

### Parameters

`consumer` - the consumer making this payment